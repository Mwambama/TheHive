package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.CustomUserDetails;
import com.example.thehiveapp.config.SecurityConfig;
import com.example.thehiveapp.dto.authentication.SignUpRequest;
import com.example.thehiveapp.entity.authentication.Authentication;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.repository.authentication.AuthenticationRepository;
import com.example.thehiveapp.service.user.CompanyService;
import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired AuthenticationRepository authenticationRepository;

    @Autowired private SecurityConfig securityConfig;

    @Autowired private CompanyService companyService;

    @Autowired private StudentService studentService;

    @Autowired private UserService userService;

    // @Autowired private EmployerService employerService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.getUserByEmail(email);
        return new CustomUserDetails(user, authenticationRepository);
    }

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

