package com.example.thehiveapp.service.employerInvitation;


import com.example.thehiveapp.entity.employerInvitation.EmployerInvitation;

import java.util.List;

public interface EmployerInvitationService {
    List<EmployerInvitation> getEmployerInvitations();
    EmployerInvitation createEmployerInvitation(EmployerInvitation request);
    EmployerInvitation getEmployerInvitationById(Long id);
    EmployerInvitation updateEmployerInvitation(EmployerInvitation request);
    void deleteEmployerInvitation(Long id);
}
