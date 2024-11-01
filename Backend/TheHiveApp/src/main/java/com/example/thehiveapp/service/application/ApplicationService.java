package com.example.thehiveapp.service.application;

import com.example.thehiveapp.dto.application.ApplicationDto;
import com.example.thehiveapp.dto.application.ApplicationRequest;
import com.example.thehiveapp.dto.application.ApplicationUpdateRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationDto getApplication(Long applicationId);
    void applyForJobPosting(ApplicationRequest request);
    List<ApplicationDto> getApplications();
    List<ApplicationDto> getApplicationsforStudent(Long studentId);
    ApplicationDto updateApplicationStatus(Long applicationId, ApplicationUpdateRequest applicationUpdateRequest);
    void deleteApplication(Long applicationId);
}
