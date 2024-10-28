package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.repository.chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    private ChatRepository chatRepository;

    public ChatServiceImpl() {}

    @Override
    public List<Chat> getChats() {
        return chatRepository.findAll();
    }

    @Override
    public Chat createChat(Chat request) {
        return chatRepository.save(request);
    }

    @Override
    public Chat getChatById(Long id) {
        return chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
    }

    @Override
    public Chat updateChat(Chat request) {
        Long id = request.getChatId();
        if (!chatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Chat not found with id " + id);
        }
        return chatRepository.save(request);
    }

    @Override
    public void deleteChat(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
        chatRepository.delete(chat);
    }

}
