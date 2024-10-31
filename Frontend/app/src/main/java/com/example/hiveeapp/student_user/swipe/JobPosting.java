package com.example.hiveeapp.student_user.swipe;

public class JobPosting {
    private int jobPostingId;
    private String jobTitle;
    private String companyName;

    // Constructor
    public JobPosting(int jobPostingId, String jobTitle, String companyName) {
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
    }

    // Getter for Job Title
    public String getJobTitle() {
        return jobTitle;
    }

    // Getter for Company Name
    public String getCompanyName() {
        return companyName;
    }

    // Getter for Job Posting ID (if needed)
    public int getJobPostingId() {
        return jobPostingId;
    }
}

