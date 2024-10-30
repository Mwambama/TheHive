package com.example.thehiveapp.dto.application;

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
    private String jobTitle;
    private String status;
    private LocalDateTime appliedOn;
}
