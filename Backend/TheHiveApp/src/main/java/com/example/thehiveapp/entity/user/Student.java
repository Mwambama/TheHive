package com.example.thehiveapp.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "student")
public class Student extends User{

    @Column(name="university", nullable = false)
    private String university;
    @Column(name="graduationDate", nullable = false)
    private String graduationDate;
    @Column(name="GPA", nullable = false)
    private double GPA;
    @Column(name="resumePath", nullable = false)
    private String resumePath;

    public String getUniversity(){ return this.university; }
    public void setUniversity(String university){this.university = university;}
    public String getGraduationDate(){return this.graduationDate;}
    public void setGraduationDate(String graduationDate){this.graduationDate = graduationDate;}
    public double getGPA(){ return this.GPA; }
    public void setGPA(double GPA){ this.GPA = GPA; }
    public String getResumePath(){ return this.resumePath; }
    public void setResumePath(String resumePath){ this.resumePath = resumePath; }

}
