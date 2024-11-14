package com.example.hiveeapp.employer_user.chat;

/**
 * Data Transfer Object (DTO) for Employer Chat, encapsulating chat details including
 * chat ID, employer ID, student ID, job title, last message, last message time, and student name.
 */
public class EmployerChatDto {
    private int chatId;
    private int employerId;
    private int studentId;
    private String jobTitle;
    private String lastMessage;
    private String lastMessageTime;
    private String studentName;

    /**
     * Constructs an EmployerChatDto with full chat details.
     *
     * @param chatId          The ID of the chat.
     * @param employerId      The ID of the employer.
     * @param studentId       The ID of the student.
     * @param jobTitle        The title of the job associated with the chat.
     * @param lastMessage     The last message sent in the chat.
     * @param lastMessageTime The timestamp of the last message.
     */
    public EmployerChatDto(int chatId, int employerId, int studentId, String jobTitle, String lastMessage, String lastMessageTime) {
        this.chatId = chatId;
        this.employerId = employerId;
        this.studentId = studentId;
        this.jobTitle = jobTitle;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    /**
     * Constructs an EmployerChatDto with basic chat details.
     *
     * @param chatId     The ID of the chat.
     * @param employerId The ID of the employer.
     * @param studentId  The ID of the student.
     * @param jobTitle   The title of the job associated with the chat.
     */
    public EmployerChatDto(int chatId, int employerId, int studentId, String jobTitle) {
        this(chatId, employerId, studentId, jobTitle, null, null);
    }

    /**
     * Gets the chat ID.
     *
     * @return The chat ID.
     */
    public int getChatId() { return chatId; }

    /**
     * Gets the employer ID.
     *
     * @return The employer ID.
     */
    public int getEmployerId() { return employerId; }

    /**
     * Gets the student ID.
     *
     * @return The student ID.
     */
    public int getStudentId() { return studentId; }

    /**
     * Gets the job title associated with the chat.
     *
     * @return The job title.
     */
    public String getJobTitle() { return jobTitle; }

    /**
     * Sets the job title associated with the chat.
     *
     * @param jobTitle The job title to set.
     */
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

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
    public String getLastMessageTime() { return lastMessageTime; }

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
    public String getStudentName() { return studentName; }

    /**
     * Sets the name of the student in the chat.
     *
     * @param studentName The student name to set.
     */
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
