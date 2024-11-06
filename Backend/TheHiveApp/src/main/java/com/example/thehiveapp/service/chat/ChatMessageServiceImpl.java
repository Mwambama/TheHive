package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.mapper.chat.ChatMessageMapper;
import com.example.thehiveapp.repository.chat.ChatMessageRepository;
import com.example.thehiveapp.repository.chat.ChatRepository;
import com.example.thehiveapp.repository.user.UserRepository;
import com.example.thehiveapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired private ChatMessageRepository chatMessageRepository;
    @Autowired private ChatService chatService;
    @Autowired private UserService userService;
    @Autowired private ChatMessageMapper chatMessageMapper;

    public ChatMessageServiceImpl() {}

    @Override
    public List<ChatMessageDto> getChatMessages() {
        return chatMessageRepository.findAll().stream()
                .map(chatMessageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getUnreadChatMessagesByUserId(Long userId){
        User user = userService.getUserById(userId);
        List<ChatMessage> unreadMessages = chatMessageRepository.findByUserAndSeenFalse(user);
        return unreadMessages.stream()
                .map(chatMessageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markMessagesAsSeen(Long chatId, Long userId){
        Chat chat = chatService.getChatById(chatId);
        User user = userService.getUserById(userId);
        List<ChatMessage> unreadMessages = chatMessageRepository.findByChatAndUserNotAndSeenFalse(chat, user);
        for (ChatMessage message : unreadMessages) {
            message.setSeen(true);
        }
        chatMessageRepository.saveAll(unreadMessages);
    }

    @Override
    public ChatMessageDto createChatMessage(ChatMessageDto dto) {
        ChatMessage chatMessage = chatMessageMapper.toEntity(dto);
        if (dto.getReplyToId() != null){
            ChatMessage repliedMessage = chatMessageRepository.findById(dto.getReplyToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Message being replied to not found"));
            chatMessage.setReplyTo(repliedMessage);
        }
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(savedMessage);
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
        Chat chat = chatService.getChatById(chatId);
        return chatMessageRepository.findAllByChat(chat).stream().map(
                chatMessageMapper::toDto).collect(Collectors.toList()
        );
    }
}