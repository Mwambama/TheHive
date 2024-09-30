package com.example.hiveeapp.company_user.handleEmployers;

public class Employer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;

    // Constructor
    public Employer(int id, String name, String email, String phone, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}

