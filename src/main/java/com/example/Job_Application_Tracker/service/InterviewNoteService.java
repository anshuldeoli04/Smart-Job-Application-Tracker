package com.example.Job_Application_Tracker.service;

import com.example.Job_Application_Tracker.dto.InterviewNoteRequest;
import com.example.Job_Application_Tracker.model.InterviewNote;
import com.example.Job_Application_Tracker.model.JobApplication;
import com.example.Job_Application_Tracker.repository.InterviewNoteRepository;
import com.example.Job_Application_Tracker.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class InterviewNoteService {

        private final InterviewNoteRepository noteRepo;
        private final JobApplicationRepository jobRepo;

        public InterviewNote addNote(Long applicationId,
                                     InterviewNoteRequest req) {
            JobApplication job = jobRepo.findById(applicationId)
                    .orElseThrow(() ->
                            new RuntimeException("Application not found"));

            InterviewNote note = new InterviewNote();
            note.setJobApplication(job);
            note.setRoundName(req.getRoundName());
            note.setInterviewDate(req.getInterviewDate());
            note.setFeedback(req.getFeedback());

            return noteRepo.save(note);
        }

        public List<InterviewNote> getNotes(Long applicationId) {
            return noteRepo.findByJobApplicationId(applicationId);
        }

}
