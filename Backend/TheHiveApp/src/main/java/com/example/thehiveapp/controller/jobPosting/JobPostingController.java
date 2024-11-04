package com.example.thehiveapp.controller.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/job-posting")
public class JobPostingController {

    @Autowired private JobPostingService jobPostingService;

    public JobPostingController() {}

    @GetMapping
    public List<JobPostingDto> getJobPostings(@RequestParam(value = "employerId", required = false) Long employerId) {
        if (employerId != null) {
            return jobPostingService.getJobPostingsByEmployerId(employerId);
        }
        return jobPostingService.getJobPostings();
    }

    @GetMapping("/student/{studentId}")
    public List<JobPostingDto> getJobPostingsForStudent(@PathVariable Long studentId) {
        return jobPostingService.getJobPostingsForStudent(studentId);
    }

    @PostMapping
    public JobPostingDto createJobPosting(@RequestBody JobPostingDto dto) {
        return jobPostingService.createJobPosting(dto);
    }

    @GetMapping("/{id}")
    public JobPostingDto getJobPostingById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id);
    }

    @PutMapping
    public JobPostingDto updateJobPosting(@RequestBody JobPostingDto dto) {
        return jobPostingService.updateJobPosting(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return "Job posting successfully deleted";
    }
}
