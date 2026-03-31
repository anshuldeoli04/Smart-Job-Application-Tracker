package com.example.Job_Application_Tracker.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InterviewNoteRequest {
    private String roundName;
    private LocalDate interviewDate;
    private String feedback;
}
