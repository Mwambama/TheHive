package com.example.thehiveapp.service.authentication;

import com.example.thehiveapp.dto.ResponseMessage;
import com.example.thehiveapp.dto.authentication.CompanySignUpRequest;
import com.example.thehiveapp.dto.authentication.EmployerSignUpRequest;
import com.example.thehiveapp.dto.authentication.StudentSignUpRequest;
import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.entity.user.Student;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.thehiveapp.dto.email.ChangePasswordRequest;


public interface AuthenticationService extends UserDetailsService {
    Company signUpCompany(CompanySignUpRequest signUpRequest);
    Student signUpStudent(StudentSignUpRequest studentSignUpRequest);
    Employer signUpEmployer(EmployerSignUpRequest employerSignUpRequest);
    ResponseMessage changePassword(ChangePasswordRequest changePasswordRequest) throws BadRequestException;
    Boolean existsByEmailAndPassword(String email, String password);
}
