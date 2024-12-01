package com.demo.vote.Service.Implementation;

import java.util.Optional;
import com.demo.vote.Entity.Party;
import com.demo.vote.Entity.Voter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.vote.Repository.PartyRepository;
import com.demo.vote.Repository.VoterRepository;
import com.demo.vote.Service.VoterService;

@Service
public class VoterImpl implements VoterService{

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private VoterRepository voterRepository;


    @Override
    public void registration(Voter voter){
        voterRepository.save(voter);
    }


    @Override
    public Voter login(String name,Long voterId){
        Optional<Voter> voter = voterRepository.findByNameAndVoterId(name, voterId);

        
        // Validate the voter's name
        if (voter.isPresent() && voter.get().getName().equalsIgnoreCase(name)) {
            return voter.get(); // Successful login
        }

        return null; // Invalid credentials
    }


    @Override
    public void chooseCandidate(Long candidateId){
        Optional<Party> choosenParty = partyRepository.findById(candidateId);

        if(choosenParty.isPresent()){
            Party party = choosenParty.get();

            party.setVoteReceived(party.getVoteReceived() + 1);
            partyRepository.save(party);
        }
        else{
            throw new RuntimeException("Candidate with the given id is not found ! choose the valid id for the voting proicess" + candidateId);
        }
    }
}
