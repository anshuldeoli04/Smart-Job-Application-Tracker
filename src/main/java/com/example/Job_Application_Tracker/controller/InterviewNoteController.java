package com.example.Job_Application_Tracker.controller;

import com.example.Job_Application_Tracker.dto.InterviewNoteRequest;
import com.example.Job_Application_Tracker.model.InterviewNote;
import com.example.Job_Application_Tracker.service.InterviewNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs/{applicationId}/notes")
@RequiredArgsConstructor
public class InterviewNoteController {

    private final InterviewNoteService noteService;

    @PostMapping
    public ResponseEntity<InterviewNote> addNote(
            @PathVariable Long applicationId,
            @RequestBody InterviewNoteRequest request) {
        return ResponseEntity.ok(
                noteService.addNote(applicationId, request));
    }

    @GetMapping
    public ResponseEntity<List<InterviewNote>> getNotes(
            @PathVariable Long applicationId) {
        return ResponseEntity.ok(
                noteService.getNotes(applicationId));
    }
}
