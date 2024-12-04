package com.example.thehiveapp.controller.chat;

import com.example.thehiveapp.dto.jobPosting.JobPostingDto;
import com.example.thehiveapp.entity.chat.Chat;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.user.UserService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired private ChatService chatService;
    @Autowired private UserService userService;

    public ChatController() {}

    @Operation(summary = "Get all chats or filter by user ID", description = "Retrieves a list of all chat records or filters them by user ID if provided.")
    @GetMapping
    public List<Chat> getChats(@RequestParam(value = "userId", required = false) Long userId) throws BadRequestException {
        if (userId != null) {
            return chatService.getChatsByUserId(userId);
        }
        return chatService.getChats();
    }

    @Operation(summary = "Create a new chat", description = "Creates a new chat with the provided chat request.")
    @PostMapping
    public Chat createChat(@RequestBody Chat request) {
        return chatService.createChat(request);
    }

    @Operation(summary = "Get chat by ID", description = "Retrieves a chat record by its unique ID.")
    @GetMapping("/{id}")
    public Chat getChatById(@PathVariable Long id) {
        return chatService.getChatById(id);
    }

    @Operation(summary = "Update an existing chat", description = "Updates an existing chat with the provided request.")
    @PutMapping
    public Chat updateChat(@RequestBody Chat request) {
        return chatService.updateChat(request);
    }

    @Operation(summary = "Delete a chat by ID", description = "Deletes a chat record by its unique ID.")
    @DeleteMapping("/{id}")
    public String deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
        return "Chat successfully deleted";
    }
}
