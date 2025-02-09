package com.app.mifi.persist.entity;

import com.app.mifi.controller.model.UserBuddyResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Entity
@Table(name = "user_buddies")
@Data
public class UserBuddy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buddy_id")
    private Long buddyId;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name = "buddy_user_id", nullable = false)
    private User buddyUser;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "buddy_since")
    private Date buddySince;

    public UserBuddyResponse toDto(){
        UserBuddyResponse userBuddyResponse= UserBuddyResponse.builder().build();
        BeanUtils.copyProperties(this, userBuddyResponse);
        userBuddyResponse.setBuddyUser(this.buddyUser.toDtos());
        return userBuddyResponse;
    }
}
