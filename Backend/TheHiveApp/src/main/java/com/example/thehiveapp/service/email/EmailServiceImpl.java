package com.example.thehiveapp.service.email;

import com.example.thehiveapp.dto.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    public void sendEmail(EmailDetails emailDetails){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailSender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            log.info("Message sent to {}", emailDetails.getRecipient());
            log.info("Message sender: {}", emailSender);
        } catch (MailException e){
            throw new RuntimeException(e);
        }
    }
    public void sendEmailWithLogo(EmailDetails emailDetails){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            String name = "<h3>Dear " + emailDetails.getName() + ", </h3>";
            String htmlMsg = "<img src='cid:logoImage' alt='The Hive Logo'/>" +
                    name + "<p>" + emailDetails.getMessageBody() + "</p>";
            helper.setText(htmlMsg, true);
            ClassPathResource logo = new ClassPathResource("images/logo.png");
            helper.addInline("logoImage", logo);
            javaMailSender.send(message);
        } catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
