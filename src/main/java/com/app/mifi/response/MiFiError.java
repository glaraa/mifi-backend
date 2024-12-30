package com.app.mifi.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MiFiError {
    private String errorCause;
    private String errorMessage;
    private String errorType;
}
