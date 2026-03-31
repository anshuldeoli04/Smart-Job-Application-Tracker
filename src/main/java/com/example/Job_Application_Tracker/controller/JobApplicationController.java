package com.example.Job_Application_Tracker.controller;

import com.example.Job_Application_Tracker.dto.JobApplicationRequest;
import com.example.Job_Application_Tracker.model.JobApplication;
import com.example.Job_Application_Tracker.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobService;

    @PostMapping
    public ResponseEntity<JobApplication> add(
            Principal principal,
            @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.ok(
                jobService.addApplication(
                        principal.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAll(
            Principal principal,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String company) {
        return ResponseEntity.ok(
                jobService.getAllApplications(
                        principal.getName(), status, company));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> update(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.ok(
                jobService.updateApplication(
                        principal.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            Principal principal,
            @PathVariable Long id) {
        jobService.deleteApplication(principal.getName(), id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
