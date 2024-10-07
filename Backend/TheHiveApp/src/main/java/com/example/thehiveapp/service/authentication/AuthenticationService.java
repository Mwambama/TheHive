package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface AuthenticationService extends UserDetailsService {
    void signUp(SignUpRequest signUpRequest);
}
