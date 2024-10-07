package com.example.thehiveapp.dto.authentication;

public class StudentSignUpRequest extends BaseSignUpRequest{

    private String university;

    public StudentSignUpRequest() {}

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
