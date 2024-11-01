package com.example.thehiveapp.service.application;

import com.example.thehiveapp.dto.application.ApplicationDto;
import com.example.thehiveapp.dto.application.ApplicationRequest;
import com.example.thehiveapp.dto.application.ApplicationUpdateRequest;
import com.example.thehiveapp.entity.application.Application;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.jobPosting.JobPosting;
import com.example.thehiveapp.entity.user.Student;
import com.example.thehiveapp.enums.status.Status;
import com.example.thehiveapp.mapper.jobPosting.JobPostingMapper;
import com.example.thehiveapp.repository.application.ApplicationRepository;
import com.example.thehiveapp.repository.jobPosting.JobPostingRepository;
import com.example.thehiveapp.repository.user.StudentRepository;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.jobPosting.JobPostingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService{

    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private JobPostingRepository jobPostingRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private JobPostingService jobPostingService;
    @Autowired private JobPostingMapper jobPostingMapper;
    @Autowired private ChatService chatService;

    @Override
    public ApplicationDto getApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application cannot be found!"));
        return new ApplicationDto(
                application.getApplicationId(),
                application.getJobPosting().getJobPostingId(),
                application.getJobPosting().getTitle(),
                application.getStatus(),
                application.getAppliedOn()
        );
    }

    @Override
    public void applyForJobPosting(ApplicationRequest request){
        JobPosting jobPosting = jobPostingRepository.findById(request.getJobPostingId())
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        boolean alreadyApplied = applicationRepository.existsByStudentAndJobPosting(student, jobPosting);
        if (alreadyApplied) {
            throw new RuntimeException("Student has already applied for this job posting!");
        }
        Application application = new Application();
        application.setJobPosting(jobPosting);
        application.setStudent(student);
        application.setStatus(Status.PENDING);
        applicationRepository.save(application);
        log.info("Student Id: " + request.getStudentId() + " applied for Job Posting " + request.getJobPostingId());
    }
    @Override
    public List<ApplicationDto> getApplicationsforStudent(Long studentId){
        List<Application> applications = applicationRepository.findApplicationsByStudentId(studentId);
        return applications.stream().map(app -> new ApplicationDto(
                app.getApplicationId(),
                app.getJobPosting().getJobPostingId(),
                app.getJobPosting().getTitle(),
                app.getStatus(),
                app.getAppliedOn()
        )).collect(Collectors.toList());
    }
    @Override
    public List<ApplicationDto> getApplications(){
        return applicationRepository.findAll().stream()
                .map(app -> new ApplicationDto(
                app.getApplicationId(),
                app.getJobPosting().getJobPostingId(),
                app.getJobPosting().getTitle(),
                app.getStatus(),
                app.getAppliedOn()))
                .collect(Collectors.toList());
    }
    @Override
    public ApplicationDto updateApplicationStatus(Long applicationId, ApplicationUpdateRequest applicationUpdateRequest){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (applicationUpdateRequest.getStatus() != null) {
            application.setStatus(applicationUpdateRequest.getStatus());
        }
        Application updatedApplication = applicationRepository.save(application);
        return new ApplicationDto(
                updatedApplication.getApplicationId(),
                updatedApplication.getJobPosting().getJobPostingId(),
                updatedApplication.getJobPosting().getTitle(),
                updatedApplication.getStatus(),
                updatedApplication.getAppliedOn()
        );
    }
    @Override
    public void deleteApplication(Long applicationId){
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        applicationRepository.delete(application);
    }

    @Override
    public List<ApplicationDto> getApplicationsByJobPostingIdAndStatus(Long jobPostingId, Status status) {
        JobPosting jobPosting = jobPostingMapper.dtoToEntity(jobPostingService.getJobPostingById(jobPostingId));
        List<Application> applications;
        if (status == null) {
            applications = applicationRepository.findApplicationsByJobPosting(jobPosting);
        } else {
            applications = applicationRepository.findApplicationsByJobPostingAndStatus(jobPosting, status);
        }
        return applications.stream().map(
                application -> new ApplicationDto(
                        application.getApplicationId(),
                        application.getJobPosting().getJobPostingId(),
                        application.getJobPosting().getTitle(),
                        application.getStatus(),
                        application.getAppliedOn()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public void rejectApplication(Long applicationId) {
        this.updateApplicationStatus(applicationId, ApplicationUpdateRequest.builder().status(Status.REJECTED).build());
    }

    @Transactional
    @Override
    public void acceptApplication(Long applicationId) {
        this.updateApplicationStatus(applicationId, ApplicationUpdateRequest.builder().status(Status.ACCEPTED).build());
        Application application = applicationRepository.findApplicationByApplicationId(applicationId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found with id: " + applicationId)
        );
        Chat chat = Chat.builder()
                .studentId(application.getStudent().getUserId())
                .employerId(application.getJobPosting().getEmployer().getUserId())
                .build();
        chatService.createChat(chat);
    }
}
