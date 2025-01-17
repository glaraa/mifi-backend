package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class UserBuddyResponse {
    private Long buddyUserId;
    private Long userId;
    private Date buddySince;
}
