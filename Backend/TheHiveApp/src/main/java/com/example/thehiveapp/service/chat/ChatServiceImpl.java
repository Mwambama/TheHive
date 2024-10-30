package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.repository.chat.ChatRepository;
import com.example.thehiveapp.repository.user.EmployerRepository;
import com.example.thehiveapp.repository.user.StudentRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired private ChatRepository chatRepository;
    @Autowired private EmployerRepository employerRepository;
    @Autowired private StudentRepository studentRepository;

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

    @Override
    public List<Chat> getChatsByUser(User user) throws BadRequestException {
        return switch (user.getRole()) {
            case EMPLOYER -> chatRepository.findAllByEmployer(
                    employerRepository.findByUserId(user.getUserId()).orElseThrow(
                            () -> new ResourceNotFoundException("Employer not found with id " + user.getUserId())
                    )
            );
            case STUDENT -> chatRepository.findAllByStudent(
                    studentRepository.findByUserId(user.getUserId()).orElseThrow(
                            () -> new ResourceNotFoundException("Student not found with id " + user.getUserId())
                    )
            );
            default -> throw new BadRequestException("Invalid role: must be STUDENT or EMPLOYER");
        };
    }

}
