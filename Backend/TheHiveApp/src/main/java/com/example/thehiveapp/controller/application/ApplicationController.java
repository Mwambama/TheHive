package com.example.thehiveapp.controller.application;

import com.example.thehiveapp.dto.application.ApplicationDto;
import com.example.thehiveapp.dto.application.ApplicationRequest;
import com.example.thehiveapp.dto.application.ApplicationUpdateRequest;
import com.example.thehiveapp.enums.status.Status;
import com.example.thehiveapp.service.application.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired private ApplicationService applicationService;

    public ApplicationController() {}

    @Operation(
            summary = "Submit an application for a job posting",
            description = "Allows a student to submit an application for a specific job posting."
    )
    @PostMapping("/apply")
    public ResponseEntity<String> applyForJobPosting(@RequestBody ApplicationRequest request) {
        applicationService.applyForJobPosting(request);
        return ResponseEntity.ok("Application submitted successfully!");
    }

    @Operation(
            summary = "Get applications for a student or all applications",
            description = "Retrieves all applications submitted by a specific student or all applications if no student ID is provided."
    )
    @GetMapping("/student")
    public ResponseEntity<List<ApplicationDto>> getApplications(@RequestParam(value = "studentId", required = false) Long studentId){
        if (studentId != null){
            return ResponseEntity.ok(applicationService.getApplicationsforStudent(studentId));
        }
        return ResponseEntity.ok(applicationService.getApplications());
    }

    @Operation(
            summary = "Get an application by its ID",
            description = "Retrieves the details of a specific application using its ID."
    )
    @GetMapping("/{applicationId}")
    public ApplicationDto getApplication(@PathVariable Long applicationId){
        return applicationService.getApplication(applicationId);
    }

    @Operation(
            summary = "Update the status of an application",
            description = "Allows updating the status of an application, such as 'ACCEPTED' or 'REJECTED'."
    )
    @PutMapping("/update/{applicationId}")
    public ResponseEntity<ApplicationDto> updateApplicationStatus(@PathVariable Long applicationId, @RequestBody ApplicationUpdateRequest applicationUpdateRequest) {
        return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, applicationUpdateRequest));
    }

    @Operation(
            summary = "Delete an application by ID",
            description = "Deletes a specific application using its ID."
    )
    @DeleteMapping("/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return "Application successfully deleted";
    }

    @Operation(
            summary = "Get applications filtered by job posting and status",
            description = "Retrieves applications filtered by a specific job posting and status (ACCEPTED, PENDING, REJECTED)."
    )
    @GetMapping
    public List<ApplicationDto> getApplicationsByJobPostingAndStatus(
            @RequestParam(value = "jobPostingId", required = false) Long jobPostingId,
            @RequestParam(value = "status", required = false) Status status) {
        return applicationService.getApplicationsByJobPostingIdAndStatus(jobPostingId, status);
    }

    @Operation(
            summary = "Reject an application",
            description = "Allows rejecting an application by changing its status to 'REJECTED'."
    )
    @PostMapping("/{id}/reject")
    public String rejectApplication(@PathVariable Long id) {
        applicationService.rejectApplication(id);
        return "Application successfully rejected";
    }

    @Operation(
            summary = "Accept an application and create a chat",
            description = "Accepts an application and creates a new chat for communication between the student and employer."
    )
    @PostMapping("/{id}/accept")
    public String acceptApplication(@PathVariable Long id) {
        applicationService.acceptApplication(id);
        return "Application successfully accepted and chat created";
    }
}
