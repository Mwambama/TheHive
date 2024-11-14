package com.example.thehiveapp.repository.otp;

import com.example.thehiveapp.entity.otp.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByEmail(String email);
}
