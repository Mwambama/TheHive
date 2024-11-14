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
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/job-posting")
public class JobPostingController {

    @Autowired private JobPostingService jobPostingService;

    public JobPostingController() {}

    @Operation(summary = "Get all job postings or filter by employer ID", description = "Retrieves a list of all job postings or filters them by employer ID if provided.")
    @GetMapping
    public List<JobPostingDto> getJobPostings(@RequestParam(value = "employerId", required = false) Long employerId) {
        if (employerId != null) {
            return jobPostingService.getJobPostingsByEmployerId(employerId);
        }
        return jobPostingService.getJobPostings();
    }

    @Operation(summary = "Get job postings available for a specific student", description = "Retrieves job postings that are available for a specific student by their student ID.")
    @GetMapping("/student/{studentId}")
    public List<JobPostingDto> getJobPostingsForStudent(@PathVariable Long studentId) {
        return jobPostingService.getJobPostingsForStudent(studentId);
    }

    @Operation(summary = "Create a new job posting", description = "Creates a new job posting with the details provided in the request body.")
    @PostMapping
    public JobPostingDto createJobPosting(@RequestBody JobPostingDto dto) {
        return jobPostingService.createJobPosting(dto);
    }

    @Operation(summary = "Get a job posting by ID", description = "Retrieves a job posting by its unique ID.")
    @GetMapping("/{id}")
    public JobPostingDto getJobPostingById(@PathVariable Long id) {
        return jobPostingService.getJobPostingById(id);
    }

    @Operation(summary = "Update an existing job posting", description = "Updates an existing job posting with the provided details.")
    @PutMapping
    public JobPostingDto updateJobPosting(@RequestBody JobPostingDto dto) {
        return jobPostingService.updateJobPosting(dto);
    }

    @Operation(summary = "Delete a job posting by ID", description = "Deletes a job posting by its unique ID.")
    @DeleteMapping("/{id}")
    public String deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return "Job posting successfully deleted";
    }
}
