package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.service.user.StudentService;
import com.example.thehiveapp.entity.user.Student;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired private StudentService studentService;

    public StudentController(){}

    @Operation(summary = "Retrieve all students", description = "Fetches a list of all registered students.")
    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @Operation(summary = "Create a new student", description = "Registers a new student with the provided request.")
    @PostMapping
    public Student createStudent(@RequestBody Student request) {
        return studentService.createStudent(request);
    }

    @Operation(summary = "Upload student resume", description = "Uploads a resume for the specified student.")
    @PostMapping("/{id}/upload-resume")
    public ResponseEntity<String> uploadStudentResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        studentService.uploadStudentResume(id, file);
        return ResponseEntity.ok("Resume uploaded successfully!");
    }

    @Operation(summary = "Get student by ID", description = "Retrieves a student's details based on their unique ID.")
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @Operation(summary = "Get student resume", description = "Retrieves the resume of a student as a PDF based on their unique ID.")
    @GetMapping(value = "/{id}/resume", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getStudentResumeById(@PathVariable Long id){
        return studentService.getStudentResume(id);
    }

    @Operation(summary = "Update student details", description = "Updates an existing student's details based on the provided request.")
    @PutMapping
    public Student updateStudent(@RequestBody Student request) {
        return studentService.updateStudent(request);
    }

    @Operation(summary = "Delete student by ID", description = "Deletes a student's record using their unique ID.")
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Student account successfully deleted";
    }
}
