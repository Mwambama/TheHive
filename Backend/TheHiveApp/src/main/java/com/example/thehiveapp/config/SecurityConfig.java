package com.example.thehiveapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Specify authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/account/signup").permitAll()
                                .requestMatchers(HttpMethod.POST, "/otp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/account/change-password").permitAll()
                        .anyRequest().authenticated()
                )
                // Use HTTP Basic authentication (can be replaced with form login or JWT)
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // In case you're using a custom UserDetailsService
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService(); // Replace with your actual implementation
//    }
}

