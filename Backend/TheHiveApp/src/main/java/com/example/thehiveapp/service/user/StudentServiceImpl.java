package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.repository.user.StudentRepository;
import com.example.thehiveapp.service.address.AddressService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService{

    @Autowired private StudentRepository studentRepository;
    @Autowired private AddressService addressService;
    private static String directory = "/var/www/thehive/resumes/"; // when running through server

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
        if (request.getAddress() != null){
            if (request.getAddress().getAddressId() == null) {
                addressService.createAddress(request.getAddress());
            } else {
                addressService.updateAddress(request.getAddress());
            }
        }
        return studentRepository.save(request);
    }

    @Override
    public void trackApplication(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));
        student.setApplicationsMadeToday(student.getApplicationsMadeToday() + 1);
        studentRepository.save(student);
    }

    @Override
    public void resetDailyApplications() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            student.setApplicationsMadeToday(0);
        });
        studentRepository.saveAll(students);
    }

    @Override
    public void uploadStudentResume(Long id, MultipartFile file) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id));
        try {
            String contentType = file.getContentType();
            if (!MediaType.APPLICATION_PDF_VALUE.equals(contentType)) {
                throw new RuntimeException("Only PDF files are allowed");
            }
            File destinationFile = new File(directory  + File.separator + file.getOriginalFilename());
            log.info("Resume destination: " + destinationFile);
            file.transferTo(destinationFile);
            student.setResumePath(destinationFile.getAbsolutePath());
            studentRepository.save(student);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading resume: " + e.getMessage());
        }
    }

    @Override
    public byte[] getStudentResume(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id));
        try {
            if (student.getResumePath() == null){
                throw new RuntimeException("Student has no resume on file");
            }
            File resumeFile = new File(student.getResumePath());
            return Files.readAllBytes(resumeFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error opening resume: " + e.getMessage());
        }
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id " + id));
        studentRepository.delete(student);
    }
}
