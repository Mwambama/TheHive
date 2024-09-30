package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.repository.user.EmployerRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

public class EmployerServiceImpl implements EmployerService{
    private final EmployerRepository employerRepository;

    public EmployerServiceImpl(EmployerRepository employerRepository){ this.employerRepository = employerRepository; }

    @Override
    public List<Employer> getEmployers() { return employerRepository.findAll(); }

    @Override
    public Employer createEmployer(Employer request) { return employerRepository.save(request); }

    @Override
    public Employer getEmployerById(Long id) { return employerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employer not found with id " + id)); }

    @Override
    public Employer updateEmployer(Employer request) {
        Long id = request.getUserId();
        if (!employerRepository.existsById(id)){
            throw new ResourceNotFoundException("Employer not found with id " + id);
        }
        return employerRepository.save(request);
    }

    @Override
    public void deleteEmployer(Long id) {
        Employer employer = employerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employer not found with id " + id));
        employerRepository.delete(employer);
    }
}
