package com.demo.vote.Service;
import com.demo.vote.Entity.Voter;


public interface VoterService {
    void registration(Voter voter);
    Voter login(String name,Long voterId);
    void chooseCandidate(Long candidateId);//voting the candidate by choosing by selecting the id if the candidate
}
