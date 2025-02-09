package com.app.mifi.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

@Data
@Builder
public class CreationRequest {
    private String caption;
    @NotNull
    private Long userId;
    private MultipartFile creation;
}