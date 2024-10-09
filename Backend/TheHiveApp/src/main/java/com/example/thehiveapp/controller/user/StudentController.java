package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.service.user.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.thehiveapp.entity.user.Student;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired private StudentService studentService;

    public StudentController(){}

    @GetMapping
    public List<Student> getStudents() { return studentService.getStudents(); }

    @PostMapping
    public Student createStudent(@RequestBody Student request) { return studentService.createStudent(request); }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student request) {
        return studentService.updateStudent(request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Student account successfully deleted";
    }
}
