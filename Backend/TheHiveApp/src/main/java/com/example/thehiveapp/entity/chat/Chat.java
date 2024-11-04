package com.example.thehiveapp.entity.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "employer_id", nullable = false)
    private Long employerId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name= "job_posting_id", nullable = false)
    private Long jobPostingId;

}
