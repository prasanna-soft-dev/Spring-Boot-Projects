package com.openai.code.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@ToString

public class CodeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //user code
    @Column(nullable = false,columnDefinition = "TEXT")
    private String code;

    //feedback from the open ai
    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    //language used by the user
    @Column(nullable = false)
    private String lang;

    //time stamp for when the review is created

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
