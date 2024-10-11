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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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
}
