package com.example.thehiveapp.service.otp;

import com.example.thehiveapp.dto.email.EmailDetails;
import com.example.thehiveapp.dto.email.ForgetPasswordResponse;
import com.example.thehiveapp.dto.otp.OtpRequest;
import com.example.thehiveapp.dto.otp.OtpResponse;
import com.example.thehiveapp.dto.otp.OtpValidationRequest;
import com.example.thehiveapp.entity.otp.Otp;
import com.example.thehiveapp.repository.otp.OtpRepository;
import com.example.thehiveapp.service.email.EmailService;
import com.example.thehiveapp.utilities.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService{
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Override
    public void updateOtp(Otp otp){
        Otp existingOtp = otpRepository.findByEmail(otp.getEmail());
        existingOtp.setOtp(otp.getOtp());
        existingOtp.setEmail(otp.getEmail());
        existingOtp.setCreatedAt(LocalDateTime.now());
        existingOtp.setExpiresAt(otp.getExpiresAt());
        otpRepository.save(existingOtp);
    }

    @Override
    public ForgetPasswordResponse sendOtp(OtpRequest otpRequest){
        Otp existingOtp = otpRepository.findByEmail(otpRequest.getEmail());
        if (existingOtp != null){
            otpRepository.delete(existingOtp);
        }
        String otp = AppUtils.generateOtp();
        log.info("Otp: {}", otp);
        otpRepository.save(Otp.builder()
                        .email(otpRequest.getEmail())
                        .otp(otp)
                        .expiresAt(LocalDateTime.now().plusHours(1))
                .build());
        emailService.sendEmail(EmailDetails.builder()
                        .subject("The Hive Password Reset")
                        .recipient(otpRequest.getEmail())
                        .messageBody(AppUtils.FORGET_PASSWORD_BODY + otp)
                .build());
        return ForgetPasswordResponse.builder()
                .statusCode(200)
                .responseMessage("SUCCESS")
                .otpResponse(OtpResponse.builder()
                        .otp(otp)
                        .build())
                .build();
    }

    @Override
    public ForgetPasswordResponse validateOtp(OtpValidationRequest otpValidationRequest){
        Otp otp = otpRepository.findByEmail(otpValidationRequest.getEmail());
        log.info("Email: {}", otpValidationRequest.getEmail());
        if (otp == null){
            return ForgetPasswordResponse.builder()
                    .statusCode(400)
                    .responseMessage("You have not sent an otp!")
                    .build();
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())){
            return ForgetPasswordResponse.builder()
                    .statusCode(400)
                    .responseMessage("Expired otp!")
                    .build();
        }
        if (!otp.getOtp().equals(otpValidationRequest.getOtp())){
            return ForgetPasswordResponse.builder()
                    .statusCode(400)
                    .responseMessage("Invalid otp!")
                    .build();
        }
        return ForgetPasswordResponse.builder()
                .statusCode(200)
                .responseMessage("SUCCESS")
                .otpResponse(OtpResponse.builder()
                        .isOtpValid(true)
                        .build())
                .build();
    }
}
