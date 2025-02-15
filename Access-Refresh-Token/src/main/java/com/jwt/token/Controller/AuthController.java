package com.jwt.token.Controller;

import com.jwt.token.Entity.User;
import com.jwt.token.Security.JwtUtil;
import com.jwt.token.Service.CustomerUserDetails;
import com.jwt.token.Service.UserService;
import com.jwt.token.dto.AuthRequest;
import com.jwt.token.dto.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody User user){
        if(userService.register(user)){
            return new ResponseEntity<>("Registration Successfully completed", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Invalid credential or User already exits", HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser(); // Fetch actual user entity

            // Generate Tokens
            String access = jwtUtil.generateAccessToken(user);
            String refresh = jwtUtil.generateRefreshToken(user);

            // Set Refresh Token in HttpOnly Cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", refresh);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            refreshTokenCookie.setAttribute("SameSite", "Strict");

            response.addCookie(refreshTokenCookie);

            AuthResponse authResponse = new AuthResponse(access, "Refresh token stored in cookies");

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Invalid email or password!"));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token format");
        }

        String token = authHeader.replace("Bearer ", "").trim(); // Extract JWT only

        if (jwtUtil.validateAccessToken(token)) {
            return ResponseEntity.ok("Token is valid! You are authenticated.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }

}
