package com.example.thehiveapp.entity.employerInvitation;

import com.example.thehiveapp.entity.user.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employer_invitation")
public class EmployerInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employer_invitation_id")
    private Long employerInvitationId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "email")
    private String email;

    public Long getEmployerInvitationId() {
        return employerInvitationId;
    }

    public void setEmployerInvitationId(Long employerInvitationId) {
        this.employerInvitationId = employerInvitationId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
