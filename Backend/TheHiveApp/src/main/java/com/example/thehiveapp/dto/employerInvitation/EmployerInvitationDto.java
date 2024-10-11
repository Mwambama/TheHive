package com.example.thehiveapp.dto.employerInvitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployerInvitationDto {
    private Long employerInvitationId;
    private String email;
    private Long companyId;
}
