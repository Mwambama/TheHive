package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Employer;

import java.util.List;

public interface EmployerService {
    List<Employer> getEmployers();
    List<Employer> getEmployersByCompanyId(Long companyId);
    Employer createEmployer(Employer request);
    Employer getEmployerById(Long id);
    Employer updateEmployer(Employer request);
    void deleteEmployer(Long id);
}
