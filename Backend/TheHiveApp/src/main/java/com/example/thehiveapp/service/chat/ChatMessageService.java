package com.example.thehiveapp.service.chat;


import com.example.thehiveapp.dto.chat.ChatDto;
import com.example.thehiveapp.dto.chat.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageDto> getChatMessages();
    ChatMessageDto createChatMessage(ChatMessageDto request);
    ChatMessageDto getChatMessageById(Long id);
    ChatMessageDto updateChatMessage(ChatMessageDto request);
    void deleteChatMessage(Long id);
    List<ChatMessageDto> getChatMessagesByChat(ChatDto chat);
}
