package com.example.hiveeapp.company_user.invitations;

public class Invitation {
    private int id;
    private int companyId;
    private String email;
    private String message;

    public Invitation(int id, int companyId, String email, String message) {
        this.id = id;
        this.companyId = companyId;
        this.email = email;
        this.message = message;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}