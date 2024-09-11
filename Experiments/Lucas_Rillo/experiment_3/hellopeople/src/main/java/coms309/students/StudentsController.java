package coms309.students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class StudentsController {

    private final StudentsService studentService;

    @Autowired
    public StudentsController(StudentsService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public HashMap<String, Student> getAllStudents() {return studentService.getAllStudents();}

    @PostMapping("/students")
    public String createStudent(@RequestBody Student student) {
        System.out.println(student);
        studentService.addStudent(student);
        return "New student " + student.getFirstName() + " saved";
    }

    @GetMapping("/students/{firstName}")
    public Student getStudent(@PathVariable String firstName) {
        return studentService.getStudent(firstName);
    }

    @PutMapping("/students/{firstName}")
    public Student updateStudent(@PathVariable String firstName, @RequestBody Student student) {
        studentService.updateStudent(firstName, student);
        return studentService.getStudent(firstName);
    }

    @DeleteMapping("/students/{firstName}")
    public HashMap<String, Student> deleteStudent(@PathVariable String firstName) {
        studentService.deleteStudent(firstName);
        return studentService.getAllStudents();
    }
}
