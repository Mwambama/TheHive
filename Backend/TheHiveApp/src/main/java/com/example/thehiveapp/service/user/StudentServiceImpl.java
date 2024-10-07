package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.repository.user.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository){ this.studentRepository = studentRepository; }

    @Override
    public List<Student> getStudents() { return studentRepository.findAll(); }

    @Override
    public Student createStudent(Student request) { return studentRepository.save(request); }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Override
    public Student updateStudent(Student request) {
        Long id = request.getUserId();
        if (!studentRepository.existsById(id)){
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        return studentRepository.save(request);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id));
        studentRepository.delete(student);
    }
}
