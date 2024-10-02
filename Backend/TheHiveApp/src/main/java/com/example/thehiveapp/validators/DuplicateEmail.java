package com.example.thehiveapp.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DuplicateEmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateEmail {
    String message() default "User already exists with this email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

