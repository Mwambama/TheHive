package com.example.hiveeapp.student_user.application;

public class JobApplication {
    private int applicationId;
    private int jobPostingId;
    private int studentId;
    private String jobTitle;
    private String status;
    private String appliedOn;

    public JobApplication(int applicationId, int jobPostingId, int studentId, String jobTitle, String status, String appliedOn) {
        this.applicationId = applicationId;
        this.jobPostingId = jobPostingId;
        this.studentId = studentId;
        this.jobTitle = jobTitle;
        this.status = status;
        this.appliedOn = appliedOn;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getJobPostingId() {
        return jobPostingId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getStatus() {
        return status;
    }

    public String getAppliedOn() {
        return appliedOn;
    }
}


