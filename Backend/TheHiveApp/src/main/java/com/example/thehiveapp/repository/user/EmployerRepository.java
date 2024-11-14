package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.Employer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    @Operation(
            summary = "Find Employers by Company ID",
            description = "Fetches all the employers associated with a given company ID."
    )
    List<Employer> findByCompanyId(Long companyId);
    @Operation(
            summary = "Find Employer by User ID",
            description = "Retrieves an employer based on the provided user ID."
    )
    Optional<Employer> findByUserId(Long userId);
}
