package com.example.thehiveapp.service.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getJobPostings();
    List<JobPostingDto> getJobPostingsForStudent(Long studentId);
    List<JobPostingDto> getJobPostingsByEmployerId(Long employerId);
    JobPostingDto createJobPosting(JobPostingDto request);
    JobPostingDto getJobPostingById(Long id);
    byte[] getJobPostingAnalytics(Long jobPostingId);
    JobPostingDto updateJobPosting(JobPostingDto request);
    void deleteJobPosting(Long id);
    List<JobPostingDto> searchJobPostings(JobPostingSearchDto searchDto);
    List<JobPostingDto> getJobPostingSuggestions(Long studentId);
}
