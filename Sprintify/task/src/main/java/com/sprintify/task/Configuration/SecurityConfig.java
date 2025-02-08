package com.sprintify.task.Configuration;

import com.sprintify.task.Service.CustomerDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables @PreAuthorize in controllers
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomerDetailsService customerDetailsService;

    // Constructor-based injection to prevent circular dependency
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomerDetailsService customerDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customerDetailsService = customerDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for JWT-based security
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/sendmail", "/api/sendAttachment").permitAll()
                        .requestMatchers("/api/login", "/api/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/task/add").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/task/delete/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/task/update").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/task/user/**").hasAnyRole("USER", "ADMIN")



                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return security.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Correct method call
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
