package com.example.thehiveapp.repository.jobPosting;

import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByEmployer(Employer employer);
    @Query("SELECT jp FROM JobPosting jp WHERE jp NOT IN " +
            "(SELECT a.jobPosting FROM Application a WHERE a.student.userId = :studentId)")
    List<JobPosting> findJobPostings(@Param("studentId") Long studentId);
}
