package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    private Long byUserId;
    private String commentText;
    private Long creationId;
}
