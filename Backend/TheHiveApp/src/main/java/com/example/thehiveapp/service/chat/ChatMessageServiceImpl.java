package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.dto.chat.ChatDto;
import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.mapper.chat.ChatMapper;
import com.example.thehiveapp.mapper.chat.ChatMessageMapper;
import com.example.thehiveapp.repository.chat.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ChatService chatService;
    @Autowired private ChatMessageMapper chatMessageMapper;
    @Autowired private ChatMapper chatMapper;

    public ChatMessageServiceImpl() {}

    @Override
    public List<ChatMessageDto> getChatMessages() {
        return chatMessageRepository.findAll().stream()
                .map(chatMessageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChatMessageDto createChatMessage(ChatMessageDto dto) {
        ChatMessage ChatMessage = chatMessageRepository.save(chatMessageMapper.toEntity(dto));
        dto = chatMessageMapper.toDto(ChatMessage);
        return dto;
    }

    @Override
    public ChatMessageDto getChatMessageById(Long id) {
        return chatMessageMapper.toDto(
                chatMessageRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Chat Message not found with id " + id)
                )
        );
    }

    @Override
    public ChatMessageDto updateChatMessage(ChatMessageDto dto) {
        Long id = dto.getMessageId();
        chatMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat Message not found with id " + id)
        );
        ChatMessage ChatMessage = chatMessageMapper.toEntity(dto);
        return chatMessageMapper.toDto(chatMessageRepository.save(ChatMessage));
    }

    @Override
    public void deleteChatMessage(Long id) {
        ChatMessage ChatMessage = chatMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat Message not found with id " + id)
        );
        chatMessageRepository.delete(ChatMessage);
    }

    @Override
    public List<ChatMessageDto> getChatMessagesByChatId(Long chatId) {
        ChatDto chat = chatService.getChatById(chatId);
        return chatMessageRepository.findAllByChat(chatMapper.toEntity(chat)).stream().map(
                chatMessageMapper::toDto).collect(Collectors.toList()
        );
    }
}