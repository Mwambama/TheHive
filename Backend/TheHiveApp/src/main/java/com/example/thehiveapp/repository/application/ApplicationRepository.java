package com.example.thehiveapp.repository.application;

import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByStudentAndJobPosting(Student student, JobPosting jobPosting);
@Query("SELECT a FROM Application a WHERE a.student.userId = :studentId")
List<Application> findApplicationsByStudentId(@Param("studentId") Long studentId);

}
