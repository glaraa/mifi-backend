package com.app.mifi.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Random;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final SendGridEmailService sendGridEmailService;
    private final UserService userService;
    private final CacheService cacheService;

    public boolean validateOtp(String email, String otp) {
        String cachedOtp = cacheService.getOtp(email);
        if (StringUtils.equals(otp, cachedOtp)) {
            cacheService.removeOtp(email);
            return true;
        } else {
            return false;
        }
    }

    public static ResponseEntity<MiFiResponse<String>> getMiFiResponseResponseEntity(boolean isValid) {
        if(isValid) {
            return new ResponseEntity<>(MiFiResponse.<String>builder()
                    .response("Success")
                    .build(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(MiFiResponse.<String>builder()
                    .response("Invalid")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    public boolean forgotPassword(String email) {
        UserResponse user=userService.fetchUserByUserEmail(email);
        if(nonNull(user)) {
            String otp = cacheService.storeOtp(email);
            try {
                sendGridEmailService.sendOtpEmail(email, otp);
                return true;
            } catch (Exception e) {
                throw new MiFiException("RUNTIME ERROR","Couldn't send email",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            throw new MiFiException("RUNTIME ERROR","User not found",HttpStatus.NOT_FOUND);
        }
    }
}
