package com.example.thehiveapp.dto.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StudentSignUpRequest extends BaseSignUpRequest{
    private String university;
}
