package com.example.hiveeapp.websocket.message;

public class Message {
    private String text;
    private boolean isSentByUser;
    private int senderId;
    private int messageId;
    private long timestamp;

    // Constructor with timestamp (for student messages)
    public Message(String text, boolean isSentByUser, int senderId, int messageId, long timestamp) {
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.senderId = senderId;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    // Constructor without timestamp (for employer messages)
    public Message(String text, boolean isSentByUser, int senderId, int messageId) {
        this(text, isSentByUser, senderId, messageId, System.currentTimeMillis());
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

    public long getTimestamp() {
        return timestamp;
    }
}
