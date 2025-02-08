package com.sprintify.task.Service.Implementation;

import com.sprintify.task.Entity.Role;
import com.sprintify.task.Entity.UserEntity;
import com.sprintify.task.Repository.UserRepository;
import com.sprintify.task.Service.CustomerDetailsService;
import com.sprintify.task.Service.UserService;
import com.sprintify.task.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    //constructor based dependency injection
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,JwtUtil jwtUtil, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

    }

    @Override
    public String registration(UserEntity user){
        try {
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                return "User with same email already exits";
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "User Successfully registered";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while registering the user" + e.getMessage();
        }
    }

    @Override
    public String authorize(String email, String password)throws UsernameNotFoundException {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch the user from the database
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Extract user roles
            List<String> roles = user.getRoles().stream()
                    .map(Enum::name) // Convert enum to string
                    .collect(Collectors.toList());

            // Generate token with roles
            String token = jwtUtil.generateToken(email, roles);

            return "Bearer " + token;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during authorization " + e.getMessage();
        }
    }
}
