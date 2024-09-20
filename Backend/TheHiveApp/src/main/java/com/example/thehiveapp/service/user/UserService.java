package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.User;

import java.util.List;

public interface UserService {
    public List<User> getUsers();
    public User createUser(User request);
    public User getUserById(Long id);
    public User updateUser(User request);
    public void deleteUser(Long id);
}
