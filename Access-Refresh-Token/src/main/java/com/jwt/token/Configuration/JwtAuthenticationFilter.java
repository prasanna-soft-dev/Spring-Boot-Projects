package com.jwt.token.Configuration;

import com.jwt.token.Security.JwtUtil;
import com.jwt.token.Service.UserInfoService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserInfoService userInfoService; // Load user details from database

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        logger.info("Authorization header Extracted token : " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No JWT token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token correctly
        String token = authHeader.replaceFirst("^Bearer\\s+", ""); // Safer extraction
        logger.info("Extracted JWT Token: '" + token + "'"); // Log extracted token

        if (token.isBlank()) {
            logger.warning("JWT Token is empty");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("JWT Token is empty.");
            return;
        }

        String email = null;

        try {
            email = jwtUtil.extractEmailFromAccessToken(token); // Extract email from token
        } catch (IllegalArgumentException e) {
            logger.severe("Invalid JWT Token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid JWT Token format.");
            return;
        } catch (ExpiredJwtException e) {
            logger.warning("JWT token is expired: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired. Please re-authenticate.");
            return;
        } catch (Exception e) {
            logger.severe("Error parsing JWT token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid token format.");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoService.loadUserByUsername(email);

            if (jwtUtil.validateAccessToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("JWT token successfully validated for user: " + email);
            } else {
                logger.warning("Invalid JWT token");
            }
        }

        filterChain.doFilter(request, response);
    }
}
