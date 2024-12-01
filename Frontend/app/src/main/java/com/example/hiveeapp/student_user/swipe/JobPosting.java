package com.example.hiveeapp.student_user.swipe;

import java.io.Serializable;

public class JobPosting implements Serializable {
    private int jobPostingId;
    private String title;
    private String description;
    private String summary;
    private double salary;
    private String jobType;
    private double minimumGpa;
    private String jobStart;
    private String applicationStart;
    private String applicationEnd;
    private int employerId;
    private String companyName;

    // Full constructor
    public JobPosting(int jobPostingId, String title, String description, String summary, double salary,
                      String jobType, double minimumGpa, String jobStart, String applicationStart, String applicationEnd, int employerId, String companyName) {
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
        this.companyName = companyName;
    }

    // Constructor for cases without employerId and companyName
    public JobPosting(int jobPostingId, String title, String description, String summary, double salary,
                      String jobType, double minimumGpa, String jobStart, String applicationStart, String applicationEnd) {
        this(jobPostingId, title, description, summary, salary, jobType, minimumGpa, jobStart, applicationStart, applicationEnd, -1, "Unknown Company");
    }

    // Getters
    public int getJobPostingId() { return jobPostingId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSummary() { return summary; }
    public double getSalary() { return salary; }
    public String getJobType() { return jobType; }
    public double getMinimumGpa() { return minimumGpa; }
    public String getJobStart() { return jobStart; }
    public String getApplicationStart() { return applicationStart; }
    public String getApplicationEnd() { return applicationEnd; }
    public int getEmployerId() { return employerId; }
    public String getCompanyName() { return companyName; }

    // Optional setters for flexibility (only add if needed for mutable fields)
    public void setSalary(double salary) { this.salary = salary; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    // Helper method for display purposes
    @Override
    public String toString() {
        return "JobPosting{" +
                "jobPostingId=" + jobPostingId +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", salary=" + salary +
                ", jobType='" + jobType + '\'' +
                '}';
    }
}
