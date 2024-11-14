package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.Student;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Operation(
            summary = "Find Student by User ID",
            description = "Retrieves a student based on the provided user ID."
    )
    Optional<Student> findByUserId(Long userId);

}
