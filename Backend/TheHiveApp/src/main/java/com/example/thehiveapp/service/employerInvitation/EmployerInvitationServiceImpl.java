package com.example.thehiveapp.service.employerInvitation;

import com.example.thehiveapp.dto.employerInvitation.EmployerInvitationDto;
import com.example.thehiveapp.entity.employerInvitation.EmployerInvitation;
import com.example.thehiveapp.mapper.employerInvitation.EmployerInvitationMapper;
import com.example.thehiveapp.repository.employerInvitation.EmployerInvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerInvitationServiceImpl implements EmployerInvitationService {

    @Autowired private EmployerInvitationRepository employerInvitationRepository;
    @Autowired private EmployerInvitationMapper mapper;

    public EmployerInvitationServiceImpl() {}

    @Override
    public List<EmployerInvitationDto> getEmployerInvitations() {
        return employerInvitationRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @Override
    public EmployerInvitationDto createEmployerInvitation(EmployerInvitationDto request) {
        EmployerInvitation employerInvitation = employerInvitationRepository.save(mapper.toEntity(request));
        return mapper.toDto(employerInvitation);
    }

    @Override
    public EmployerInvitationDto getEmployerInvitationById(Long id) {
        return mapper.toDto(
                employerInvitationRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Employer Invitation not found with id " + id)
                )
        );
    }

    @Override
    public EmployerInvitationDto updateEmployerInvitation(EmployerInvitationDto request) {
        Long id = request.getEmployerInvitationId();
        if (!employerInvitationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employer Invitation not found with id " + id);
        }
        EmployerInvitation employerInvitation = employerInvitationRepository.save(mapper.toEntity(request));
        return mapper.toDto(employerInvitation);
    }

    @Override
    public void deleteEmployerInvitation(Long id) {
        EmployerInvitation employerInvitation = employerInvitationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Employer Invitation not found with id " + id)
        );
        employerInvitationRepository.delete(employerInvitation);
    }
}
