package com.app.mifi.persist.entity;

import com.app.mifi.controller.model.UserResponse;
import com.app.mifi.persist.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.DateTimeException;
import java.util.Base64;
import java.util.Date;

import static java.util.Objects.nonNull;

@Entity
@Data
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "category")
    private String category;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Column(name = "description")
    private String description;

    @Column(name = "password", nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at",nullable = false)
    private Date updatedAt;

    public UserResponse toDto(){
        UserResponse userResponse= UserResponse.builder().build();
        BeanUtils.copyProperties(this,userResponse);
        if(nonNull(this.getProfilePic())) {
            byte[] fileBytes = this.getProfilePic();
            String profilePic = Base64.getEncoder().encodeToString(fileBytes);
            userResponse.setProfilePicBase64(profilePic);
        }
        return userResponse;
    }
}
