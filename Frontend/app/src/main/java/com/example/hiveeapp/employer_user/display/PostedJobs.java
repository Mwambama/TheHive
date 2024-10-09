package com.example.hiveeapp.employer_user.display;

import java.io.Serializable;

public class PostedJobs implements Serializable {
    private String jobId; // Unique identifier for the job
    private String jobTitle;
    private String jobDescription;
    private String jobType;
    private String salaryRequirements;
    private String ageRequirement;
    private String minimumGpa;

    // Constructor
    public PostedJobs(String jobId, String jobTitle, String jobDescription, String jobType, String salaryRequirements, String ageRequirement, String minimumGpa) {
        this.jobId = jobId; // Initialize jobId
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.jobType = jobType;
        this.salaryRequirements = salaryRequirements;
        this.ageRequirement = ageRequirement;
        this.minimumGpa = minimumGpa;
    }

    // Getters
    public String getJobId() {
        return jobId; // Return the job ID
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getJobType() {
        return jobType;
    }

    public String getSalaryRequirements() {
        return salaryRequirements;
    }

    public String getAgeRequirement() {
        return ageRequirement;
    }

    public String getMinimumGpa() {
        return minimumGpa;
    }

    // Setters
    // For when the user/employer updates the job posting
    public void setJobId(String jobId) {
        this.jobId = jobId; // Add setter for jobId if needed
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setSalaryRequirements(String salaryRequirements) {
        this.salaryRequirements = salaryRequirements;
    }

    public void setAgeRequirement(String ageRequirement) {
        this.ageRequirement = ageRequirement;
    }

    public void setMinimumGpa(String minimumGpa) {
        this.minimumGpa = minimumGpa;
    }
}
