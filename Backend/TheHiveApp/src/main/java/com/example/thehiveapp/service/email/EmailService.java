package com.example.thehiveapp.service.email;

import com.example.thehiveapp.dto.email.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendEmailWithLogo(EmailDetails emailDetails);
}