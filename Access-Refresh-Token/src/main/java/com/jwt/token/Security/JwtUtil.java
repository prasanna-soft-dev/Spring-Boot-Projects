package com.jwt.token.Security;

import com.jwt.token.Entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.access-secret}")
    private String accessSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // ✅ Convert secret to a valid 256-bit key
    private Key getSecretKey(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    // ✅ Generate Access Token
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(getSecretKey(accessSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Generate Refresh Token
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(getSecretKey(refreshSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract email from Access Token
    public String extractEmailFromAccessToken(String token) {
        return extractClaim(token, accessSecret, Claims::getSubject);
    }

    // ✅ Extract email from Refresh Token
    public String extractEmailFromRefreshToken(String token) {
        return extractClaim(token, refreshSecret, Claims::getSubject);
    }

    // ✅ Extract claim from token
    public <T> T extractClaim(String token, String secret, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(getSecretKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Validate Access Token
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = extractAllClaims(token, accessSecret);
            log.info("Token validated successfully for email: {}", claims.getSubject());
            log.info("Token Expiration: {}", claims.getExpiration());
            log.info("Current Time: {}", new Date());
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
        }
        return false;
    }

    // ✅ Validate Refresh Token
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }

    private boolean validateToken(String token, String secret) {
        try {
            Claims claims = extractAllClaims(token, secret);
            return !claims.getExpiration().before(new Date()); // Ensure it's not expired
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token, String secret) {
        return extractAllClaims(token, secret).getExpiration().before(new Date());
    }
}
