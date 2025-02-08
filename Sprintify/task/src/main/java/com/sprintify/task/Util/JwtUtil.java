package com.sprintify.task.Util;

import com.sprintify.task.Entity.Role;
import com.sprintify.task.Service.CustomerDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    // Injecting values from application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    // Generate JWT token using email
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)  // Set email as 'sub' claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("roles", String.join(",", roles))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Extract email from the token
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim from the token
    public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Validate the token
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check if the token is valid
    public boolean validateToken(String token, String email) {
        return (email.equals(extractEmail(token)) && !isTokenExpired(token));
    }

    // Interface to resolve claim
    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}
