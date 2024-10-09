package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.repository.user.StudentRepository;
import com.example.thehiveapp.service.address.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired private StudentRepository studentRepository;
    @Autowired private AddressService addressService;

    public StudentServiceImpl(){}

    @Override
    public List<Student> getStudents() { return studentRepository.findAll(); }

    @Override
    public Student createStudent(Student request) { return studentRepository.save(request); }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    @Transactional
    @Override
    public Student updateStudent(Student request) {
        Long id = request.getUserId();
        if (!studentRepository.existsById(id)){
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        addressService.updateAddress(request.getAddress());
        return studentRepository.save(request);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id));
        studentRepository.delete(student);
    }
}
