package com.app.mifi.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class MiFiResponse<T> {
    private HttpStatus status;
    private T response;
    private List<MiFiError> errors;
}
