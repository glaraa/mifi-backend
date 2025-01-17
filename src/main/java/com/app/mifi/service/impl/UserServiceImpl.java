package com.app.mifi.service.impl;

import com.app.mifi.utils.JwtUtil;
import com.app.mifi.controller.model.LoginRequest;
import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.persist.entity.User;
import com.app.mifi.repository.UserRepository;
import com.app.mifi.service.UserService;
import com.app.mifi.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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
        return userRepository.findById(userId).map(User::toDto).orElse(null);    }

    @Override
    public UserResponse updateUserInfo(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(nonNull(user)){
            validateUserRequest(userRequest,userId);
            userRepository.save(UserUtils.mapUser(userRequest,user));
            return user.toDto();
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
                userResponse.setToken(token);
                return userResponse;
            }
            else {
                throw new MiFiException("Validation","Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            throw new MiFiException("NOT FOUND", "Username", HttpStatus.NOT_FOUND);
        }
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

    private void validateUserRequest(UserRequest userRequest,Long userId) {
        User user= userRepository.findByUsername(userRequest.getUsername());
        if(nonNull(user) && !user.getUserId().equals(userId)){
            throw new MiFiException("Validation","Username already exists", HttpStatus.BAD_REQUEST);
        }
        UserUtils.validateEmail(userRequest);
    }
}
