package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.chat.ChatMessage;
import com.example.thehiveapp.repository.chat.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl() {}

    @Override
    public List<ChatMessage> getChatMessages() {
        return chatMessageRepository.findAll();
    }

    @Override
    public ChatMessage createChatMessage(ChatMessage request) {
        return chatMessageRepository.save(request);
    }

    @Override
    public ChatMessage getChatMessageById(Long id) {
        return chatMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ChatMessage not found with id " + id)
        );
    }

    @Override
    public ChatMessage updateChatMessage(ChatMessage request) {
        Long id = request.getMessageId();
        if (!chatMessageRepository.existsById(id)) {
            throw new ResourceNotFoundException("ChatMessage not found with id " + id);
        }
        return chatMessageRepository.save(request);
    }

    @Override
    public void deleteChatMessage(Long id) {
        ChatMessage chatMessage = chatMessageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ChatMessage not found with id " + id)
        );
        chatMessageRepository.delete(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatMessagesByChat(Chat chat) {
        return chatMessageRepository.findAllByChat(chat);
    }
}