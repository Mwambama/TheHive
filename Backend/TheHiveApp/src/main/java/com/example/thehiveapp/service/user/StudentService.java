package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student createStudent(Student request);
    Student getStudentById(Long id);
    Student updateStudent(Student request);
    void deleteStudent(Long id);
}
