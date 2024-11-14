package com.example.thehiveapp.controller.otp;

import com.example.thehiveapp.dto.email.ForgetPasswordResponse;
import com.example.thehiveapp.dto.otp.OtpRequest;
import com.example.thehiveapp.dto.otp.OtpValidationRequest;
import com.example.thehiveapp.service.otp.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @Operation(summary = "Send OTP to the user for password reset", description = "Sends a One-Time Password (OTP) to the user's registered email address to initiate the password reset process.")
    @PostMapping("/sendOtp")
    public ForgetPasswordResponse sendOtp(@RequestBody OtpRequest otpRequest){
        return otpService.sendOtp(otpRequest);
    }

    @Operation(summary = "Validate the OTP for password reset", description = "Validates the OTP entered by the user to verify their identity before allowing them to reset their password.")
    @PostMapping("/validateOtp")
    public ForgetPasswordResponse validateOtp(@RequestBody OtpValidationRequest otpValidationRequest){
        return otpService.validateOtp(otpValidationRequest);
    }
}
