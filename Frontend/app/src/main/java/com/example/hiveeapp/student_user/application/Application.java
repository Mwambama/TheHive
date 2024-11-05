package com.example.hiveeapp.student_user.application;

public class Application {
    private int applicationId;
    private int jobPostingId;
    private String jobTitle;
    private String status;
    private String appliedOn;

    public Application(int applicationId, int jobPostingId, String jobTitle, String status, String appliedOn) {
        this.applicationId = applicationId;
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
        this.status = status;
        this.appliedOn = appliedOn;
    }

    public int getApplicationId() { return applicationId; }
    public int getJobPostingId() { return jobPostingId; }
    public String getJobTitle() { return jobTitle; }
    public String getStatus() { return status; }
    public String getAppliedOn() { return appliedOn; }
}
