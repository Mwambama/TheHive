package com.example.thehiveapp.dto.jobPosting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingSearchDto {
    private String q;
    private String title;
    private String description;
    private String summary;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private BigDecimal minGpa;
    private BigDecimal maxGpa;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minJobStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxJobStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minApplicationStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxApplicationStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minApplicationEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxApplicationEnd;
    private Long employerId;
}

