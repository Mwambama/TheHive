package com.example.thehiveapp.dto.authentication;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class EmployerSignUpRequest extends BaseSignUpRequest {
    @NotNull
    private Long companyId;
}
