package com.example.hiveeapp.student_user.chat.message;

public class Message {
    private String text;
    private boolean isSentByUser;
    private int senderId;
    private int messageId;

    // Constructor with four parameters
    public Message(String text, boolean isSentByUser, int senderId, int messageId) {
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.senderId = senderId;
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getMessageId() {
        return messageId;
    }
}
