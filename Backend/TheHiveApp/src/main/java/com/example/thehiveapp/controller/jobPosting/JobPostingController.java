package com.example.thehiveapp.controller.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @GetMapping("/search")
    @Operation(
            summary = "Search for job postings",
            description = "Search job postings based on various optional filters such as keywords, salary range, GPA range, dates, and employerId."
    )
    public List<JobPostingDto> searchJobPostings(
            @Parameter(description = "Keyword to search across title, description, and summary") @RequestParam(required = false) String q,
            @Parameter(description = "Like match for job title", schema = @Schema(type = "string", example = "developer")) @RequestParam(required = false) String title,
            @Parameter(description = "Like match for job description") @RequestParam(required = false) String description,
            @Parameter(description = "Like match for job summary") @RequestParam(required = false) String summary,
            @Parameter(description = "Minimum salary for job postings", schema = @Schema(type = "number", example = "10")) @RequestParam(required = false) BigDecimal minSalary,
            @Parameter(description = "Maximum salary for job postings", schema = @Schema(type = "number", example = "100")) @RequestParam(required = false) BigDecimal maxSalary,
            @Parameter(description = "Minimum GPA required", schema = @Schema(type = "number", format = "double", example = "2.0")) @RequestParam(required = false) BigDecimal minGpa,
            @Parameter(description = "Maximum GPA required", schema = @Schema(type = "number", format = "double", example = "4.0")) @RequestParam(required = false) BigDecimal maxGpa,
            @Parameter(description = "Earliest job start date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2024-01-01")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minJobStart,
            @Parameter(description = "Latest job start date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2027-12-31")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxJobStart,
            @Parameter(description = "Earliest application start date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2024-01-01")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minApplicationStart,
            @Parameter(description = "Latest application start date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2027-12-31")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxApplicationStart,
            @Parameter(description = "Earliest application end date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2024-01-01")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minApplicationEnd,
            @Parameter(description = "Latest application end date (yyyy-MM-dd)", schema = @Schema(type = "string", format = "date", example = "2027-12-31")) @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxApplicationEnd,
            @Parameter(description = "Exact match for Employer ID") @RequestParam(required = false) Long employerId) {

        JobPostingSearchDto searchDto = JobPostingSearchDto.builder()
                .q(q)
                .title(title)
                .description(description)
                .summary(summary)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .minGpa(minGpa)
                .maxGpa(maxGpa)
                .minJobStart(minJobStart)
                .maxJobStart(maxJobStart)
                .minApplicationStart(minApplicationStart)
                .maxApplicationStart(maxApplicationStart)
                .minApplicationEnd(minApplicationEnd)
                .maxApplicationEnd(maxApplicationEnd)
                .employerId(employerId)
                .build();
        return jobPostingService.searchJobPostings(searchDto);
    }

}
