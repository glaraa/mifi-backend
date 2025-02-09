package com.app.mifi.controller.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FeedbackResponse {
    private Long feedbackId;
    private String username;
    private String mailId;
    private String feedback;
    private LocalDate feedbackAt;
}
