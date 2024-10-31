package com.example.thehiveapp.dto.application;

import com.example.thehiveapp.enums.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUpdateRequest {
    private Status status;
}
