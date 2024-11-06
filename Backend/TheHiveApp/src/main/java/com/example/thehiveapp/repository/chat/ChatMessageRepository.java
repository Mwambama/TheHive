package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChat(Chat chat);
    List<ChatMessage> findByUserAndSeenFalse(User user);
    List<ChatMessage> findByChatAndUserNotAndSeenFalse(Chat chat, User user);
}
