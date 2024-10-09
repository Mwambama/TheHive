package com.example.thehiveapp.service.user;

import com.example.thehiveapp.entity.user.Company;
import com.example.thehiveapp.repository.user.CompanyRepository;
import com.example.thehiveapp.service.address.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private AddressService addressService;

    public CompanyServiceImpl() {}

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

    @Transactional
    public Company updateCompany(Company request) {
        Long id = request.getUserId();
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id " + id);
        }
        addressService.updateAddress(request.getAddress());
        return companyRepository.save(request);
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with id " + id)
        );
        companyRepository.delete(company);
    }
}
