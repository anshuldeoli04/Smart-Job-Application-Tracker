package com.example.Job_Application_Tracker.dto;

import lombok.Data;

@Data
public class AiMatchRequest {
    private Long applicationId;
    private String resumeText;
}
