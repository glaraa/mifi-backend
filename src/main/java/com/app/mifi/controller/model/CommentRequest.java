package com.app.mifi.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CommentRequest {
    private Long byUserId;
    private String commentText;
    private Long creationId;
}
