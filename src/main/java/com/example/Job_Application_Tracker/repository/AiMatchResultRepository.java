package com.example.Job_Application_Tracker.repository;

import com.example.Job_Application_Tracker.model.AiMatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AiMatchResultRepository
        extends JpaRepository<AiMatchResult, Long> {
    List<AiMatchResult> findByJobApplicationId(Long applicationId);
}
