package com.demo.vote.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.vote.Service.AdminService;
import com.demo.vote.Service.VoterService;
import com.demo.vote.Entity.Voter;
import com.demo.vote.Entity.Admin;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private VoterService voterService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/voter")
    public ResponseEntity<String> logging(@RequestParam String name, @RequestParam Long voterId) {
        try {
            Voter voter = voterService.login(name, voterId);
            if(voter != null)
            {
                return ResponseEntity.ok("Login Successful! You can cast your vote.");
            }else{
                return new ResponseEntity<String>("Invalid credentials", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<String> logginAdmin(@RequestParam String adminName, @RequestParam String  password){
        try {
            Admin admin = adminService.loginAdmin(adminName, password);

            if(admin != null){
                return ResponseEntity.ok("Login Successfull Admin! you can continue your work");           
            }
            else{
                return new ResponseEntity<String>("Invalid credentials", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
