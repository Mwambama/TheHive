package com.example.hiveeapp.student_user.search;

public class JobPosting {
    private String title;
    private String description;
    private double salary;
    private String jobStart;
    private String applicationStart;
    private String applicationEnd;
    private String summary;

    // Constructor to match the parameters being passed
    public JobPosting(String title, String description, double salary,
                      String jobStart, String applicationStart, String applicationEnd, String summary) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.jobStart = jobStart;
        this.applicationStart = applicationStart;
        this.applicationEnd = applicationEnd;
        this.summary = summary;
    }

    // Getters and setters for all fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getJobStart() {
        return jobStart;
    }

    public void setJobStart(String jobStart) {
        this.jobStart = jobStart;
    }

    public String getApplicationStart() {
        return applicationStart;
    }

    public void setApplicationStart(String applicationStart) {
        this.applicationStart = applicationStart;
    }

    public String getApplicationEnd() {
        return applicationEnd;
    }

    public void setApplicationEnd(String applicationEnd) {
        this.applicationEnd = applicationEnd;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}