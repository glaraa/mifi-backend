package com.app.mifi.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class MiFiException extends RuntimeException{
    private String errorType;
    private String errorMessage;
    private HttpStatus status;
}
