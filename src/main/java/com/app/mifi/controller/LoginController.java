package com.app.mifi.controller;

import com.app.mifi.controller.model.LoginRequest;
import com.app.mifi.controller.model.Requesters;
import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<MiFiResponse<UserResponse>> userLogin (@RequestHeader(name = "RequestBy") Requesters requestBy, @RequestBody LoginRequest user) {
        log.info("Login requested by [{}] with user [{}]",requestBy,user.getUsername());
        UserResponse createdUser = userService.loginUser(user);
        return new ResponseEntity<>(MiFiResponse.<UserResponse>builder().response(createdUser).build(), HttpStatus.OK);
    }
}
