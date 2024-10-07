//package com.example.thehiveapp.dto.jobPosting;
//
//import com.example.thehiveapp.enums.jobPosting.JobType;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//public class JobPostingDto {
//
//    private Long jobPostingId;
//    private String title;
//    private String description;
//    private String summary;
//    private BigDecimal salary;
//    private JobType jobType;
//    private BigDecimal minimumGpa;
//    private LocalDate jobStart;
//    private LocalDate applicationStart;
//    private LocalDate applicationEnd;
//    private Long employerId;
//
//    public JobPostingDto(String title,
//                         String description,
//                         String summary,
//                         BigDecimal salary,
//                         JobType jobType,
//                         BigDecimal minimumGpa,
//                         LocalDate jobStart,
//                         LocalDate applicationStart,
//                         LocalDate applicationEnd,
//                         Long employerId
//    ) {
//        this.title = title;
//        this.description = description;
//        this.summary = summary;
//        this.salary = salary;
//        this.jobType = jobType;
//        this.minimumGpa = minimumGpa;
//        this.jobStart = jobStart;
//        this.applicationStart = applicationStart;
//        this.applicationEnd = applicationEnd;
//        this.employerId = employerId;
//    }
//
//    public Long getJobPostingId() {
//        return jobPostingId;
//    }
//
//    public void setJobPostingId(Long jobPostingId) {
//        this.jobPostingId = jobPostingId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getSummary() {
//        return summary;
//    }
//
//    public void setSummary(String summary) {
//        this.summary = summary;
//    }
//
//    public BigDecimal getSalary() {
//        return salary;
//    }
//
//    public void setSalary(BigDecimal salary) {
//        this.salary = salary;
//    }
//
//    public JobType getJobType() {
//        return jobType;
//    }
//
//    public void setJobType(JobType jobType) {
//        this.jobType = jobType;
//    }
//
//    public BigDecimal getMinimumGpa() {
//        return minimumGpa;
//    }
//
//    public void setMinimumGpa(BigDecimal minimumGpa) {
//        this.minimumGpa = minimumGpa;
//    }
//
//    public LocalDate getJobStart() {
//        return jobStart;
//    }
//
//    public void setJobStart(LocalDate jobStart) {
//        this.jobStart = jobStart;
//    }
//
//    public LocalDate getApplicationStart() {
//        return applicationStart;
//    }
//
//    public void setApplicationStart(LocalDate applicationStart) {
//        this.applicationStart = applicationStart;
//    }
//
//    public LocalDate getApplicationEnd() {
//        return applicationEnd;
//    }
//
//    public void setApplicationEnd(LocalDate applicationEnd) {
//        this.applicationEnd = applicationEnd;
//    }
//
//    public Long getEmployerId() {
//        return employerId;
//    }
//
//    public void setEmployerId(Long employerId) {
//        this.employerId = employerId;
//    }
//}
