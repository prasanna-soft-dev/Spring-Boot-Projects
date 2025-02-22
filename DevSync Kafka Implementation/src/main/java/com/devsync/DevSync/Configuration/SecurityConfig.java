package com.devsync.DevSync.Configuration;

import com.devsync.DevSync.Filter.JwtAuthenticationFilter;
import com.devsync.DevSync.Service.UserInfoService;
import com.devsync.DevSync.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor // ✅ Constructor Injection Instead of @Autowired
public class SecurityConfig {

    private final UserInfoService userInfoService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // ✅ Allow login & registration
                        .requestMatchers("/api/forum").authenticated()  // ✅ Only authenticated users can join the forum
                        .requestMatchers("/api/forum/send").authenticated()  // ✅ Only authenticated users can send messages
                        .anyRequest().authenticated()  // ✅ Protect all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Enforce JWT-based authentication
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userInfoService), UsernamePasswordAuthenticationFilter.class) // ✅ Register JWT Filter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userInfoService);
        return new ProviderManager(List.of(daoAuthenticationProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
