package com.example.hiveeapp.student_user.chat;

public class ChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private int jobPostingId;
    private String jobTitle;
    private String lastMessage;
    private String lastMessageTime;

    // Constructor with all parameters
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle, String lastMessage, String lastMessageTime) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    // Overloaded constructor for backward compatibility
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle) {
        this(chatId, employerId, studentId, jobPostingId, jobTitle, null, null);
    }

    // Getters and setters
    public int getChatId() { return chatId; }
    public int getEmployerId() { return employerId; }
    public int getStudentId() { return studentId; }
    public int getJobPostingId() { return jobPostingId; }
    public String getJobTitle() { return jobTitle; }

    public String getLastMessage() { return lastMessage; }
    public String getLastMessageTime() { return lastMessageTime; }

    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setJobPostingId(int jobPostingId) { this.jobPostingId = jobPostingId; }

    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }
}
