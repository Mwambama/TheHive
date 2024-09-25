package com.example.thehiveapp.service.employerInvitation;

import com.example.thehiveapp.entity.employerInvitation.EmployerInvitation;
import com.example.thehiveapp.repository.employerInvitation.EmployerInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerInvitationServiceImpl implements EmployerInvitationService {

    @Autowired
    private final EmployerInvitationRepository employerInvitationRepository;

    public EmployerInvitationServiceImpl(EmployerInvitationRepository repository) {
        this.employerInvitationRepository = repository;
    }

    @Override
    public List<EmployerInvitation> getEmployerInvitations() {
        return employerInvitationRepository.findAll();
    }

    @Override
    public EmployerInvitation createEmployerInvitation(EmployerInvitation request) {
        return employerInvitationRepository.save(request);
    }

    @Override
    public EmployerInvitation getEmployerInvitationById(Long id) {
        return employerInvitationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employer Invitation not found with id " + id)
        );
    }

    @Override
    public EmployerInvitation updateEmployerInvitation(EmployerInvitation request) {
        Long id = request.getEmployerInvitationId();
        if (!employerInvitationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employer Invitation not found with id " + id);
        }
        return employerInvitationRepository.save(request);
    }

    @Override
    public void deleteEmployerInvitation(Long id) {
        EmployerInvitation employerInvitation = employerInvitationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employer Invitation not found with id " + id)
        );
        employerInvitationRepository.delete(employerInvitation);
    }
}
