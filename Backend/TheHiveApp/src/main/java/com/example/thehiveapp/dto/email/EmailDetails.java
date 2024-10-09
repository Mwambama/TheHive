package com.example.thehiveapp.dto.email;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String name;
    private String recipient;
    private String messageBody;
    private String subject;
}
