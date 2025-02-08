package com.sprintify.task.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime reminderTime;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Long userId; // To fetch email for reminders

    // Constructors
    public TaskEntity() {}

    public TaskEntity(String description, LocalDateTime reminderTime, TaskStatus status, Long userId) {
        this.description = description;
        this.reminderTime = reminderTime;
        this.status = status;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalDateTime reminderTime) { this.reminderTime = reminderTime; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
