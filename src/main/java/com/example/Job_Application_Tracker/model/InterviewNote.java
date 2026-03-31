package com.example.Job_Application_Tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_notes")
@Data
public class InterviewNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication jobApplication;

    @Column(name = "round_name")
    private String roundName;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
