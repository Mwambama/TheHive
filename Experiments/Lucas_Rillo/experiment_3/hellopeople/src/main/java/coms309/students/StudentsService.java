package coms309.students;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class StudentsService {

    private final HashMap<String, Student> studentList = new HashMap<>();

    public HashMap<String, Student> getAllStudents() {
        return studentList;
    }

    public void addStudent(Student student) {
        studentList.put(student.getFirstName(), student);
    }

    public Student getStudent(String firstName) {
        return studentList.get(firstName);
    }

    public void updateStudent(String firstName, Student student) {
        studentList.replace(firstName, student);
    }

    public void deleteStudent(String firstName) {
        studentList.remove(firstName);
    }
}
