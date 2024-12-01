package com.demo.vote.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.vote.Service.AdminService;
import com.demo.vote.Service.VoterService;
import com.demo.vote.Entity.Voter;
import com.demo.vote.Entity.Admin;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private VoterService voterService;

    @Autowired 
    private AdminService adminService;

    @PostMapping("/voter")
    public ResponseEntity<String> register(@RequestBody Voter voter){
        try {
            voterService.registration(voter);

            return new ResponseEntity<String>("Voter info successfully registered", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid data", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody Admin admin){
        try {
            adminService.registrationAdmin(admin);

            return new ResponseEntity<String>("Admin info successfully registered", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>( "Invalid data", HttpStatus.BAD_REQUEST);
        }
    }

    

}
