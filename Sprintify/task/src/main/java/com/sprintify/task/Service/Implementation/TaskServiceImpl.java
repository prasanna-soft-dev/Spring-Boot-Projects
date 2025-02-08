package com.sprintify.task.Service.Implementation;

import com.sprintify.task.Entity.TaskEntity;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Repository.TaskRepository;
import com.sprintify.task.Repository.UserRepository;
import com.sprintify.task.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addTask(TaskEntity task, String email) {
        try {
            // Retrieve user from database using email
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set the user ID before saving
            task.setUserId(user.getId());

            taskRepository.save(task);
            return "Task saved successfully";
        } catch (Exception e) {
            return "An error occurred while saving the task: " + e.getMessage();
        }
    }

    @Transactional
    @Override
    public String deleteTask(Long taskId, String email) {
        try {
            TaskEntity task = taskRepository.findById(taskId).orElse(null);
            if (task == null) {
                return "No such task exists";
            }

            // Fetch user and validate ownership
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getId().equals(task.getUserId())) {
                return "You are not authorized to delete this task";
            }

            taskRepository.deleteById(taskId);
            return "Successfully deleted";
        } catch (Exception e) {
            return "An error occurred while deleting the task: " + e.getMessage();
        }
    }

    @Transactional
    @Override
    public String updateTask(TaskEntity updatedTask, String email) {
        try {
            if (updatedTask == null || updatedTask.getId() == null) {
                return "Invalid task update request: Task ID cannot be null";
            }

            TaskEntity existingTask = taskRepository.findById(updatedTask.getId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long userId = user.getId(); // Fetch ID to prevent LazyInitialization issues
            if (!existingTask.getUserId().equals(userId)) {
                return "You are not authorized to update this task";
            }

            // Update only allowed fields
            if (updatedTask.getDescription() != null) {
                existingTask.setDescription(updatedTask.getDescription());
            }
            if (updatedTask.getReminderTime() != null) {
                existingTask.setReminderTime(updatedTask.getReminderTime());
            }
            if (updatedTask.getStatus() != null) {
                existingTask.setStatus(updatedTask.getStatus());
            }

            taskRepository.saveAndFlush(existingTask); // Ensure immediate save
            return "Task updated successfully";
        } catch (NoSuchElementException e) {
            return "Task or user not found";
        } catch (Exception e) {
            e.printStackTrace(); // Print full error for debugging
            return "An unexpected error occurred while updating the task: " + e.getMessage();
        }
    }


    @Override
    public List<TaskEntity> getTaskByUserId(Long userId,String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getId().equals(userId)) {
            throw new RuntimeException("403 Forbidden: You are not authorized to view these tasks");
        }

        return taskRepository.findByUserId(userId);
    }
}
