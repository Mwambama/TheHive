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

@RestController
@RequestMapping("/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/sendOtp")
    public ForgetPasswordResponse sendOtp(@RequestBody OtpRequest otpRequest){
        return otpService.sendOtp(otpRequest);
    }

    @PostMapping("/validateOtp")
    public ForgetPasswordResponse validateOtp(@RequestBody OtpValidationRequest otpValidationRequest){
        return otpService.validateOtp(otpValidationRequest);
    }
}
