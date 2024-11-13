package com.email.emaildemo.Entity;

public class User {
    private String FirstName;
    private String LastName;
    private String email;


    //SETTER AND GETTER

    public void setFirstName(String FirstName){
        this.FirstName = FirstName;
    }

    public void setLastName(String LastName){
        this.LastName = LastName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return FirstName + LastName;
    }

    public String getEmail(){
        return email;
    }
}
