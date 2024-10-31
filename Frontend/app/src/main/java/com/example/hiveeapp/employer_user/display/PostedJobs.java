package com.example.hiveeapp.employer_user.display;

import java.io.Serializable;

public class PostedJobs implements Serializable {
    private String jobPostingId; // Unique identifier for the job posting
    private String title; // Job title
    private String description; // Job description
    private String summary; // Job summary
    private double salary; // Salary requirements
    private String jobType; // Type of job (e.g., INTERNSHIP)
    private String minimumGpa; // Minimum GPA requirement
    private String jobStart; // Job start date
    private String applicationStart; // Application start date
    private String applicationEnd; // Application end date
    private int employerId; // Employer ID

    // Constructor
    public PostedJobs(String jobPostingId, String title, String description, String summary, double salary,
                      String jobType, String minimumGpa, String jobStart, String applicationStart,
                      String applicationEnd, int employerId) {
        this.jobPostingId = jobPostingId;
        this.title = title;
        this.description = description;
        this.summary = summary;
        this.salary = salary;
        this.jobType = jobType;
        this.minimumGpa = minimumGpa;
        this.jobStart = jobStart;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.employerId = employerId;
    }

    // Getters
    public String getJobPostingId() {
        return jobPostingId; // Return the job posting ID
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
    }

    public double getSalary() {
        return salary;
    }

    public String getJobType() {
        return jobType;
    }

    public String getMinimumGpa() {
        return minimumGpa;
    }

    public String getJobStart() {
        return jobStart;
    }

    public String getApplicationStart() {
        return applicationStart;
    }

    public String getApplicationEnd() {
        return applicationEnd;
    }

    public int getEmployerId() {
        return employerId;
    }

    // Setters
    public void setJobPostingId(String jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setMinimumGpa(String minimumGpa) {
        this.minimumGpa = minimumGpa;
    }

    public void setJobStart(String jobStart) {
        this.jobStart = jobStart;
    }

    public void setApplicationStart(String applicationStart) {
        this.applicationStart = applicationStart;
    }

    public void setApplicationEnd(String applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }
}
