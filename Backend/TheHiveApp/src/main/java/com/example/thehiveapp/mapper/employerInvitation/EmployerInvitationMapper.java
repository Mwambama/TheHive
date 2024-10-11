package com.example.thehiveapp.mapper.employerInvitation;

import com.example.thehiveapp.dto.employerInvitation.EmployerInvitationDto;
import com.example.thehiveapp.entity.employerInvitation.EmployerInvitation;
import com.example.thehiveapp.service.user.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployerInvitationMapper {

    @Autowired
    private CompanyService companyService;

    public EmployerInvitationDto toDto(EmployerInvitation entity) {
        if (entity == null) {
            return null;
        }
        EmployerInvitationDto dto = new EmployerInvitationDto();
        dto.setEmployerInvitationId(entity.getEmployerInvitationId());
        dto.setCompanyId(entity.getCompany().getUserId());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public EmployerInvitation toEntity(EmployerInvitationDto dto) {
        if (dto == null) {
            return null;
        }
        EmployerInvitation entity = new EmployerInvitation();
        entity.setEmployerInvitationId(dto.getEmployerInvitationId());
        entity.setCompany(companyService.getCompanyById(dto.getCompanyId()));
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
