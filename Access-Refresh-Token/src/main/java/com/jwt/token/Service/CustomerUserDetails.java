package com.jwt.token.Service;

import com.jwt.token.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomerUserDetails implements UserDetails {

    private final User user;

    public CustomerUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // Modify if your User has roles
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // Fix: Return actual password
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // Fix: Return email (assuming email is used for login)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this.user;
    }

}
