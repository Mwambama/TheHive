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
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/employer-invitation")
public class EmployerInvitationController {

    @Autowired private EmployerInvitationService employerInvitationService;

    public EmployerInvitationController() {}

    @Operation(summary = "Get all employer invitations", description = "Retrieves a list of all employer invitations.")
    @GetMapping
    public List<EmployerInvitationDto> getEmployerInvitations() {
        return employerInvitationService.getEmployerInvitations();
    }

    @Operation(summary = "Create a new employer invitation", description = "Creates a new employer invitation with the provided request.")
    @PostMapping
    public EmployerInvitationDto createEmployerInvitation(@RequestBody EmployerInvitationDto request) {
        return employerInvitationService.createEmployerInvitation(request);
    }

    @Operation(summary = "Get employer invitation by ID", description = "Retrieves an employer invitation by its unique ID.")
    @GetMapping("/{id}")
    public EmployerInvitationDto getEmployerInvitationById(@PathVariable Long id) {
        return employerInvitationService.getEmployerInvitationById(id);
    }

    @Operation(summary = "Update an existing employer invitation", description = "Updates an existing employer invitation with the provided request.")
    @PutMapping
    public EmployerInvitationDto updateEmployerInvitation(@RequestBody EmployerInvitationDto request) {
        return employerInvitationService.updateEmployerInvitation(request);
    }

    @Operation(summary = "Delete an employer invitation by ID", description = "Deletes an employer invitation by its unique ID.")
    @DeleteMapping("/{id}")
    public String deleteEmployerInvitation(@PathVariable Long id) {
        employerInvitationService.deleteEmployerInvitation(id);
        return "Invitation successfully deleted";
    }
}
