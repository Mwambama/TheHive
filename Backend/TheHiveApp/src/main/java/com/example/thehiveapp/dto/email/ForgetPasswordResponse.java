package com.example.thehiveapp.dto.email;

import com.example.thehiveapp.dto.otp.OtpResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgetPasswordResponse {
    private int statusCode;
    private String responseMessage;
    private Long userId;
    private OtpResponse otpResponse;
}
