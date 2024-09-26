package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.config.SecurityConfig;
import com.example.thehiveapp.dto.authentication.LoginRequest;
import com.example.thehiveapp.dto.authentication.SignUpRequest;
import com.example.thehiveapp.entity.authentication.Authentication;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.repository.authentication.AuthenticationRepository;
import com.example.thehiveapp.service.user.CompanyService;
import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired AuthenticationRepository authenticationRepository;

    @Autowired private SecurityConfig securityConfig;

    @Autowired private CompanyService companyService;

    @Autowired private StudentService studentService;

    @Autowired private UserService userService;

    // @Autowired private EmployerService employerService;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        Long userId;
        switch (signUpRequest.getRole()) {
            case company -> userId = createCompany(signUpRequest);
            case employer -> userId = createEmployer(signUpRequest);
            case student -> userId = createStudent(signUpRequest);
            default -> throw new IllegalArgumentException("Invalid role: " + signUpRequest.getRole());
        }

        createAuthentication(signUpRequest, userId);
    }

    private void createAuthentication(SignUpRequest signUpRequest, Long userId) {
        Authentication authentication = new Authentication();
        authentication.setUserId(userId);
        authentication.setPassword(securityConfig.passwordEncoder().encode(signUpRequest.getPassword()));
        authenticationRepository.save(authentication);
    }

    public void login(LoginRequest loginRequest){
        Long userId = userService.getIdByEmail(loginRequest.getEmail(), loginRequest.getRole());
        Authentication authentication = authenticationRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        boolean isPasswordMatch = securityConfig.passwordEncoder().matches(loginRequest.getPassword(), authentication.getPassword());
        if (!isPasswordMatch) {
            throw new IllegalArgumentException("Invalid password for email: " + loginRequest.getEmail());
        }
    }


    private Long createCompany(SignUpRequest signUpRequest) {
        Company company = new Company();
        company.setEmail(signUpRequest.getEmail());
        company.setName(signUpRequest.getName());
        company.setRole(Role.company);
        companyService.createCompany(company);
        return company.getUserId();
    }

    private Long createEmployer(SignUpRequest signUpRequest) {
        return 1L;
        // TODO
    }

    private Long createStudent(SignUpRequest signUpRequest) {
        return 2L;
        // TODO
    }
}

