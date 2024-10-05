package com.example.thehiveapp.validators;

import com.example.thehiveapp.enums.user.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSignUpRoleValidator implements ConstraintValidator<ValidSignUpRole, Role> {

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        if (role == null) {
            return true;
        }
        return role == Role.student || role == Role.employer || role == Role.company;
    }
}

