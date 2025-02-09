package com.app.mifi.controller;

import com.app.mifi.controller.model.LoginRequest;
import com.app.mifi.controller.model.Requesters;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.UserService;
import com.app.mifi.service.impl.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.app.mifi.constant.Constant.REQUEST_BY;
import static com.app.mifi.service.impl.OtpService.getMiFiResponseResponseEntity;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;
    private final OtpService otpService;

    @PostMapping("/login")
    public ResponseEntity<MiFiResponse<UserResponse>> userLogin (@RequestHeader(name =  REQUEST_BY) Requesters requestBy, @RequestBody LoginRequest user) {
        log.info("Login requested by [{}] with user [{}]",requestBy,user.getUsername());
        UserResponse createdUser = userService.loginUser(user);
        return new ResponseEntity<>(MiFiResponse.<UserResponse>builder().response(createdUser).build(), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MiFiResponse<String>> forgotPassword(
            @RequestHeader(name = REQUEST_BY) Requesters requestBy,
            @RequestParam String email) {
        log.info("Forgot password requested by [{}] for user [{}]", requestBy, email);
        boolean mailSent= otpService.forgotPassword(email);
        return getMiFiResponseResponseEntity(mailSent);
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<MiFiResponse<String>> validateOtp(
            @RequestHeader(name = REQUEST_BY) Requesters requestBy,
            @RequestParam String email,
            @RequestParam String otp) {

        log.info("OTP validation requested by [{}] for user [{}]", requestBy, email);
        boolean isValid =otpService.validateOtp(email,otp);
        return getMiFiResponseResponseEntity(isValid);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MiFiResponse<String>> resetPassword(
            @RequestHeader(name = REQUEST_BY) Requesters requestBy,
            @RequestParam String email,
            @RequestParam String password) {

        log.info("Reset password requested by [{}] for user [{}]", requestBy, email);
        boolean response =userService.resetUserPassword(email,password);
        return new ResponseEntity<>(MiFiResponse.<String>builder().response("Success").build(), HttpStatus.OK);
    }
    @PostMapping("/update-password")
    public ResponseEntity<MiFiResponse<String>> updatePassword(
            @RequestHeader(name = REQUEST_BY) Requesters requestBy,
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        log.info("Update password requested by [{}] for user [{}]", requestBy, userId);
        boolean response =userService.updateUserPassword(userId,oldPassword,newPassword);
        return new ResponseEntity<>(MiFiResponse.<String>builder().response("Success").build(), HttpStatus.OK);
    }

}
