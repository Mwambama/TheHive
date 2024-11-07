package com.example.hiveeapp.websocket.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private String text;
    private boolean isSentByUser;
    private int senderId;
    private int messageId;
    private int chatId;
    private long timestamp;
    private Integer replyToId;
    private boolean seen;

    // Constructor for messages from server with all fields
    public Message(String text, boolean isSentByUser, int senderId, int messageId, int chatId, long timestamp, Integer replyToId, boolean seen) {
        this.text = text;
        this.isSentByUser = isSentByUser;
        this.senderId = senderId;
        this.messageId = messageId;
        this.chatId = chatId;
        this.timestamp = timestamp;
        this.replyToId = replyToId;
        this.seen = seen;
    }

    public Message(String text, boolean isSentByUser, int senderId, int messageId, int chatId) {
        this(text, isSentByUser, senderId, messageId, chatId, System.currentTimeMillis(), null, false);
    }

    // Getters for each field
    public String getText() { return text; }
    public boolean isSentByUser() { return isSentByUser; }
    public int getSenderId() { return senderId; }
    public int getMessageId() { return messageId; }
    public int getChatId() { return chatId; }
    public long getTimestamp() { return timestamp; }
    public Integer getReplyToId() { return replyToId; }
    public boolean isSeen() { return seen; }

    // Method to set the timestamp if received as a String
    public void setTimestampFromString(String timestampStr) {
        // Parse timestamp string and handle exceptions
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestampStr);
            if (date != null) {
                this.timestamp = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            this.timestamp = System.currentTimeMillis();
        }
    }
}