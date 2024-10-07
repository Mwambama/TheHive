package com.example.thehiveapp.dto.authentication;

import com.example.thehiveapp.validators.DuplicateEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseSignUpRequest {
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @DuplicateEmail
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
            message = "Password must have at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password;

    @NotNull(message = "Name is required")
    private String name;
}
