package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
