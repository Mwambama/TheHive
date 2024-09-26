package com.example.thehiveapp.controller.authentication;

import com.example.thehiveapp.dto.authentication.LoginRequest;
import com.example.thehiveapp.dto.authentication.SignUpRequest;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public void createAuthentication(@RequestBody SignUpRequest request) {
        authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public void loginAuthentication(@RequestBody LoginRequest request){
        authenticationService.login(request);
    }
}
