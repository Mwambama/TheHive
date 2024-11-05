package com.example.thehiveapp.mapper.chat;

import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    @Autowired private UserService userService;
    @Autowired private ChatService chatService;

    public ChatMessageDto toDto(ChatMessage entity) {
        if (entity == null) {
            return null;
        }
        ChatMessageDto dto = new ChatMessageDto();
        dto.setMessageId(entity.getMessageId());
        dto.setMessage(entity.getMessage());
        dto.setUserId(entity.getUser().getUserId());
        dto.setChatId(entity.getChat().getChatId());
        dto.setTimestamp(entity.getTimestamp());
        dto.setReplyToId(entity.getReplyTo().getMessageId());
        return dto;
    }

    public ChatMessage toEntity(ChatMessageDto dto) {
        if (dto == null) {
            return null;
        }
        ChatMessage entity = new ChatMessage();
        entity.setMessageId(dto.getMessageId());
        entity.setMessage(dto.getMessage());
        entity.setUser(userService.getUserById(dto.getUserId()));
        entity.setChat(chatService.getChatById(dto.getChatId()));
        entity.setTimestamp(dto.getTimestamp());
        return entity;
    }
}
