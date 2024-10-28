package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.authentication.Authentication;
import com.example.thehiveapp.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
