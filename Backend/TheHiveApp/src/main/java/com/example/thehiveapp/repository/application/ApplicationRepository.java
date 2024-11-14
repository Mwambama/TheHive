package com.example.thehiveapp.repository.application;

import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.status.Status;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Operation(
            summary = "Check if an application exists for a specific student and job posting",
            description = "Returns true if an application exists by the given student for the specified job posting; otherwise, returns false."
    )
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);
    @Operation(
            summary = "Find all applications by a specific student",
            description = "Retrieves a list of applications that have been submitted by a particular student."
    )
    List<Application> findApplicationsByStudent(Student student);
    @Operation(
            summary = "Find all applications for a specific job posting",
            description = "Retrieves a list of applications that have been submitted for a given job posting."
    )
    List<Application> findApplicationsByJobPosting(JobPosting jobPosting);
    @Operation(
            summary = "Find applications by job posting and status",
            description = "Retrieves a list of applications for a specific job posting filtered by a given status (ACCEPTED, PENDING, REJECTED)."
    )
    List<Application> findApplicationsByJobPostingAndStatus(JobPosting jobPosting, Status status);
    @Operation(
            summary = "Find an application by its application ID",
            description = "Retrieves a single application based on its unique application ID. Returns an Optional to handle cases where the application might not exist."
    )
    Optional<Application> findApplicationByApplicationId(Long id);
}
