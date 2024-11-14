package com.example.thehiveapp.repository.authentication;

import com.example.thehiveapp.entity.authentication.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    @Operation(
            summary = "Find authentication details by user ID",
            description = "Retrieves the authentication details for a user based on their unique user ID."
    )
    Authentication findByUserId(Long userId);
}
