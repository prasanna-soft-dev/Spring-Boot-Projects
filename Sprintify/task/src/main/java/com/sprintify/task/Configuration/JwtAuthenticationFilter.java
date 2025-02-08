package com.sprintify.task.Configuration;

import com.sprintify.task.Service.CustomerDetailsService;


import com.sprintify.task.Service.CustomerDetailsService;
import com.sprintify.task.Util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomerDetailsService customerDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomerDetailsService customerDetailsService){
        this.jwtUtil = jwtUtil;
        this.customerDetailsService = customerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        // Check if token is valid
        if (token != null && !token.trim().isEmpty()) {
            try {
                Claims claims = jwtUtil.extractAllClaims(token);
                if(claims != null) {
                    System.out.println("Claimed roles" + claims.get("roles"));
                    // Ensure claims are not null before proceeding
                    String email = jwtUtil.extractEmail(token);

                    // Add authorities/roles based on claims
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    String roles = claims.get("roles", String.class);  // Assuming roles are stored in the "roles" claim

                    if (roles != null) {
                        // Split by comma and trim spaces around roles
                        String[] roleArray = roles.split(",");
                        for (String role : roleArray) {
                            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.trim())); // No "ROLE_" prefix added here
                        }
                    }

                    // Create authentication object
                    Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Handle the exception (invalid/expired token or any parsing errors)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token.");
                return;
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    // Extract JWT token from the Authorization header
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            // Remove any extra Bearer if present
            bearerToken = bearerToken.replace("Bearer ", "").trim();

            // If the token is not empty, return it
            if (!bearerToken.isEmpty()) {
                return bearerToken;
            }
        }
        return null;
    }
}
