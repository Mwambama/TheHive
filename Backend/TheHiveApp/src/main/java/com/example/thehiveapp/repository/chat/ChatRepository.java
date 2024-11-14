package com.example.thehiveapp.repository.chat;

import com.example.thehiveapp.entity.chat.Chat;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Operation(
            summary = "Find all chats by employer ID",
            description = "Retrieves a list of all chats associated with a specific employer."
    )
    List<Chat> findAllByEmployerId(Long employerId);
    @Operation(
            summary = "Find all chats by student ID",
            description = "Retrieves a list of all chats associated with a specific student."
    )
    List<Chat> findAllByStudentId(Long studentId);
}
