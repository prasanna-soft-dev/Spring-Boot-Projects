package com.jwt.token.Controller;

import com.jwt.token.Entity.User;
import com.jwt.token.Repository.UserRepository;
import com.jwt.token.Security.JwtUtil;
import com.jwt.token.Service.CustomerUserDetails;
import com.jwt.token.dto.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class RefreshTokenController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            Cookie clearCookie = new Cookie("refreshToken", null);
            clearCookie.setHttpOnly(true);
            clearCookie.setSecure(true);
            clearCookie.setPath("/");
            clearCookie.setMaxAge(0); // Expire immediately
            response.addCookie(clearCookie);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token. Please log in again.");
        }

        String email = jwtUtil.extractEmailFromRefreshToken(refreshToken);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }

        User user = optionalUser.get();

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(user);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, "New access token generated successfully"));
    }

}
