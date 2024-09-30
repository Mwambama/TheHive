package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
}
