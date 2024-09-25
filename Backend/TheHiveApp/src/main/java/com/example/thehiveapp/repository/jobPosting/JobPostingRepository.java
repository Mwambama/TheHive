package com.example.thehiveapp.repository.jobPosting;

import com.example.thehiveapp.entity.jobPosting.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
}
