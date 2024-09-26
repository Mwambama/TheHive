package com.example.thehiveapp.dto.authentication;

import com.example.thehiveapp.enums.user.Role;

public class LoginRequest {
    private String email;
    private String password;

    private Role role;

    public LoginRequest(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public void setRole(Role role) { this.role = role; }

    public Role getRole() { return role; }
}
