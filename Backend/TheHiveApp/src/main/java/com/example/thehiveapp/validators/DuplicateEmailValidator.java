package com.example.thehiveapp.validators;

import com.example.thehiveapp.service.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DuplicateEmailValidator implements ConstraintValidator<DuplicateEmail, String> {

    private final UserService userService;

    public DuplicateEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        return !userService.existsByEmail(email);
    }
}

