package com.example.thehiveapp.dto.authentication;


import jakarta.validation.constraints.NotNull;

public class EmployerSignUpRequest extends BaseSignUpRequest {

    @NotNull
    private Long companyId;

    public EmployerSignUpRequest() {}

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
