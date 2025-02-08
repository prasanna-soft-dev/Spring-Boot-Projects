package com.sprintify.task.Service;


import com.sprintify.task.Entity.TaskEntity;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Repository.TaskRepository;
import com.sprintify.task.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository; // To get user email

    @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void sendTaskReminders() {
        System.out.println("Executing ReminderService at: " + LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusMinutes(10); // Tasks that need reminders within 10 minutes

        List<TaskEntity> upcomingTasks = taskRepository.findByReminderTimeBetween(now, soon);

        if (upcomingTasks.isEmpty()) {
            System.out.println("No upcoming tasks found.");
        }

        for (TaskEntity task : upcomingTasks) {
            sendReminderEmail(task);
        }
    }

    private void sendReminderEmail(TaskEntity task) {
        // Fetch user email from the user table using userId
        try {
            String userEmail = userRepository.findById(task.getUserId())
                    .map(UserEntity::getEmail)
                    .orElse(null);

            if (userEmail != null) {
                String subject = "Task Reminder: " + task.getDescription();
                String body = "Reminder: Your task \"" + task.getDescription() +
                        "\" is scheduled at " + task.getReminderTime();

                emailService.sendSimpleEmail(userEmail, body, subject);
                System.out.println("Reminder sent to " + userEmail);
            } else {
                System.out.println("No email found for user ID: " + task.getUserId());
            }
        } catch (Exception e) {
            System.out.println("Error sending reminder email: " + e.getMessage());
        }
    }
}
