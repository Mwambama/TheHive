package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.User;

public interface UserService {
    User getUserById(Long id);
    User getUserByEmail(String email);
    Boolean existsByEmail(String email);
    User getCurrentUser();
}
