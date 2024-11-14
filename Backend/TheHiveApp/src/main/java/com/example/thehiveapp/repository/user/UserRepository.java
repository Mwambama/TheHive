package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    @Operation(
            summary = "Find User by Email",
            description = "Fetches the user details by the provided email address."
    )
    User findByEmail(String email);
    @Operation(
            summary = "Check if User Exists by Email",
            description = "Checks if a user with the provided email address already exists in the database."
    )
    Boolean existsByEmail(String email);
}
