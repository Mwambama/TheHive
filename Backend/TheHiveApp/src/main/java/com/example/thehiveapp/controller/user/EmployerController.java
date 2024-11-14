package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.service.user.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @Autowired private EmployerService employerService;

    public EmployerController(){}

    @Operation(summary = "Retrieve all employers", description = "Fetches a list of all registered employers. Optionally, filter by company ID.")
    @GetMapping
    public List<Employer> getEmployers(@RequestParam(value = "companyId", required = false) Long companyId) {
        if (companyId != null) {
            return employerService.getEmployersByCompanyId(companyId);
        }
        return employerService.getEmployers();
    }

    @Operation(summary = "Create a new employer", description = "Registers a new employer with the provided request.")
    @PostMapping
    public Employer createEmployer(@RequestBody Employer request) {
        return employerService.createEmployer(request);
    }

    @Operation(summary = "Get employer by ID", description = "Retrieves employer information based on the employer's unique ID.")
    @GetMapping("/{id}")
    public Employer getEmployerById(@PathVariable Long id) {
        return employerService.getEmployerById(id);
    }

    @Operation(summary = "Update employer details", description = "Updates the details of an existing employer based on the provided request.")
    @PutMapping
    public Employer updateEmployer(@RequestBody Employer request) {
        return employerService.updateEmployer(request);
    }

    @Operation(summary = "Delete an employer", description = "Deletes an employer's record using their unique ID.")
    @DeleteMapping("/{id}")
    public String deleteEmployer(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return "Employer account successfully deleted";
    }
}
