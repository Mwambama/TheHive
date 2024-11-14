package com.example.hiveeapp.websocket.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Represents a message in a chat session, containing details such as the message text,
 * sender information, timestamp, reply ID, and seen status.
 */
public class Message {
    private String text;
    private boolean isSentByUser;
    private int senderId;
    private int messageId;
    private int chatId;
    private long timestamp;
    private Integer replyToId;
    private boolean seen;

    /**
     * Constructs a Message object with all fields.
     *
     * @param text        The content of the message.
     * @param isSentByUser Indicates if the message was sent by the current user.
     * @param senderId    The ID of the user who sent the message.
     * @param messageId   The unique ID of the message.
     * @param chatId      The ID of the chat session.
     * @param timestamp   The timestamp of when the message was sent (in milliseconds).
     * @param replyToId   The ID of the message this message is replying to (optional).
     * @param seen        Indicates if the message has been seen by the recipient.
     */
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

    /**
     * Constructs a Message object with basic fields.
     *
     * @param text        The content of the message.
     * @param isSentByUser Indicates if the message was sent by the current user.
     * @param senderId    The ID of the user who sent the message.
     * @param messageId   The unique ID of the message.
     * @param chatId      The ID of the chat session.
     */
    public Message(String text, boolean isSentByUser, int senderId, int messageId, int chatId) {
        this(text, isSentByUser, senderId, messageId, chatId, System.currentTimeMillis(), null, false);
    }

    /**
     * Gets the content of the message.
     *
     * @return The message text.
     */
    public String getText() {
        return text;
    }

    /**
     * Checks if the message was sent by the current user.
     *
     * @return True if sent by the current user, otherwise false.
     */
    public boolean isSentByUser() {
        return isSentByUser;
    }

    /**
     * Gets the ID of the user who sent the message.
     *
     * @return The sender's ID.
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * Gets the unique ID of the message.
     *
     * @return The message ID.
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Gets the ID of the chat session.
     *
     * @return The chat ID.
     */
    public int getChatId() {
        return chatId;
    }

    /**
     * Gets the timestamp of when the message was sent.
     *
     * @return The timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the ID of the message this message is replying to.
     *
     * @return The reply-to message ID, or null if not a reply.
     */
    public Integer getReplyToId() {
        return replyToId;
    }

    /**
     * Checks if the message has been seen by the recipient.
     *
     * @return True if the message has been seen, otherwise false.
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Sets the timestamp of the message based on a timestamp string.
     *
     * @param timestampStr The timestamp string in "yyyy-MM-dd HH:mm:ss" format.
     */
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