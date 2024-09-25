package com.example.thehiveapp.mapper.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobPostingMapper {

    @Mapping(target = "companyId", source = "company.userId")
    JobPostingDto entityToDto(JobPosting entity);

    @Mapping(target = "company.userId", source="companyId")
    JobPosting dtoToEntity(JobPostingDto dto);
}
