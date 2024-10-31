package com.example.hiveeapp.student_user.swipe;

public class JobApplicationRequest {
    private int studentId;
    private int jobPostingId;

    public JobApplicationRequest(int studentId, int jobPostingId) {
        this.studentId = studentId;
        this.jobPostingId = jobPostingId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getJobPostingId() {
        return jobPostingId;
    }
}

