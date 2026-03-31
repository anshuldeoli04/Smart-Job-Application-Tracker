package com.example.Job_Application_Tracker.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "users")
    @Data
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 100)
        private String name;

        @Column(nullable = false, unique = true, length = 150)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
        }
    }
