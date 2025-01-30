package com.jwt.token.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserInfo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String roles;

    // Default constructor (required by JPA)
    public UserInfo() {
    }

    // Parameterized constructor
    public UserInfo(int id, String name, String email, String password, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getter and Setter for `id`
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for `name`
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for `email`
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for `password`
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for `roles`
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    // Optional: Override `toString()` for debugging purposes
    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }

    // Optional: Override `equals()` and `hashCode()` if required
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id &&
                name.equals(userInfo.name) &&
                email.equals(userInfo.email) &&
                password.equals(userInfo.password) &&
                roles.equals(userInfo.roles);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, email, password, roles);
    }
}

