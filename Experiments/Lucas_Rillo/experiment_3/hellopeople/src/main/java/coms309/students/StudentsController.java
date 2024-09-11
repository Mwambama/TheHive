package coms309.students;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class StudentsController {

    HashMap<String, Student> studentList = new HashMap<>();

    @GetMapping("/students")
    public HashMap<String, Student> getAllStudents() {return studentList;}

    @PostMapping("/students")
    public String createStudent(@RequestBody Student student) {
        System.out.println(student);
        studentList.put(student.getFirstName(), student);
        return "New student " + student.getFirstName() + " saved";
    }

    @GetMapping("/students/{firstName}")
    public Student getStudent(@PathVariable String firstName) {
        return studentList.get(firstName);
    }

    @PutMapping("/students/{firstName}")
    public Student updateStudent(@PathVariable String firstName, @RequestBody Student student) {
        studentList.replace(firstName, student);
        return studentList.get(firstName);
    }

    @DeleteMapping("/students/{firstName}")
    public HashMap<String, Student> deleteStudent(@PathVariable String firstName) {
        studentList.remove(firstName);
        return studentList;
    }
}
