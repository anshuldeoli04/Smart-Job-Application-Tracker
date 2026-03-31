package com.example.Job_Application_Tracker.service;

import com.example.Job_Application_Tracker.dto.DashboardStatsResponse;
import com.example.Job_Application_Tracker.model.ApplicationStatus;
import com.example.Job_Application_Tracker.model.User;
import com.example.Job_Application_Tracker.repository.JobApplicationRepository;
import com.example.Job_Application_Tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final JobApplicationRepository jobRepo;
    private final UserRepository userRepo;

    public DashboardStatsResponse getStats(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long total = jobRepo.findByUserId(user.getId()).size();

        long applied = jobRepo.findByUserIdAndStatus(
                user.getId(), ApplicationStatus.APPLIED).size();

        long shortlisted = jobRepo.findByUserIdAndStatus(
                user.getId(), ApplicationStatus.SHORTLISTED).size();

        long interview = jobRepo.findByUserIdAndStatus(
                user.getId(), ApplicationStatus.INTERVIEW).size();

        long offer = jobRepo.findByUserIdAndStatus(
                user.getId(), ApplicationStatus.OFFER).size();

        long rejected = jobRepo.findByUserIdAndStatus(
                user.getId(), ApplicationStatus.REJECTED).size();

        double interviewRate = total > 0 ?
                Math.round((interview * 100.0 / total) * 10.0) / 10.0 : 0;

        double offerRate = total > 0 ?
                Math.round((offer * 100.0 / total) * 10.0) / 10.0 : 0;

        return new DashboardStatsResponse(
                total, applied, shortlisted,
                interview, offer, rejected,
                interviewRate, offerRate);
    }
}
