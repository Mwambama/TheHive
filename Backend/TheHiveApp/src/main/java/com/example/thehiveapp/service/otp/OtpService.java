package com.example.thehiveapp.service.otp;

import com.example.thehiveapp.dto.email.ForgetPasswordResponse;
import com.example.thehiveapp.dto.otp.OtpRequest;
import com.example.thehiveapp.dto.otp.OtpValidationRequest;
import com.example.thehiveapp.entity.otp.Otp;

public interface OtpService {
    void updateOtp(Otp otp);
    ForgetPasswordResponse sendOtp(OtpRequest otpRequest);
    ForgetPasswordResponse validateOtp(OtpValidationRequest otpValidationRequest);
}
