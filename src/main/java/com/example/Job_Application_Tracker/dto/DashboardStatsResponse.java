package com.example.Job_Application_Tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalApplications;
    private long applied;
    private long shortlisted;
    private long interview;
    private long offer;
    private long rejected;
    private double interviewRate;
    private double offerRate;
}
