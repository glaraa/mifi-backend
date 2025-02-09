package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentResponse {
    private Long commentId;
    private UserResponse byUser;
    private String commentText;
    private Long creationId;
    private String commentedAt;
}
