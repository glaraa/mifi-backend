package com.app.mifi.service.impl;

import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.response.MiFiResponse;
import com.app.mifi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CacheService {

    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @CachePut(value = "otpCache", key = "#email")
    public String storeOtp(String email) {
        return generateOtp();
    }

    @Cacheable(value = "otpCache", key = "#email")
    public String getOtp(String email) {
        return null;
    }

    @CacheEvict(value = "otpCache", key = "#email")
    public void removeOtp(String email) {
    }
}
