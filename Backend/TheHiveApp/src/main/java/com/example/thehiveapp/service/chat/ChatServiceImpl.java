package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.dto.chat.ChatDto;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.mapper.chat.ChatMapper;
import com.example.thehiveapp.repository.chat.ChatRepository;
import com.example.thehiveapp.repository.user.EmployerRepository;
import com.example.thehiveapp.repository.user.StudentRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired private ChatRepository chatRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private ChatMapper chatMapper;

    public ChatServiceImpl() {}

    public List<ChatDto> getChats() {
        return chatRepository.findAll().stream()
                .map(chatMapper::toDto)
                .collect(Collectors.toList());
    }

    public ChatDto createChat(ChatDto dto) {
        Chat chat = chatRepository.save(chatMapper.toEntity(dto));
        dto = chatMapper.toDto(chat);
        return dto;
    }

    public ChatDto getChatById(Long id) {
        return chatMapper.toDto(
                chatRepository.findById(id).orElseThrow(
                        () -> new ResourceNotFoundException("Chat not found with id " + id)
                )
        );
    }

    public ChatDto updateChat(ChatDto dto) {
        Long id = dto.getChatId();
        chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
        Chat chat = chatMapper.toEntity(dto);
        return chatMapper.toDto(chatRepository.save(chat));
    }

    public void deleteChat(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
        chatRepository.delete(chat);
    }

    @Override
    public List<ChatDto> getChatsByUser(User user) throws BadRequestException {
        return switch (user.getRole()) {
            case EMPLOYER -> chatRepository.findAllByEmployer(
                    employerRepository.findByUserId(user.getUserId()).orElseThrow(
                            () -> new ResourceNotFoundException("Employer not found with id " + user.getUserId())
                    )
            ).stream().map(chatMapper::toDto).collect(Collectors.toList());
            case STUDENT -> chatRepository.findAllByStudent(
                    studentRepository.findByUserId(user.getUserId()).orElseThrow(
                            () -> new ResourceNotFoundException("Student not found with id " + user.getUserId())
                    )
            ).stream().map(chatMapper::toDto).collect(Collectors.toList());
            default -> throw new BadRequestException("Invalid role: must be STUDENT or EMPLOYER");
        };
    }

}
