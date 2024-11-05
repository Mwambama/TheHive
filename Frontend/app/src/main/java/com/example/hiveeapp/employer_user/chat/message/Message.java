package com.example.hiveeapp.employer_user.chat.message;

public class Message {
    private String text;
    private boolean isSentByUser;
    private int senderId;
    private int messageId;

    // Constructor accepting four parameters
    public Message(String text, boolean isSentByUser, int senderId, int messageId) {
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.senderId = senderId;
        this.messageId = messageId;
    }

    // Getters for each field
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
