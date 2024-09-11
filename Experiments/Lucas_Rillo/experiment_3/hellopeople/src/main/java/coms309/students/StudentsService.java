package coms309.students;

import coms309.people.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class StudentsService {

    private final HashMap<String, Student> studentList = new HashMap<>();
    private final PeopleService peopleService;

    @Autowired
    public StudentsService(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    public HashMap<String, Student> getAllStudents() {
        return studentList;
    }

    public void addStudent(Student student) {
        studentList.put(student.getFirstName(), student);
        peopleService.addPerson(student);
    }

    public Student getStudent(String firstName) {
        return studentList.get(firstName);
    }

    public void updateStudent(String firstName, Student student) {
        studentList.replace(firstName, student);
        peopleService.updatePerson(firstName, student);
    }

    public void deleteStudent(String firstName) {
        studentList.remove(firstName);
        peopleService.deletePerson(firstName);
    }
}
