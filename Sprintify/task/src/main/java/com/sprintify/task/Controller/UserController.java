package com.sprintify.task.Controller;

import com.sprintify.task.Entity.AuthRequest;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Service.EmailService;
import com.sprintify.task.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    private EmailService emailService;

    public UserController(UserService userService) {
        this.userService = userService;
    }//manual constructor based injection

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        try {
            String result = userService.registration(user);

            if (result.equals("User Successfully registered")) {
                emailService.sendSimpleEmail(user.getEmail(),
                        "You have successfully registered in Sprintify",
                        "Successful Registration");
                return ResponseEntity.ok("Successfully registered");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsuccessful registration");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Error Occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest loginRequest) {
        try {
            String result = userService.authorize(loginRequest.getEmail(), loginRequest.getPassword());

            if (result.startsWith("Bearer ")) {
                emailService.sendSimpleEmail(
                        loginRequest.getEmail(),
                        "You have logged into your Sprintify account",
                        "Login Email"
                );
                return ResponseEntity.ok("Login successful: " + result);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Unsuccessful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // Accessible by both USER and ADMIN
    public String welcomeUser() {

        return "Welcome User";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")  // Only accessible by ADMIN
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }
}
