package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class BuddyRequestResponse {
    private Integer requestId;
    private UserResponse recipientUser;
    private UserResponse requesterUser;
    private Boolean hasSeen;
    private Date sentAt;
    private List<UserResponse> mutualBuddies;
}
