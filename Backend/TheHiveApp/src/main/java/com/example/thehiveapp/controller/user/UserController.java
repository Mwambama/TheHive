package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.repository.user.UserRepository;
import com.example.thehiveapp.service.user.UserService;
import com.example.thehiveapp.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    public UserController() {}

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public User updateUser(@RequestBody User request) {
        return userService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return;
    }
}
