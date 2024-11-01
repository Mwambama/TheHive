package com.example.hiveeapp.student_user.chat;

public class ChatDto {
    private Long chatId;
    private Long employerId;
    private Long studentId;

    public ChatDto(Long chatId, Long employerId, Long studentId) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getEmployerId() {
        return employerId;
    }

    public Long getStudentId() {
        return studentId;
    }
}

