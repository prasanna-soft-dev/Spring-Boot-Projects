package com.sprintify.task.Entity;


import jakarta.persistence.Table;
import lombok.*;


public class AuthRequest {
    private String email;
    private String password;


    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
