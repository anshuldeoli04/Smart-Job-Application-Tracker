package com.example.Job_Application_Tracker.repository;

import com.example.Job_Application_Tracker.model.InterviewNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewNoteRepository
        extends JpaRepository<InterviewNote, Long> {
    List<InterviewNote> findByJobApplicationId(Long applicationId);
}
