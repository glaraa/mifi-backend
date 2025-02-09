package com.app.mifi.service.impl;

import com.app.mifi.controller.model.FeedbackRequest;
import com.app.mifi.persist.entity.FeedBack;
import com.app.mifi.repository.BuddyRequestRepository;
import com.app.mifi.repository.CommentRepository;
import com.app.mifi.repository.CreationRepository;
import com.app.mifi.repository.FeedbackRepository;
import com.app.mifi.repository.UserBuddyRepository;
import com.app.mifi.utils.JwtUtil;
import com.app.mifi.controller.model.LoginRequest;
import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.persist.entity.User;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.UserService;
import com.app.mifi.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.app.mifi.utils.UserUtils.validatePassword;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserBuddyRepository userBuddyRepository;
    private final CreationRepository creationRepository;
    private final FeedbackRepository feedbackRepository;
    private final CommentRepository commentRepository;
    private final BuddyRequestRepository buddyRequestRepository;

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        validateUserRequest(userRequest);
        User user = new User();
        BeanUtils.copyProperties(userRequest,user);
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        return userRepository.save(user).toDto();
    }

    private void validateUserRequest(UserRequest userRequest) {
        User user= userRepository.findByUsername(userRequest.getUsername());
        if(nonNull(user) ){
            throw new MiFiException("Validation","Username already exists", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new MiFiException("Validation","Email already exists", HttpStatus.BAD_REQUEST);
        }
        UserUtils.validateUser(userRequest);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponses=new ArrayList<>();
        userRepository.findAll().forEach(user-> userResponses.add(user.toDto()));
        return userResponses;
    }

    @Override
    public UserResponse getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserResponse dto = user.toDto();
                    return populateCreationBuddyCount(dto);
                })
                .orElse(null); }

    @Override
    public UserResponse updateUserInfo(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(nonNull(user)){
            validateUserRequest(userRequest,userId);
            userRepository.save(UserUtils.mapUser(userRequest,user));
            return populateCreationBuddyCount(user.toDto());
        }
        else{
            throw new MiFiException("NOT FOUND","User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public UserResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if(nonNull(user)) {
            if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                UserResponse userResponse= user.toDto();
                populateCreationBuddyCount(userResponse);
                userResponse.setToken(token);
                return userResponse;
            }
            else {
                throw new MiFiException("Validation","Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            throw new MiFiException("NOT FOUND", "Username not found", HttpStatus.NOT_FOUND);
        }
    }

    private UserResponse populateCreationBuddyCount(UserResponse userResponse) {
        userResponse.setCreationsCount(creationRepository.countByUser_UserId(userResponse.getUserId()));
        userResponse.setBuddiesCount(userBuddyRepository.countByUser_UserId(userResponse.getUserId()));
        userResponse.setBuddyRequestsCount(buddyRequestRepository.countByRecipientUser_UserId(userResponse.getUserId()));
        return userResponse;
    }

    @Override
    public boolean checkUserExists(String username) {
        User user= userRepository.findByUsername(username);
        return nonNull(user);
    }

    @Override
    public UserResponse uploadProfilePicture(Long userId, MultipartFile picture) throws IOException {
        User user= userRepository.findById(userId).orElse(null);
        if(nonNull(user)){
            user.setProfilePic(picture.getBytes());
            return userRepository.save(user).toDto();
        }
        else{
            throw new MiFiException("NOT FOUND","User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public UserResponse fetchUserByUserEmail(String email) {
        AtomicReference<UserResponse> userResponse= new AtomicReference<>(UserResponse.builder().build());
                Optional.ofNullable(userRepository.findByEmail(email)).ifPresent(user -> {
                    userResponse.set(user.toDto());
                });
        return userResponse.get();
    }

    @Override
    public boolean resetUserPassword(String email, String password) {
        User user= userRepository.findByEmail(email);
        validatePassword(password);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void deleteUserAccount(Long userId, String password, FeedbackRequest feedbackRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(nonNull(user)){
            if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
                throw new MiFiException("Validation","Password is invalid", HttpStatus.BAD_REQUEST);
            }
            FeedBack feedBack = new FeedBack();
            feedBack.setUsername(user.getUsername());
            feedBack.setMailId(user.getEmail());
            feedBack.setFeedback(feedbackRequest.getFeedback());
            feedBack.setFeedbackAt(LocalDate.now());
            feedbackRepository.save(feedBack);
            userBuddyRepository.deleteAllByUser(user);
            userBuddyRepository.deleteAllByBuddyUser(user);
            creationRepository.deleteAllByUser(user);
            buddyRequestRepository.deleteAllByRecipientUser(user);
            buddyRequestRepository.deleteAllByRequesterUser(user);
            commentRepository.deleteAllByByUser(user);
            userRepository.delete(user);
        }
    }

    @Override
    public boolean updateUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if(nonNull(user)){
            if(!bCryptPasswordEncoder.matches(oldPassword,user.getPassword())){
                throw new MiFiException("Validation","Old password is invalid", HttpStatus.BAD_REQUEST);
            }
            validatePassword(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        }
        return true;
    }

    private void validateUserRequest(UserRequest userRequest,Long userId) {
        User user= userRepository.findByUsername(userRequest.getUsername());
        if(nonNull(user) && !user.getUserId().equals(userId)){
            throw new MiFiException("Validation","Username already exists", HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(userRequest.getEmail()) && !user.getUserId().equals(userId)){
            throw new MiFiException("Validation","Email already exists", HttpStatus.BAD_REQUEST);
        }
        UserUtils.validateEmail(userRequest.getEmail());
    }
}
