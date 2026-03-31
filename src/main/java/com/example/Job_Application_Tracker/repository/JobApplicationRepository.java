package com.example.Job_Application_Tracker.repository;

import com.example.Job_Application_Tracker.model.ApplicationStatus;
import com.example.Job_Application_Tracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserId(Long userId);
    List<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status);
    List<JobApplication> findByUserIdAndCompanyNameContainingIgnoreCase(
            Long userId, String companyName);
}
