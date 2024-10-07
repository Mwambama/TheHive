package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.CustomUserDetails;
import com.example.thehiveapp.config.SecurityConfig;
import com.example.thehiveapp.dto.authentication.BaseSignUpRequest;
import com.example.thehiveapp.entity.authentication.Authentication;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.repository.authentication.AuthenticationRepository;
import com.example.thehiveapp.service.user.CompanyService;
import com.example.thehiveapp.service.user.EmployerService;
import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired private AuthenticationRepository authenticationRepository;
    @Autowired private SecurityConfig securityConfig;
    @Autowired private CompanyService companyService;
    @Autowired private StudentService studentService;
    @Autowired private UserService userService;
    @Autowired private EmployerService employerService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.getUserByEmail(email);
        return new CustomUserDetails(user, authenticationRepository);
    }

    @Transactional
    public void signUp(BaseSignUpRequest baseSignUpRequest) {
        Long userId;
        switch (baseSignUpRequest.getRole()) {
            case COMPANY -> userId = createCompany(baseSignUpRequest);
            case EMPLOYER -> userId = createEmployer(baseSignUpRequest);
            case STUDENT -> userId = createStudent(baseSignUpRequest);
            default -> throw new IllegalArgumentException("Invalid role: " + baseSignUpRequest.getRole());
        }
        createAuthentication(baseSignUpRequest, userId);
    }

    private void createAuthentication(BaseSignUpRequest baseSignUpRequest, Long userId) {
        Authentication authentication = new Authentication();
        authentication.setUserId(userId);
        authentication.setPassword(securityConfig.passwordEncoder().encode(baseSignUpRequest.getPassword()));
        authenticationRepository.save(authentication);
    }

    private Long createCompany(BaseSignUpRequest baseSignUpRequest) {
        Company company = new Company();
        company.setEmail(baseSignUpRequest.getEmail());
        company.setName(baseSignUpRequest.getName());
        company.setRole(Role.COMPANY);
        companyService.createCompany(company);
        return company.getUserId();
    }

    private Long createEmployer(BaseSignUpRequest baseSignUpRequest) {
        return 1L;
        // TODO
    }

    private Long createStudent(BaseSignUpRequest baseSignUpRequest) {
        return 2L;
        // TODO
    }
}

