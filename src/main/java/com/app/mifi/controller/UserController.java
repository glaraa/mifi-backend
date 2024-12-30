package com.app.mifi.controller;


import com.app.mifi.controller.model.Requesters;
import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/")
    public ResponseEntity<MiFiResponse<UserResponse>> createUser(@RequestHeader(name = "RequestBy") Requesters requestBy, @RequestBody UserRequest user) {
            UserResponse savedUser = userService.saveUser(user);
            return new ResponseEntity<>(MiFiResponse.<UserResponse>builder().response(savedUser).build(), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<MiFiResponse<List<UserResponse>>> getUsers(@RequestHeader Requesters requestBy) {
        List<UserResponse> users = userService.getAllUsers();
        return new ResponseEntity<>(MiFiResponse.<List<UserResponse>>builder().response(users).build(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MiFiResponse<UserResponse>> getUserById(@RequestHeader(name = "RequestBy") Requesters requestBy, @PathVariable Long userId) {
        log.info("Fetching user info for user [{}]  requester [{}]",userId,requestBy);
        UserResponse user = userService.getUserInfo(userId);
        return new ResponseEntity<>(MiFiResponse.<UserResponse>builder().response(user).build(), HttpStatus.OK);
    }
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserById(@RequestHeader(name = "RequestBy") Requesters requestBy, @PathVariable Long userId,@RequestBody UserRequest userRequest) {
        log.info("Updating user info for user [{}]  requester [{}]",userId,requestBy);
        UserResponse createdUser = userService.updateUserInfo(userId,userRequest);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }

    @GetMapping("/unique/{username}")
    public ResponseEntity<MiFiResponse<Boolean>> checkIfUserIsUnique(@RequestHeader(name = "RequestBy") Requesters requestBy, @PathVariable String username) {
        log.info("checking user unique [{}] for requester [{}]",username,requestBy);
        boolean userExists = userService.checkUserExists(username);
        return new ResponseEntity<>(MiFiResponse.<Boolean>builder().response(!userExists).build(), HttpStatus.OK);
    }
    @PostMapping(value = "/profile-pic/{userId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<MiFiResponse<UserResponse>> uploadProfilePicture(@RequestHeader(name = "RequestBy") String requestBy,
                           @PathVariable Long userId, @RequestPart("picture") MultipartFile picture) throws IOException {
        log.info("Uploading profile pic for user [{}]  requester [{}]",userId,requestBy);
        UserResponse user= userService.uploadProfilePicture(userId,picture);
        return ResponseEntity.status(HttpStatus.OK).body(MiFiResponse.<UserResponse>builder().response(user).build());
    }
}
