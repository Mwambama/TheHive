//package com.example.thehiveapp.mapper;
//
//import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
//import com.example.thehiveapp.entity.jobPosting.JobPosting;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface JobPostingMapper {
//
//    @Mapping(target = "employerId", source = "employer.userId")
//    JobPostingDto entityToDto(JobPosting entity);
//
//    @Mapping(target = "employer.userId", source="employerId")
//    JobPosting dtoToEntity(JobPostingDto dto);
//}
