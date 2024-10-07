package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
