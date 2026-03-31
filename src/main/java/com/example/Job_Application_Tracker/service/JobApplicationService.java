package com.example.Job_Application_Tracker.service;

import com.example.Job_Application_Tracker.dto.JobApplicationRequest;
import com.example.Job_Application_Tracker.model.ApplicationStatus;
import com.example.Job_Application_Tracker.model.JobApplication;
import com.example.Job_Application_Tracker.model.User;
import com.example.Job_Application_Tracker.repository.JobApplicationRepository;
import com.example.Job_Application_Tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepo;

    private User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public JobApplication addApplication(String email,
                                         JobApplicationRequest req) {
        User user = getUser(email);
        JobApplication job = new JobApplication();
        job.setUser(user);
        job.setCompanyName(req.getCompanyName());
        job.setRole(req.getRole());
        job.setJobUrl(req.getJobUrl());
        job.setSalaryRange(req.getSalaryRange());
        job.setStatus(req.getStatus() != null ?
                req.getStatus() : ApplicationStatus.APPLIED);
        job.setAppliedDate(req.getAppliedDate());
        job.setJobDescription(req.getJobDescription());
        return jobRepo.save(job);
    }

    public List<JobApplication> getAllApplications(String email,
                                                   String status,
                                                   String company) {
        User user = getUser(email);
        if (status != null) {
            return jobRepo.findByUserIdAndStatus(
                    user.getId(),
                    ApplicationStatus.valueOf(status.toUpperCase()));
        }
        if (company != null) {
            return jobRepo
                    .findByUserIdAndCompanyNameContainingIgnoreCase(
                            user.getId(), company);
        }
        return jobRepo.findByUserId(user.getId());
    }

    public JobApplication updateApplication(String email, Long id,
                                            JobApplicationRequest req) {
        JobApplication job = jobRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));
        job.setCompanyName(req.getCompanyName());
        job.setRole(req.getRole());
        job.setJobUrl(req.getJobUrl());
        job.setSalaryRange(req.getSalaryRange());
        if (req.getStatus() != null) job.setStatus(req.getStatus());
        job.setAppliedDate(req.getAppliedDate());
        job.setJobDescription(req.getJobDescription());
        return jobRepo.save(job);
    }

    public void deleteApplication(String email, Long id) {
        JobApplication job = jobRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Application not found"));
        jobRepo.delete(job);
    }
}
