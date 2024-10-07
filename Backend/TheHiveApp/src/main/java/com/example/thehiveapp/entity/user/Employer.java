package com.example.thehiveapp.entity.user;

import com.example.thehiveapp.entity.jobPosting.JobPosting;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="employer")
public class Employer extends User{
    @Column(name="company_id", nullable = false)
    private Long companyId;
    @Column(name="field", nullable = false)
    private String field;
    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobPosting> jobPostings;

    public Long getCompanyId() { return companyId; }

    public void setCompanyID(Long companyId) { this.companyId = companyId; }

    public String getField() { return field; }

    public void setField(String field) { this.field = field; }

    public List<JobPosting> getJobPostings() { return jobPostings; }

    public void setJobPostings(List<JobPosting> jobPostings) { this.jobPostings = jobPostings; }

}
