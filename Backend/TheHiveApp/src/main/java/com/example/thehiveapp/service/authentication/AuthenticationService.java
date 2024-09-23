package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.SignUpRequest;


public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequest);
}
