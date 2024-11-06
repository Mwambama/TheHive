package com.example.thehiveapp.service.chat;

import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.entity.user.User;
import com.example.thehiveapp.repository.chat.ChatRepository;
import com.example.thehiveapp.service.user.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired private ChatRepository chatRepository;
    @Autowired private UserService userService;

    public ChatServiceImpl() {}

    public List<Chat> getChats() {
        return chatRepository.findAll();
    }

    public Chat createChat(Chat chat) {
        return chatRepository.save(chat);
    }

    public Chat getChatById(Long id) {
        return chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
    }

    public String getOtherUserEmail(Long chatId, Long userId){
        Chat chat = getChatById(chatId);
        // if student
        if (chat.getStudentId().equals(userId)){
            // send employer email
            User user = userService.getUserById(chat.getEmployerId());
            return user.getEmail();
        } else {
            // its employer, so send student
            User user = userService.getUserById(chat.getStudentId());
            return user.getEmail();
        }
    }
    public Chat updateChat(Chat chat) {
        Long id = chat.getChatId();
        chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
        return chatRepository.save(chat);
    }

    public void deleteChat(Long id) {
        Chat chat = chatRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat not found with id " + id)
        );
        chatRepository.delete(chat);
    }

    @Override
    public List<Long> getChatIdsByUser(User user) throws BadRequestException {
        return switch (user.getRole()) {
            case EMPLOYER -> chatRepository.findAllByEmployerId(
                    user.getUserId()
            ).stream().map(Chat::getChatId).collect(Collectors.toList());
            case STUDENT -> chatRepository.findAllByStudentId(
                        user.getUserId()
                ).stream().map(Chat::getChatId).toList();
            default -> throw new BadRequestException("Invalid role: must be STUDENT or EMPLOYER");
        };
    }

}
