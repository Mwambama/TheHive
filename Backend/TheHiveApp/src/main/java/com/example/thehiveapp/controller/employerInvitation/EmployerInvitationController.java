package com.example.thehiveapp.controller.employerInvitation;

import com.example.thehiveapp.dto.employerInvitation.EmployerInvitationDto;
import com.example.thehiveapp.service.employerInvitation.EmployerInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employer-invitation")
public class EmployerInvitationController {

    @Autowired private EmployerInvitationService employerInvitationService;


    public EmployerInvitationController() {}

    @GetMapping
    public List<EmployerInvitationDto> getEmployerInvitations() {
        return employerInvitationService.getEmployerInvitations();
    }

    @PostMapping
    public EmployerInvitationDto createEmployerInvitation(@RequestBody EmployerInvitationDto request) {
        return employerInvitationService.createEmployerInvitation(request);
    }

    @GetMapping("/{id}")
    public EmployerInvitationDto getEmployerInvitationById(@PathVariable Long id) {
        return employerInvitationService.getEmployerInvitationById(id);
    }

    @PutMapping
    public EmployerInvitationDto updateEmployerInvitation(@RequestBody EmployerInvitationDto request) {
        return employerInvitationService.updateEmployerInvitation(request);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployerInvitation(@PathVariable Long id) {
        employerInvitationService.deleteEmployerInvitation(id);
    }
}

