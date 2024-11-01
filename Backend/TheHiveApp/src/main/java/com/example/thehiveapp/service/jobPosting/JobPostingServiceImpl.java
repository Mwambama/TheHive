package com.example.thehiveapp.service.jobPosting;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Employer;
import com.example.thehiveapp.mapper.jobPosting.JobPostingMapper;
import com.example.thehiveapp.repository.jobPosting.JobPostingRepository;
import com.example.thehiveapp.service.user.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService{

    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private JobPostingMapper jobPostingMapper;
    @Autowired private EmployerService employerService;

    public JobPostingServiceImpl() {}

    public List<JobPostingDto> getJobPostings() {
        return jobPostingRepository.findAll().stream()
                .map(jobPostingMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostingDto> getJobPostingsByEmployerId(Long employerId) {
        Employer employer = employerService.getEmployerById(employerId);
        return jobPostingRepository.findByEmployer(employer).stream()
                .map(jobPostingMapper::entityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<JobPostingDto> getJobPostingsForStudent(Long studentId) {
        List<JobPosting> jobPostings = jobPostingRepository.findJobPostings(studentId);
        return jobPostings.stream()
                .map(jobPostingMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public JobPostingDto createJobPosting(JobPostingDto dto) {
        JobPosting jobPosting = jobPostingRepository.save(jobPostingMapper.dtoToEntity(dto));
        dto = jobPostingMapper.entityToDto(jobPosting);
        return dto;
    }

    public JobPostingDto getJobPostingById(Long id) {
        return jobPostingMapper.entityToDto(
                jobPostingRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Job Posting not found with id " + id)
                )
        );
    }

    public JobPostingDto updateJobPosting(JobPostingDto dto) {
        Long id = dto.getJobPostingId();
        jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        JobPosting jobPosting = jobPostingMapper.dtoToEntity(dto);
        return jobPostingMapper.entityToDto(jobPostingRepository.save(jobPosting));
    }

    public void deleteJobPosting(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Job Posting not found with id " + id)
        );
        jobPostingRepository.delete(jobPosting);
    }
}
