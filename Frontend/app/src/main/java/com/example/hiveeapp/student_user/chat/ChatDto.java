package com.example.hiveeapp.student_user.chat;

public class ChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private int jobPostingId;
    private String jobTitle;

    // Constructor that includes jobTitle
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
    }

    public int getChatId() {
        return chatId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getJobPostingId() {
        return jobPostingId;
    }

    public String getJobTitle() {
        return jobTitle;
    }
}
