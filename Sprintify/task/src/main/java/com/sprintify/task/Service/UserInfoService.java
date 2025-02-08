package com.sprintify.task.Service;

import com.sprintify.task.Entity.Role;
import com.sprintify.task.Entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class UserInfoService implements UserDetails {
    private final String username;
    private final String password;
    private final String email;

    private final List<GrantedAuthority> authorities;

    public UserInfoService(UserEntity user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.authorities = mapRolesToAuthorities(user.getRoles());
    }

    public List<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming account expiration is not used; you can add logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Assuming account is not locked by default; add logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Assuming credentials do not expire; add logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Assuming the user is enabled; add logic if needed
    }




}
