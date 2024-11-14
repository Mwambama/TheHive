package com.example.thehiveapp.repository.otp;

import com.example.thehiveapp.entity.otp.Otp;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    @Operation(
            summary = "Find OTP by email",
            description = "Retrieves the OTP associated with a given email address."
    )
    Otp findByEmail(String email);
}
