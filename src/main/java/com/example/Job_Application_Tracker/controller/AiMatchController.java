package com.example.Job_Application_Tracker.controller;

import com.example.Job_Application_Tracker.dto.AiMatchRequest;
import com.example.Job_Application_Tracker.model.AiMatchResult;
import com.example.Job_Application_Tracker.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiMatchController {

    private final GeminiService geminiService;

    @PostMapping("/match")
    public ResponseEntity<AiMatchResult> match(
            @RequestBody AiMatchRequest request) {
        return ResponseEntity.ok(
                geminiService.analyzeMatch(request));
    }
}
