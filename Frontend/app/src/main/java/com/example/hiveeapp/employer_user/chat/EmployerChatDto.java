package com.example.hiveeapp.employer_user.chat;

public class EmployerChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private String jobTitle;

    public EmployerChatDto(int chatId, int employerId, int studentId, String jobTitle) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobTitle = jobTitle;
    }

    public int getChatId() { return chatId; }
    public int getEmployerId() { return employerId; }
    public int getStudentId() { return studentId; }
    public String getJobTitle() { return jobTitle; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}
