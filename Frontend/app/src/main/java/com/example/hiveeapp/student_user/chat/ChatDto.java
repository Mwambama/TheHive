package com.example.hiveeapp.student_user.chat;

public class ChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private int jobPostingId;
    private String jobTitle;

    // Constructor
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
    }

    // Getters and setters
    public int getChatId() { return chatId; }
    public int getEmployerId() { return employerId; }
    public int getStudentId() { return studentId; }
    public int getJobPostingId() { return jobPostingId; }
    public String getJobTitle() { return jobTitle; }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setJobPostingId(int jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
}
