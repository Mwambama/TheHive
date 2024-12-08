package com.example.hiveeapp.student_user.search;

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
    private boolean applied; // New field to track application status

    public JobPosting(int jobPostingId, String title, String description, String summary, double salary,
                      String jobType, double minimumGpa, String jobStart, String applicationStart,
                      String applicationEnd, int employerId, String companyName) {
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
        this.applied = false; // Default to not applied
    }

    // Getters and setters
    public int getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(int jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public double getMinimumGpa() {
        return minimumGpa;
    }

    public void setMinimumGpa(double minimumGpa) {
        this.minimumGpa = minimumGpa;
    }

    public String getJobStart() {
        return jobStart;
    }

    public void setJobStart(String jobStart) {
        this.jobStart = jobStart;
    }

    public String getApplicationStart() {
        return applicationStart;
    }

    public void setApplicationStart(String applicationStart) {
        this.applicationStart = applicationStart;
    }

    public String getApplicationEnd() {
        return applicationEnd;
    }

    public void setApplicationEnd(String applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public int getEmployerId() {
        return employerId;
    }

    public void setEmployerId(int employerId) {
        this.employerId = employerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }
}
