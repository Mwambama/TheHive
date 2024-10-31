package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.dto.chat.ChatDto;
import com.example.thehiveapp.entity.user.User;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ChatService {
    List<ChatDto> getChats();
    ChatDto createChat(ChatDto request);
    ChatDto getChatById(Long id);
    ChatDto updateChat(ChatDto request);
    void deleteChat(Long id);
    List<ChatDto> getChatsByUser(User user) throws BadRequestException;
}
