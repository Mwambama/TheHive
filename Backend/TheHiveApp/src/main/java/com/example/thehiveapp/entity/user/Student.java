package com.example.thehiveapp.entity.user;

import com.example.thehiveapp.entity.application.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Table(name = "student")
public class Student extends User{
    @Column(name="university", nullable = false)
    private String university;

    @Column(name="graduation_date")
    private String graduationDate;

    @Column(name="gpa")
    private Double gpa;

    @Column(name="resume_path")
    private String resumePath;

    @Column(name = "applications_made_today")
    private int applicationsMadeToday = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;
}
