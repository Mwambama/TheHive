package com.example.thehiveapp.mapper.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.service.user.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobPostingMapper {

    @Autowired
    private EmployerService employerService;

    public JobPostingDto entityToDto(JobPosting entity) {
        if (entity == null) {
            return null;
        }
        JobPostingDto dto = new JobPostingDto();
        dto.setJobPostingId(entity.getJobPostingId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setSummary(entity.getSummary());
        dto.setSalary(entity.getSalary());
        dto.setJobType(entity.getJobType());
        dto.setMinimumGpa(entity.getMinimumGpa());
        dto.setJobStart(entity.getJobStart());
        dto.setApplicationStart(entity.getApplicationStart());
        dto.setApplicationEnd(entity.getApplicationEnd());
        dto.setEmployerId(entity.getEmployer().getUserId()); // Assuming Employer has getEmployerId() method

        return dto;
    }

    public JobPosting dtoToEntity(JobPostingDto dto) {
        if (dto == null) {
            return null;
        }
        JobPosting entity = new JobPosting();
        entity.setJobPostingId(dto.getJobPostingId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setSummary(dto.getSummary());
        entity.setSalary(dto.getSalary());
        entity.setJobType(dto.getJobType());
        entity.setMinimumGpa(dto.getMinimumGpa());
        entity.setJobStart(dto.getJobStart());
        entity.setApplicationStart(dto.getApplicationStart());
        entity.setApplicationEnd(dto.getApplicationEnd());
        entity.setEmployer(employerService.getEmployerById(dto.getEmployerId()));

        return entity;
    }
}
