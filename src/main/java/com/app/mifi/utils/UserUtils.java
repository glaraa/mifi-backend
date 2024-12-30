package com.app.mifi.utils;

import com.app.mifi.controller.model.UserRequest;
import com.app.mifi.exception.MiFiException;
import com.app.mifi.persist.entity.User;
import org.springframework.http.HttpStatus;

public class UserUtils {
    public static void validateUser(UserRequest userRequest) {
        validateEmail(userRequest);
        validatePassword(userRequest);
    }

    private static void validatePassword(UserRequest userRequest) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        String password = userRequest.getPassword();
        if (password == null || !password.matches(passwordRegex)) {
            throw new MiFiException("Validation","Password must contain at least 8 characters, including one uppercase letter, one lowercase letter, one digit, and one special character.", HttpStatus.BAD_REQUEST);
        }
    }

    public static void validateEmail(UserRequest userRequest) {
        String email = userRequest.getEmail();
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (email == null || !email.matches(emailRegex)) {
            throw new MiFiException("Validation","Invalid Email", HttpStatus.BAD_REQUEST);
        }
    }

    public static User mapUser(UserRequest userRequest, User user) {
        user.setCategory(userRequest.getCategory());
        user.setDescription(userRequest.getDescription());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        return user;
    }
}