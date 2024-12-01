package com.demo.vote.Service;


import com.demo.vote.Entity.Admin;

public interface AdminService {

    void registrationAdmin(Admin admin);
    Admin loginAdmin(String adminName, String password);
    String calculateWinner();
    
} 
