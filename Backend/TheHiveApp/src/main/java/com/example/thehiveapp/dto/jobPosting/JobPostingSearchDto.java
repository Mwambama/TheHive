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
    private String q; // Keyword search
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate minJobStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate maxJobStart;
    private Boolean isApplicationOpen;
    private Boolean isQualified;
    private Boolean hasNotAppliedTo;
}

