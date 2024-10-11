package com.example.thehiveapp.service.otp;

import com.example.thehiveapp.dto.email.ForgetPasswordResponse;
import com.example.thehiveapp.dto.otp.OtpRequest;
import com.example.thehiveapp.dto.otp.OtpValidationRequest;

public interface OtpService {
    public ForgetPasswordResponse sendOtp(OtpRequest otpRequest);
    public ForgetPasswordResponse validateOtp(OtpValidationRequest otpValidationRequest);
}
