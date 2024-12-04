package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Student;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();
    Student createStudent(Student request);
    Student getStudentById(Long id);
    Student updateStudent(Student request);
    void trackApplication(Long studentId);
    void uploadStudentResume(Long id, MultipartFile file);
    byte[] getStudentResume(Long id);
    void deleteStudent(Long id);
}
