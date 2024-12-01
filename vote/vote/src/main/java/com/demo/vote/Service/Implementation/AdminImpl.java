package com.demo.vote.Service.Implementation;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.vote.Repository.AdminRepository;
import com.demo.vote.Repository.PartyRepository;
import com.demo.vote.Service.AdminService;
import com.demo.vote.Entity.Admin;
import com.demo.vote.Entity.Party;

@Service
public class AdminImpl implements AdminService {

    @Autowired 
    private AdminRepository adminRepository;

    @Autowired 
    private PartyRepository partyRepository;


    @Override
    public void registrationAdmin(Admin admin){
        adminRepository.save(admin);
    }


    @Override
    public Admin loginAdmin(String adminName, String password){

        try {
            Optional<Admin> optionalAdmin = adminRepository.findByAdminNameAndPassword(adminName, password);

            if (optionalAdmin.isPresent()) {
                Admin admin = optionalAdmin.get();
                if (admin.getPassword().equals(password)) {
                    return admin;
                }
            }
            return null;  // Or you can return a custom response indicating invalid login

        } catch (Exception e) {
            // Handle specific exceptions or log the exception for debugging
            throw new RuntimeException("Login failed", e);
        }
    }

    @Override
    public String calculateWinner(){
        int maxVote = Integer.MIN_VALUE;

        String winner = "";

        List<Party> parties = partyRepository.findAll();

        for(Party party : parties){
            int votes = party.getVoteReceived();

            if(votes > maxVote){
                maxVote = votes;
                winner = party.getName();
            }
        }

        return winner;
    }
}

