package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired private UserService userService;

    public UserController() {}

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all registered users.")
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Create a new user", description = "Registers a new user with the provided request.")
    @PostMapping
    public User createUser(@RequestBody User request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user's details based on their unique ID.")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Update user details", description = "Updates an existing user's details based on the provided request.")
    @PutMapping
    public User updateUser(@RequestBody User request) {
        return userService.updateUser(request);
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user's account based on their unique ID.")
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User account successfully deleted";
    }
}
