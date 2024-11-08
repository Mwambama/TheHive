package com.example.hiveeapp.employer_user.chat;

public class EmployerChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private String jobTitle;
    private String lastMessage;
    private String lastMessageTime;
    private String studentName;
    // Constructors
    public EmployerChatDto(int chatId, int employerId, int studentId, String jobTitle, String lastMessage, String lastMessageTime) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobTitle = jobTitle;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public EmployerChatDto(int chatId, int employerId, int studentId, String jobTitle) {
        this(chatId, employerId, studentId, jobTitle, null, null);
    }

    // Existing getters and setters
    public int getChatId() { return chatId; }
    public int getEmployerId() { return employerId; }
    public int getStudentId() { return studentId; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    public String getLastMessageTime() { return lastMessageTime; }
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }

    // New getter and setter for studentName
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
