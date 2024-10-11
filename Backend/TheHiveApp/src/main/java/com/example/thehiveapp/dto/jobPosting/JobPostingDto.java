package com.example.thehiveapp.dto.jobPosting;

import com.example.thehiveapp.enums.jobPosting.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingDto {
    private Long jobPostingId;
    private String title;
    private String description;
    private String summary;
    private BigDecimal salary;
    private JobType jobType;
    private BigDecimal minimumGpa;
    private LocalDate jobStart;
    private LocalDate applicationStart;
    private LocalDate applicationEnd;
    private Long employerId;
}
