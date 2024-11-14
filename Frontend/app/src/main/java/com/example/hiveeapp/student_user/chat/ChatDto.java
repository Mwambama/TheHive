package com.example.hiveeapp.student_user.chat;

/**
 * Data Transfer Object (DTO) for Chat, encapsulating details of a chat session,
 * including chat ID, employer ID, student ID, job posting ID, job title, last message,
 * last message time, and student name.
 */
public class ChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private int jobPostingId;
    private String jobTitle;
    private String lastMessage;
    private String lastMessageTime;
    private String studentName;

    /**
     * Constructs a ChatDto with all parameters.
     *
     * @param chatId          The ID of the chat.
     * @param employerId      The ID of the employer.
     * @param studentId       The ID of the student.
     * @param jobPostingId    The ID of the job posting associated with the chat.
     * @param jobTitle        The title of the job associated with the chat.
     * @param lastMessage     The last message sent in the chat.
     * @param lastMessageTime The timestamp of the last message sent in the chat.
     */
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle, String lastMessage, String lastMessageTime) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobPostingId = jobPostingId;
        this.jobTitle = jobTitle;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * Constructs a ChatDto with basic parameters for backward compatibility.
     *
     * @param chatId       The ID of the chat.
     * @param employerId   The ID of the employer.
     * @param studentId    The ID of the student.
     * @param jobPostingId The ID of the job posting associated with the chat.
     * @param jobTitle     The title of the job associated with the chat.
     */
    public ChatDto(int chatId, int employerId, int studentId, int jobPostingId, String jobTitle) {
        this(chatId, employerId, studentId, jobPostingId, jobTitle, null, null);
    }

    /**
     * Gets the chat ID.
     *
     * @return The chat ID.
     */
    public int getChatId() {
        return chatId;
    }

    /**
     * Gets the employer ID.
     *
     * @return The employer ID.
     */
    public int getEmployerId() {
        return employerId;
    }

    /**
     * Gets the student ID.
     *
     * @return The student ID.
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * Gets the job posting ID.
     *
     * @return The job posting ID.
     */
    public int getJobPostingId() {
        return jobPostingId;
    }

    /**
     * Gets the job title.
     *
     * @return The job title.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the job title.
     *
     * @param jobTitle The job title to set.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Sets the job posting ID.
     *
     * @param jobPostingId The job posting ID to set.
     */
    public void setJobPostingId(int jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    /**
     * Gets the last message sent in the chat.
     *
     * @return The last message.
     */
    public String getLastMessage() { return lastMessage; }

    /**
     * Sets the last message sent in the chat.
     *
     * @param lastMessage The last message to set.
     */
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }

    /**
     * Gets the timestamp of the last message sent in the chat.
     *
     * @return The last message timestamp.
     */
    public String getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * Sets the timestamp of the last message sent in the chat.
     *
     * @param lastMessageTime The last message timestamp to set.
     */
    public void setLastMessageTime(String lastMessageTime) { this.lastMessageTime = lastMessageTime; }


    /**
     * Gets the name of the student in the chat.
     *
     * @return The student name.
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the name of the student in the chat.
     *
     * @param studentName The student name to set.
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
