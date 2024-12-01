package com.demo.vote.API;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.vote.Service.VoterService;

@RestController
@RequestMapping("/Voting")
public class VoterController {

    @Autowired
    private VoterService voterService;

    @PostMapping("/vote")
    public ResponseEntity<String> castVote(@RequestParam Long candidateId){
        try {
            voterService.chooseCandidate(candidateId);

            return new ResponseEntity<String>("Your Votee Casted Successfully", HttpStatus.ACCEPTED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Candidate not found", HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid candidate ID", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
