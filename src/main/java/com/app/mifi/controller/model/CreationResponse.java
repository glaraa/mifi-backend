package com.app.mifi.controller.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class CreationResponse {
    private Long creationId;
    private String caption;
    private String creationBase64;
    private Date createdDate;
    private Date updatedDate;
    private Long userId;
}