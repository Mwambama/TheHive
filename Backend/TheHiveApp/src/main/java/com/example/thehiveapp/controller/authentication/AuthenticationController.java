package com.example.thehiveapp.controller.authentication;

import com.example.thehiveapp.dto.ResponseMessage;
import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.dto.email.ChangePasswordRequest;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
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

    @Operation(summary = "Sign up a company", description = "Registers a new company user with the provided request.")
    @PostMapping("/signup/company")
    public Company createCompany(@Valid @RequestBody CompanySignUpRequest request) {
        return authenticationService.signUpCompany(request);
    }

    @Operation(summary = "Sign up an employer", description = "Registers a new employer with the provided request.")
    @PostMapping("/signup/employer")
    public Employer createEmployer(@Valid @RequestBody EmployerSignUpRequest request) {
        return authenticationService.signUpEmployer(request);
    }

    @Operation(summary = "Sign up a student", description = "Registers a new student user with the provided request.")
    @PostMapping("/signup/student")
    public Student createStudent(@Valid @RequestBody StudentSignUpRequest request) {
        return authenticationService.signUpStudent(request);
    }

    @Operation(summary = "Login and get the current logged-in user", description = "Authenticates the user and retrieves the currently logged-in user's information.")
    @PostMapping("/login")
    public User loginAuthentication(){
        return userService.getCurrentUser();
    }

    @Operation(summary = "Change password for the current user", description = "Allows the currently authenticated user to change their password.")
    @PostMapping("/change-password")
    public ResponseMessage changePasswordAuthentication(@RequestBody ChangePasswordRequest changePasswordRequest) throws BadRequestException {
        return authenticationService.changePassword(changePasswordRequest);
    }
}
