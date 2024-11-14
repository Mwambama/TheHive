package com.example.thehiveapp.repository.jobPosting;

import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Employer;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    @Operation(
            summary = "Find job postings by employer",
            description = "Retrieves a list of job postings associated with a specific employer."
    )
    List<JobPosting> findByEmployer(Employer employer);
    @Operation(
            summary = "Find job postings excluding those already applied by student",
            description = "Retrieves a list of job postings that a specific student has not yet applied to, based on their user ID."
    )
    @Query("SELECT jp FROM JobPosting jp WHERE jp NOT IN " +
            "(SELECT a.jobPosting FROM Application a WHERE a.student.userId = :studentId)")
    List<JobPosting> findJobPostings(@Param("studentId") Long studentId);
}
