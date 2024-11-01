package com.example.thehiveapp.repository.application;

import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);

    @Query("SELECT a FROM Application a WHERE a.student.userId = :studentId")
    List<Application> findApplicationsByStudentId(@Param("studentId") Long studentId);

    List<Application> findApplicationsByJobPosting(JobPosting jobPosting);
    List<Application> findApplicationsByJobPostingAndStatus(JobPosting jobPosting, Status status);
}
