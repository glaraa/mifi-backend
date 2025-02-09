package com.app.mifi.service.impl;

import com.app.mifi.controller.model.BuddyRequestResponse;
import com.app.mifi.controller.model.MutualBuddyResponse;
import com.app.mifi.controller.model.SuggestionType;
import com.app.mifi.controller.model.UserBuddyResponse;
import com.app.mifi.controller.model.UserRelationship;
import com.app.mifi.persist.entity.BuddyRequest;
import com.app.mifi.persist.entity.User;
import com.app.mifi.persist.entity.UserBuddy;
import com.app.mifi.repository.BuddyRequestRepository;
import com.app.mifi.repository.UserBuddyRepository;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.BuddyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class BuddyServiceImpl implements BuddyService {
    private final UserRepository userRepository;
    private final BuddyRequestRepository buddyRequestRepository;
    private final UserBuddyRepository userBuddyRepository;
    @Override
    public BuddyRequestResponse createBuddyRequest(Long userId,Long reqUserId) {
        BuddyRequest buddyRequest=new BuddyRequest();
        buddyRequest.setRequesterUser(userRepository.findById(reqUserId).orElse(new User()));
        buddyRequest.setRecipientUser(userRepository.findById(userId).orElse(new User()));
        return buddyRequestRepository.save(buddyRequest).toDto();
    }

    @Override
    public List<BuddyRequestResponse> fetchBuddyRequests(Long userId) {
        List<BuddyRequestResponse> buddyRequestResponses=new ArrayList<>();
        buddyRequestRepository.findAllByRecipientUser_UserId(userId).forEach(buddyRequest -> {
            buddyRequestResponses.add(buddyRequest.toDto());
        });
        return buddyRequestResponses;
    }

    @Override
    public List<MutualBuddyResponse> suggestBuddies(Long userId) {
        Set<MutualBuddyResponse> finalSuggestions = new TreeSet<>((a, b) -> {
            int comparison = b.getMutualCount().compareTo(a.getMutualCount());
            if (comparison == 0) {
                return a.getSuggestedUser().getUserId().compareTo(b.getSuggestedUser().getUserId());
            }
            return comparison;
        });
        User user = userRepository.findById(userId).orElse(null);
        if (nonNull(user)) {
            List<User> sameCategoryUsers = userRepository.findAllByCategoryAndUserIdNot(user.getCategory(),userId);
            List<UserBuddy> userBuddies = userBuddyRepository.findAllByUser_UserId(userId);
            Map<User, Integer> mutualBuddies = new HashMap<>();
            userBuddies.forEach(userBuddy -> {
                List<UserBuddy> mutualBuddyList = userBuddyRepository.findAllByUser_UserId(userBuddy.getBuddyUser().getUserId());
                mutualBuddyList.forEach(mutualBuddy -> {
                    User mutualUser = mutualBuddy.getBuddyUser();
                    if (!mutualUser.getUserId().equals(userId)) {
                        mutualBuddies.merge(mutualUser, 1, Integer::sum);
                    }
                });
            });
            mutualBuddies.forEach((mutualUser, count) -> {
                MutualBuddyResponse response = MutualBuddyResponse.builder().mutualCount(count)
                        .suggestedUser(mutualUser.toDtos()).suggestionType(SuggestionType.MUTUAL).build();
                finalSuggestions.add(response);
            });
            sameCategoryUsers.forEach(categoryUser -> {
                if (mutualBuddies.containsKey(categoryUser)) {
                    return;
                }
                MutualBuddyResponse response = MutualBuddyResponse.builder()
                        .mutualCount(3)
                        .suggestedUser(categoryUser.toDtos())
                        .suggestionType(SuggestionType.CATEGORY)
                        .build();
                finalSuggestions.add(response);
            });
            List<User> users= userRepository.findAllByUserIdNot(userId);
            users.forEach(u->{
                if (mutualBuddies.containsKey(u)) {
                    return;
                }
                MutualBuddyResponse response = MutualBuddyResponse.builder()
                        .mutualCount(0)
                        .suggestedUser(u.toDtos())
                        .suggestionType(SuggestionType.RANDOM)
                        .build();
                finalSuggestions.add(response);

            });
            Set<MutualBuddyResponse> toRemove = new HashSet<>();
            for(MutualBuddyResponse suggestion:finalSuggestions){
                if(userBuddyRepository.existsByUser_UserIdAndBuddyUser_UserId(userId,suggestion.getSuggestedUser().getUserId())){
                    toRemove.add(suggestion);
                }
                else if(buddyRequestRepository.existsByRequesterUser_UserIdAndRecipientUser_UserId(userId,suggestion.getSuggestedUser().getUserId())){
                    toRemove.add(suggestion);
                }
            }
            finalSuggestions.removeAll(toRemove);
        }
        return new ArrayList<>(finalSuggestions);
    }

    @Transactional
    @Override
    public UserBuddyResponse createUserBuddy(Long userId, Long requesterUserId) {
        BuddyRequest buddyRequest =buddyRequestRepository.findByRequesterUser_UserIdAndRecipientUser_UserId(requesterUserId,userId);
        buddyRequestRepository.delete(buddyRequest);
        Date dateNow=new Date();
        saveUserBuddy(userId, requesterUserId,dateNow);
        saveUserBuddy(requesterUserId,userId, dateNow);
        return UserBuddyResponse.builder().buddyUser(userRepository.findById(requesterUserId).orElse(new User()).toDtos())
                .userId(userId).buddySince(dateNow).build();
    }

    @Override
    public UserRelationship checkViewUserRelationship(Long userId, Long viewUserId) {
        if(userBuddyRepository.existsByUser_UserIdAndBuddyUser_UserId(userId,viewUserId)){
            return UserRelationship.BUDDIES;
        }
        else if(buddyRequestRepository.existsByRequesterUser_UserIdAndRecipientUser_UserId(userId,viewUserId)){
            return UserRelationship.PENDING;
        }
        else if(buddyRequestRepository.existsByRequesterUser_UserIdAndRecipientUser_UserId(viewUserId,userId)){
            return UserRelationship.RESPOND;
        }
        else{
            return UserRelationship.ADD;
        }
    }

    @Transactional
    @Override
    public Boolean removeUserBuddy(Long userId, Long buddyUserId) {
        removeBuddy(userId, buddyUserId);
        removeBuddy(buddyUserId,userId);
        return true;
    }

    @Override
    public Boolean rejectBuddyRequest(Long requestId) {
        buddyRequestRepository.deleteById(requestId);
        return true;
    }

    @Override
    public List<UserBuddyResponse> getAllUserBuddies(Long userId) {
        List<UserBuddyResponse> userBuddies = new ArrayList<>();
        userBuddyRepository.findAllByUser_UserId(userId).forEach(buddy->{
            userBuddies.add(buddy.toDto());
        });
        return userBuddies;
    }

    private void removeBuddy(Long userId, Long buddyUserId) {
        UserBuddy userBuddy=userBuddyRepository.findByUser_UserIdAndBuddyUser_UserId(userId, buddyUserId);
        userBuddyRepository.delete(userBuddy);
    }

    private void saveUserBuddy(Long userId, Long buddyUserId, Date dateNow) {
        UserBuddy userBuddy=new UserBuddy();
        userBuddy.setBuddyUser(userRepository.findById(buddyUserId).orElse(new User()));
        userBuddy.setUser(userRepository.findById(userId).orElse(new User()));
        userBuddy.setBuddySince(dateNow);
        userBuddyRepository.save(userBuddy);
    }
}
