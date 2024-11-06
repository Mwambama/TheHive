package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.User;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ChatService {
    List<Chat> getChats();
    String getOtherUserEmail(Long chatId, Long userId);
    Chat createChat(Chat request);
    Chat getChatById(Long id);
    Chat updateChat(Chat request);
    void deleteChat(Long id);
    List<Long> getChatIdsByUser(User user) throws BadRequestException;
}
