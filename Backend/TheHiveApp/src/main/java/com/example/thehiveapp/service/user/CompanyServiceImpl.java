package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.repository.user.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyRepository companyRepository;
    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company request) {
        return companyRepository.save(request);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id " + id)
        );
    }

    public Company updateCompany(Company request) {
        Long id = request.getId();
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id " + id);
        }
        return companyRepository.save(request);
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id " + id)
        );
        companyRepository.delete(company);
    }
}
