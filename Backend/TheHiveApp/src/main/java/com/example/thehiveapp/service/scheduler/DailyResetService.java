package com.example.thehiveapp.service.scheduler;

import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.repository.user.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DailyResetService {

    @Autowired
    private StudentRepository studentRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight daily
    public void resetDailyApplications() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            student.setApplicationsMadeToday(0);
        });
        studentRepository.saveAll(students);
    }
}
