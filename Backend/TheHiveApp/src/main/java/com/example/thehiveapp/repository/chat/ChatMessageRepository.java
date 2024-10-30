package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChat(Chat chat);
}
