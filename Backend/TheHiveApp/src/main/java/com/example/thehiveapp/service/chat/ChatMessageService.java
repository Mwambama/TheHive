package com.example.thehiveapp.service.chat;


import com.example.thehiveapp.dto.chat.ChatMessageDto;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessageDto> getChatMessages();
    List<ChatMessageDto> getUnreadChatMessagesByUserId(Long userId);
    ChatMessageDto createChatMessage(ChatMessageDto request);
    ChatMessageDto getChatMessageById(Long id);
    ChatMessageDto updateChatMessage(ChatMessageDto request);
    void deleteChatMessage(Long id);
    List<ChatMessageDto> getChatMessagesByChatId(Long chatId);
}
