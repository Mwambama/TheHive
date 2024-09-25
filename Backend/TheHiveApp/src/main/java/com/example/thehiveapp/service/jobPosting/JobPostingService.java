package com.example.thehiveapp.service.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getJobPostings();
    JobPostingDto createJobPosting(JobPostingDto request);
    JobPostingDto getJobPostingById(Long id);
    JobPostingDto updateJobPosting(JobPostingDto request);
    void deleteJobPosting(Long id);
}
