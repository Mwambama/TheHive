package com.example.thehiveapp.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@Table(name = "student")
public class Student extends User{
    @Column(name="university", nullable = false)
    private String university;

    @Column(name="graduation_date", nullable = false)
    private String graduationDate;

    @Column(name="gpa", nullable = false)
    private Double gpa;

    @Column(name="resume_path", nullable = false)
    private String resumePath;

}
