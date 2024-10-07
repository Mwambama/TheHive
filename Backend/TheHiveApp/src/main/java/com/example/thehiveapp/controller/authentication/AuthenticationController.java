package com.example.thehiveapp.controller.authentication;

import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
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

    @PostMapping("/signup/company")
    public Company createCompany(@Valid @RequestBody CompanySignUpRequest request) {
        return authenticationService.signUpCompany(request);
    }

    @PostMapping("/signup/employer")
    public Employer createEmployer(@Valid @RequestBody EmployerSignUpRequest request) {
        return authenticationService.signUpEmployer(request);
    }

    @PostMapping("/signup/student")
    public Student createStudent(@Valid @RequestBody StudentSignUpRequest request) {
        return authenticationService.signUpStudent(request);
    }

    @PostMapping("/login")
    public User loginAuthentication(){
        return userService.getCurrentUser();
    }
}
