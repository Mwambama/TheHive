package com.example.thehiveapp.repository.application;

import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);
    List<Application> findApplicationsByStudent(Student student);
    List<Application> findApplicationsByJobPosting(JobPosting jobPosting);
    List<Application> findApplicationsByJobPostingAndStatus(JobPosting jobPosting, Status status);
    Optional<Application> findApplicationByApplicationId(Long id);
    List<Application> findTop10ByStudentOrderByAppliedOnDesc(Student student);
}
