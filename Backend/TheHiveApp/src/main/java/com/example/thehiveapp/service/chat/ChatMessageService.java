package com.example.thehiveapp.service.chat;


import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> getChatMessages();
    ChatMessage createChatMessage(ChatMessage request);
    ChatMessage getChatMessageById(Long id);
    ChatMessage updateChatMessage(ChatMessage request);
    void deleteChatMessage(Long id);
    List<ChatMessage> getChatMessagesByChat(Chat chat);
}
