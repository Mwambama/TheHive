package com.example.thehiveapp.controller.authentication;

import com.example.thehiveapp.dto.authentication.BaseSignUpRequest;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/account")
public class AuthenticationController {

    @Autowired private AuthenticationService authenticationService;
    @Autowired private UserService userService;

    public AuthenticationController() {}

    @PostMapping("/signup")
    public void createAuthentication(@Valid @RequestBody BaseSignUpRequest request) {
        authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public User loginAuthentication(){
        return userService.getCurrentUser();
    }
}
