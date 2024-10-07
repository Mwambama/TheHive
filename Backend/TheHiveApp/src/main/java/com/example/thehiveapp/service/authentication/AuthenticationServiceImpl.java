package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.CustomUserDetails;
import com.example.thehiveapp.config.SecurityConfig;
import com.example.thehiveapp.dto.authentication.BaseSignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.dto.email.ChangePasswordRequest;
import com.example.thehiveapp.entity.authentication.Authentication;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.repository.authentication.AuthenticationRepository;
import com.example.thehiveapp.service.user.CompanyService;
import com.example.thehiveapp.service.user.EmployerService;
import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    @Override
    @Transactional
    public Company signUpCompany(CompanySignUpRequest signUpRequest) {
        Company company = new Company();
        company.setEmail(signUpRequest.getEmail());
        company.setName(signUpRequest.getName());
        company.setRole(Role.COMPANY);
        companyService.createCompany(company);
        createAuthentication(signUpRequest, company.getUserId());
        return company;
    }

    @Override
    @Transactional
    public Student signUpStudent(StudentSignUpRequest studentSignUpRequest) {
        Student student = new Student();
        student.setEmail(studentSignUpRequest.getEmail());
        student.setName(studentSignUpRequest.getName());
        student.setRole(Role.STUDENT);
        student.setUniversity(studentSignUpRequest.getUniversity());
        studentService.createStudent(student);
        createAuthentication(studentSignUpRequest, student.getUserId());
        return student;
    }

    @Override
    @Transactional
    public Employer signUpEmployer(EmployerSignUpRequest employerSignUpRequest) {
        Employer employer = new Employer();
        employer.setEmail(employerSignUpRequest.getEmail());
        employer.setName(employerSignUpRequest.getName());
        employer.setRole(Role.EMPLOYER);
        Company company = companyService.getCompanyById(employerSignUpRequest.getCompanyId());
        employer.setCompanyID(company.getUserId());
        employerService.createEmployer(employer);
        createAuthentication(employerSignUpRequest, employer.getUserId());
        return employer;
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())){
            return "Passwords do not match! Try again!";
        }
        User user = userService.getUserByEmail(changePasswordRequest.getEmail());
        Authentication authentication = authenticationRepository.findById(user.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User not found"));
        authentication.setPassword(securityConfig.passwordEncoder().encode(changePasswordRequest.getConfirmPassword()));
        authenticationRepository.save(authentication);
        return "Password changed successfully";
    }

    private void createAuthentication(BaseSignUpRequest baseSignUpRequest, Long userId) {
        Authentication authentication = new Authentication();
        authentication.setUserId(userId);
        authentication.setPassword(securityConfig.passwordEncoder().encode(baseSignUpRequest.getPassword()));
        authenticationRepository.save(authentication);
    }

}

