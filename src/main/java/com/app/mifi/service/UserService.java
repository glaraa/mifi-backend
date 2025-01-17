package com.app.mifi.service;

import com.app.mifi.controller.model.LoginRequest;
import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.controller.model.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    UserResponse saveUser(UserRequest user);

    List<UserResponse> getAllUsers();

    UserResponse getUserInfo(Long userId);

    UserResponse updateUserInfo(Long userId, UserRequest userRequest);

    UserResponse loginUser(LoginRequest user);

    boolean checkUserExists(String username);

    UserResponse uploadProfilePicture(Long userId, MultipartFile picture) throws IOException;
}
