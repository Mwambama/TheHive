package com.example.thehiveapp.service.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.dto.jobPosting.JobPostingSearchDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.mapper.jobPosting.JobPostingMapper;
import com.example.thehiveapp.repository.jobPosting.JobPostingRepository;
import com.example.thehiveapp.specifications.jobPosting.JobPostingSpecification;
import com.example.thehiveapp.service.user.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService{

    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private JobPostingMapper mapper;
    @Autowired private EmployerService employerService;

    public JobPostingServiceImpl() {}

    @Override
    public List<JobPostingDto> getJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostingDto> getJobPostingsByEmployerId(Long employerId) {
        Employer employer = employerService.getEmployerById(employerId);
        return jobPostingRepository.findByEmployer(employer).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<JobPostingDto> getJobPostingsForStudent(Long studentId) {
        List<JobPosting> jobPostings = jobPostingRepository.findJobPostings(studentId);
        return jobPostings.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto dto) {
        JobPosting jobPosting = jobPostingRepository.save(mapper.dtoToEntity(dto));
        dto = mapper.entityToDto(jobPosting);
        return dto;
    }

    @Override
    public JobPostingDto getJobPostingById(Long id) {
        return mapper.entityToDto(
                jobPostingRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Job Posting not found with id " + id)
                )
        );
    }

    @Override
    public JobPostingDto updateJobPosting(JobPostingDto dto) {
        Long id = dto.getJobPostingId();
        jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        JobPosting jobPosting = mapper.dtoToEntity(dto);
        return mapper.entityToDto(jobPostingRepository.save(jobPosting));
    }

    @Override
    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        jobPostingRepository.delete(jobPosting);
    }

    @Override
    public List<JobPostingDto> searchJobPostings(JobPostingSearchDto searchDto) {
        Specification<JobPosting> spec = JobPostingSpecification.matchesOptionalFields(searchDto);
        return jobPostingRepository.findAll(spec).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }
}
