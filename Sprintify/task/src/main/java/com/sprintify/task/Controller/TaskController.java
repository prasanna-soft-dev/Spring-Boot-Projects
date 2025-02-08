package com.sprintify.task.Controller;

import com.sprintify.task.Entity.TaskEntity;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Repository.UserRepository;
import com.sprintify.task.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskEntity task,Principal principal)
    {
        String username = principal.getName();
        String result = taskService.addTask(task,username);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId,Principal principal) {
        String email = principal.getName();
        String result = taskService.deleteTask(taskId,email);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateTask(@RequestBody TaskEntity task,Principal principal) {
        String email = principal.getName();
        String result = taskService.updateTask(task,email);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId, Principal principal) {
        String email = principal.getName();
        try {
            List<TaskEntity> tasks = taskService.getTaskByUserId(userId, email);
            return ResponseEntity.ok(tasks);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        }
    }


    @GetMapping("/test")
    public ResponseEntity<String> testPrincipal(Principal principal) {
        return ResponseEntity.ok("Authenticated user: " + principal.getName());
    }

}
