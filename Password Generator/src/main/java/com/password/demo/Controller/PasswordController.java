package com.password.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.password.demo.Service.*;

@RestController

public class PasswordController {

    @Autowired
    private PasswordGenratorService passwordGenrationService;


    @GetMapping("/generate-password")
    public ResponseEntity<String> generateStrongPassword(@RequestParam int length){
        try {
            // Generate the password
            String generatedPassword = passwordGenrationService.generatePassword(length);

            // Return the generated password with HTTP 201 (Created)
            return new ResponseEntity<>(generatedPassword, HttpStatus.CREATED);

        } catch (IllegalArgumentException ex) {
            // Handle invalid length exceptions gracefully
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            // Catch-all for unexpected errors
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
