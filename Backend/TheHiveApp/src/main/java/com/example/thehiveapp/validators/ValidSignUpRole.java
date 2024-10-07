package com.example.thehiveapp.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidSignUpRoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSignUpRole {
    String message() default "Invalid role: must be student, employer, or company";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

