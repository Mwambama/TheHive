package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.entity.user.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Operation(
            summary = "Find all messages by chat",
            description = "Retrieves a list of all messages within a specified chat."
    )
    List<ChatMessage> findAllByChat(Chat chat);
    @Operation(
            summary = "Find unseen messages by user",
            description = "Fetches all messages sent to a user that have not been marked as seen."
    )
    List<ChatMessage> findByUserAndSeenFalse(User user);
    @Operation(
            summary = "Find unseen messages in a chat by other users",
            description = "Retrieves unseen messages in a specified chat that were sent by other users."
    )
    List<ChatMessage> findByChatAndUserNotAndSeenFalse(Chat chat, User user);
}
