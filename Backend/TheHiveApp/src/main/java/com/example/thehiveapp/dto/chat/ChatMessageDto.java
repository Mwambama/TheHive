package com.example.thehiveapp.dto.chat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {
    private Long messageId;

    @NotNull
    private Long chatId;

    @NotNull
    private String message;

    @NotNull
    private Long userId;

    @NotNull
    private String timestamp;

    @NotNull
    private Long replyToId;

    @NotNull
    private Boolean seen;
}
