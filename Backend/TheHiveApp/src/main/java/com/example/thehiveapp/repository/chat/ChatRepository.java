package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByEmployerId(Long employerId);
    List<Chat> findAllByStudentId(Long studentId);
}
