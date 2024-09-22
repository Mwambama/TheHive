package com.example.hiveeapp.company_user.invitations;

public class Invitation {
    private int id;
    private int companyId;
    private String email;

    public Invitation(int id, int companyId, String email) {
        this.id = id;
        this.companyId = companyId;
        this.email = email;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

