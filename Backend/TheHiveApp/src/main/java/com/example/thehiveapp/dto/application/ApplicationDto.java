package com.example.thehiveapp.dto.application;

import com.example.thehiveapp.enums.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDto {
    private Long applicationId;
    private Long jobPostingId;
    private Long studentId;
    private String jobTitle;
    private Status status;
    private LocalDateTime appliedOn;
}
