package com.example.thehiveapp.controller.user;

import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.service.user.CompanyService;
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
@RequestMapping("/company")
public class CompanyController {

    @Autowired private CompanyService companyService;

    public CompanyController() {}

    @Operation(summary = "Retrieve all companies", description = "Fetches a list of all registered companies.")
    @GetMapping
    public List<Company> getCompanies() {
        return companyService.getCompanies();
    }

    @Operation(summary = "Create a new company", description = "Registers a new company using the provided request.")
    @PostMapping
    public Company createCompany(@RequestBody Company request) {
        return companyService.createCompany(request);
    }

    @Operation(summary = "Get company by ID", description = "Retrieves a company's details based on its unique ID.")
    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }

    @Operation(summary = "Update company details", description = "Updates the information of an existing company based on the provided request.")
    @PutMapping
    public Company updateCompany(@RequestBody Company request) {
        return companyService.updateCompany(request);
    }

    @Operation(summary = "Delete a company", description = "Deletes a company's record from the database using its unique ID.")
    @DeleteMapping("/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return "Company account successfully deleted";
    }
}
