package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.repository.user.EmployerRepository;
import com.example.thehiveapp.service.address.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerServiceImpl implements EmployerService{

    @Autowired private EmployerRepository employerRepository;
    @Autowired private AddressService addressService;

    public EmployerServiceImpl(){}

    @Override
    public List<Employer> getEmployers() { return employerRepository.findAll(); }

    @Override
    public List<Employer> getEmployersByCompanyId(Long companyId) {
        return employerRepository.findByCompanyId(companyId);
    }

    @Override
    public Employer createEmployer(Employer request) { return employerRepository.save(request); }

    @Override
    public Employer getEmployerById(Long id) { return employerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employer not found with id " + id)); }

    @Transactional
    @Override
    public Employer updateEmployer(Employer request) {
        Long id = request.getUserId();
        if (!employerRepository.existsById(id)){
            throw new ResourceNotFoundException("Employer not found with id " + id);
        }
        addressService.updateAddress(request.getAddress());
        return employerRepository.save(request);
    }

    @Override
    public void deleteEmployer(Long id) {
        Employer employer = employerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employer not found with id " + id));
        employerRepository.delete(employer);
    }
}
