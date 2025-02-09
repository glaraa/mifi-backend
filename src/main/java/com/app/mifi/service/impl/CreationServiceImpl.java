package com.app.mifi.service.impl;

import com.app.mifi.controller.model.CreationRequest;
import com.app.mifi.controller.model.CreationResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.persist.entity.Comment;
import com.app.mifi.persist.entity.Creation;
import com.app.mifi.persist.entity.User;
import com.app.mifi.persist.entity.UserBuddy;
import com.app.mifi.repository.CommentRepository;
import com.app.mifi.repository.CreationRepository;
import com.app.mifi.repository.UserBuddyRepository;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.CreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CreationServiceImpl implements CreationService {

    private final UserRepository userRepository;
    private final CreationRepository creationRepository;
    private final UserBuddyRepository userBuddyRepository;
    private final CommentRepository commentRepository;

    @Override
    public CreationResponse saveCreation(CreationRequest creationRequest, MultipartFile creationFile) throws IOException {
        Creation creation= new Creation();
        creation.setCreation(creationFile.getBytes());
        creation.setCaption(creationRequest.getCaption());
        creation.setCreatedDate(LocalDate.now());
        creation.setUser(userRepository.findById(creationRequest.getUserId()).orElse(new User()));
        return creationRepository.save(creation).toDto();
    }

    @Override
    public List<CreationResponse> fetchAllCreations(Long userId) {
        List<CreationResponse> creationResponses=new ArrayList<>();
        creationRepository.findAllByUser_UserIdOrderByCreationIdDesc(userId).forEach(creation ->
                creationResponses.add(creation.toDtos()) );
        return creationResponses;
    }

    @Override
    public CreationResponse fetchCreationById(Long creationId) {
        return creationRepository.findById(creationId).orElse(new Creation()).toDto();
    }

    @Override
    public List<CreationResponse> fetchAllFeedCreations(Long userId) {
        List<UserBuddy> userBuddies= userBuddyRepository.findAllByUser_UserId(userId);
        List<CreationResponse> creationResponses=new ArrayList<>();
        if(nonNull(userBuddies)){
            userBuddies.forEach(userBuddy->{
                creationRepository.findAllByUser_UserIdOrderByCreationIdDesc(userBuddy.getBuddyUser().getUserId())
                        .forEach(buddy->creationResponses.add(buddy.toDtos()) );
            });
        }
        creationResponses.stream()
                .sorted(Comparator.comparing(CreationResponse::getCreatedDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        return creationResponses;
    }

    @Override
    public Boolean deleteCreationById(Long userId, Long creationId) {
        creationRepository.findById(creationId).ifPresentOrElse(creation -> {
           if(isNull(creationRepository.findByCreationIdAndUser_UserId(creationId,userId))){
               throw new MiFiException("RUNTIME ERROR","User cant delete creation", HttpStatus.BAD_REQUEST);
           }
           List<Comment> comments =commentRepository.findAllByCreationIdOrderByCommentIdDesc(creationId);
           commentRepository.deleteAll(comments);
           creationRepository.delete(creation);

        },()-> {
            throw new MiFiException("RUNTIME ERROR","Creation Not Found", HttpStatus.NOT_FOUND);
        });
        return true;
    }

    @Override
    public CreationResponse updateCreationById(Long userId, Long creationId, String caption) {
        AtomicReference<CreationResponse> creationResponse = new AtomicReference<>();
        creationRepository.findById(creationId).ifPresentOrElse(creation -> {
            if(isNull(creationRepository.findByCreationIdAndUser_UserId(creationId,userId))){
                throw new MiFiException("RUNTIME ERROR","User cant update creation", HttpStatus.BAD_REQUEST);
            }
            creation.setCaption(caption);
            creation.setUpdatedDate(LocalDate.now());
            creationResponse.set(creationRepository.save(creation).toDto());

        },()-> {
            throw new MiFiException("RUNTIME ERROR","Creation Not Found", HttpStatus.NOT_FOUND);
        });
        return creationResponse.get();
    }
}
