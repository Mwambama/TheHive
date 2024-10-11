package com.example.thehiveapp.service.employerInvitation;


import com.example.thehiveapp.dto.employerInvitation.EmployerInvitationDto;

import java.util.List;

public interface EmployerInvitationService {
    List<EmployerInvitationDto> getEmployerInvitations();
    EmployerInvitationDto createEmployerInvitation(EmployerInvitationDto request);
    EmployerInvitationDto getEmployerInvitationById(Long id);
    EmployerInvitationDto updateEmployerInvitation(EmployerInvitationDto request);
    void deleteEmployerInvitation(Long id);
}
