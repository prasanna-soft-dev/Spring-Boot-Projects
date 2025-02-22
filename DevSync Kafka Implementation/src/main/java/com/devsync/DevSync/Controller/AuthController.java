package com.devsync.DevSync.Controller;

import com.devsync.DevSync.Dao.AuthRequest;
import com.devsync.DevSync.Entity.UserEntity;
import com.devsync.DevSync.Repository.UserRepository;
import com.devsync.DevSync.Service.UserService;
import com.devsync.DevSync.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user){
        try {
            boolean success = userService.registration(user);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("User registered successfully: " + user.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User already exists with email: " + user.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/loginToken")
    public ResponseEntity<String> loginTokenGeneration(@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));


        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        Optional<UserEntity> user = userRepository.findByEmail(authRequest.getEmail());
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        String role = user.get().getRole().name();
        String jwtToken = jwtUtil.generateToken(authRequest.getEmail(),role);


        return ResponseEntity.ok("Jwt Token - " + jwtToken);
    }


}
