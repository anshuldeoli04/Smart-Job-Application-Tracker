package com.example.Job_Application_Tracker.controller;

import com.example.Job_Application_Tracker.dto.DashboardStatsResponse;
import com.example.Job_Application_Tracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardStatsResponse> getStats(
            Principal principal) {
        return ResponseEntity.ok(
                dashboardService.getStats(principal.getName()));
    }
}
