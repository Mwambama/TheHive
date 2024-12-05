package com.example.thehiveapp.config;

import com.example.thehiveapp.service.user.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private StudentService studentService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight daily
    public void scheduleDailyReset() {
        studentService.resetDailyApplications();
    }
}
