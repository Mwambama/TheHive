package com.example.thehiveapp.repository.user;

import com.example.thehiveapp.entity.user.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    List<Employer> findByCompanyId(Long companyId);
}
