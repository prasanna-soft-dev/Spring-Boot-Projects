package com.demo.vote.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.vote.Service.AdminService;

@RestController
@RequestMapping("/election-result")
public class resultController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<String> winnerIs(){
        String winner = adminService.calculateWinner();

        return new ResponseEntity<String>(winner, HttpStatus.OK);
    }

}
