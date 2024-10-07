package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.LoginRequest;
import com.example.thehiveapp.dto.authentication.SignUpRequest;
import com.example.thehiveapp.dto.email.ChangePasswordRequest;


public interface AuthenticationService {
    void signUp(SignUpRequest signUpRequest);
    void login(LoginRequest loginRequest);
    String changePassword(ChangePasswordRequest changePasswordRequest);
}
