package com.example.Job_Application_Tracker.dto;

import com.example.Job_Application_Tracker.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JobApplicationRequest {

    @NotBlank(message = "Company name required")
    private String companyName;

    @NotBlank(message = "Role required")
    private String role;

    private String jobUrl;
    private String salaryRange;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private String jobDescription;
}
