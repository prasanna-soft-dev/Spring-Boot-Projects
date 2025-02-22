package com.devsync.DevSync.Util;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // ✅ Generate JWT Token with Claims (Email & Role)
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // ✅ Email as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // ✅ Use configured expiration time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // ✅ Extract Email (Subject) from JWT
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract Role from JWT Claims
    public String extractRole(String token) {
        return (String) extractAllClaims(token).get("role");
    }

    // ✅ Extract Specific Claim from Token
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // ✅ Extract All Claims from Token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Validate JWT Token
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            log.info("Token validated successfully for email: {}", claims.getSubject());
            log.info("Token Expiration: {}", claims.getExpiration());
            log.info("Current Time: {}", new Date());

            return !claims.getExpiration().before(new Date()); // ✅ Token is valid if not expired
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
        }
        return false;
    }
}
