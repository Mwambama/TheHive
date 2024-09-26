package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.enums.user.Role;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User createUser(User request);
    User getUserById(Long id);
    User updateUser(User request);
    void deleteUser(Long id);
    Long getIdByEmail(String email, Role role);
}
