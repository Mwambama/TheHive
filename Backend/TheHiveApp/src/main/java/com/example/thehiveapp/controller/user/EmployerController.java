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

import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @Autowired private EmployerService employerService;

    public EmployerController(){}

    @GetMapping
    public List<Employer> getEmployers(@RequestParam(value = "companyId", required = false) Long companyId) {
        if (companyId != null) {
           return employerService.getEmployersByCompanyId(companyId);
        }
        return employerService.getEmployers();
    }

    @PostMapping
    public Employer createEmployer(@RequestBody Employer request) { return employerService.createEmployer(request); }

    @GetMapping("/{id}")
    public Employer getEmployerById(@PathVariable Long id) {
        return employerService.getEmployerById(id);
    }

    @PutMapping
    public Employer updateEmployer(@RequestBody Employer request) {
        return employerService.updateEmployer(request);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployer(@PathVariable Long id) {
        employerService.deleteEmployer(id);
        return "Account successfully deleted";
    }
}


