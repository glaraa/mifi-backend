package com.app.mifi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cache")
public class CacheController {

    private final CacheManager cacheManager;

    @GetMapping("/otp/{email}")
    public String getCachedOtp(@PathVariable String email) {
        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            return cache.get(email, String.class);
        }
        return "No OTP found for this email";
    }
}
