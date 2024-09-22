package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Company;

import java.util.List;

public interface CompanyService {
    List<Company> getCompanies();
    Company createCompany(Company request);
    Company getCompanyById(Long id);
    Company updateCompany(Company request);
    void deleteCompany(Long id);
}
