package com.jwt.token.Entity;




public class AuthRequest {

    private String username;
    private String password;

    // Default constructor
    public AuthRequest() {
    }

    // Parameterized constructor
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for `username`
    public String getUsername() {
        return username;
    }

    // Setter for `username`
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for `password`
    public String getPassword() {
        return password;
    }

    // Setter for `password`
    public void setPassword(String password) {
        this.password = password;
    }

    // Optional: Override `toString()` for debugging/logging purposes
    @Override
    public String toString() {
        return "AuthRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

