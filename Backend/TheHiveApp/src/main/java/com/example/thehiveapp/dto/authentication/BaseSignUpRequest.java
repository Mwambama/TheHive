package com.example.thehiveapp.dto.authentication;

import com.example.thehiveapp.enums.user.Role;
import com.example.thehiveapp.validators.DuplicateEmail;
import com.example.thehiveapp.validators.ValidSignUpRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    @NotNull(message = "Role is required")
    @ValidSignUpRole
    private Role role;

    public BaseSignUpRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
