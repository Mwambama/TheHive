package com.example.thehiveapp.controller.chat;


import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired private ChatService chatService;

    public ChatController() {}

    @GetMapping
    public List<Chat> getChats() {
        return chatService.getChats();
    }

    @PostMapping
    public Chat createChat(@RequestBody Chat request) {
        return chatService.createChat(request);
    }

    @GetMapping("/{id}")
    public Chat getChatById(@PathVariable Long id) {
        return chatService.getChatById(id);
    }

    @PutMapping
    public Chat updateChat(@RequestBody Chat request) {
        return chatService.updateChat(request);
    }

    @DeleteMapping("/{id}")
    public String deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return "Chat successfully deleted";
    }
}

