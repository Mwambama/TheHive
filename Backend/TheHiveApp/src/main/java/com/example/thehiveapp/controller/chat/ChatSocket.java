package com.example.thehiveapp.controller.chat;

import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.chat.ChatMessageService;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/ws/chat/{chatId}")
@Component
public class ChatSocket {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private static ChatMessageService chatMessageService;
    private static ChatService chatService;
    private static UserService userService;
    private static AuthenticationService authenticationService;

    // Maps each chatId to its map of session-email pairs
    private static Map<Long, Map<Session, String>> chatRooms = new ConcurrentHashMap<>();
    private static Map<Long, Map<String, Session>> emailSessionMap = new ConcurrentHashMap<>();


    @Autowired
    public void setServices(
            ChatMessageService chatMessageService,
            UserService userService,
            ChatService chatService,
            AuthenticationService authenticationService,
            PasswordEncoder passwordEncoder
    ) {
        ChatSocket.chatMessageService = chatMessageService;
        ChatSocket.userService = userService;
        ChatSocket.chatService = chatService;
        ChatSocket.authenticationService = authenticationService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long chatId) throws IOException {
        Map<String, String> queryParams = parseQueryParams(session.getRequestURI().getQuery());
        String email = queryParams.get("email");
        String password = queryParams.get("password");

        // Validate credentials
        if (!this.validOpen(session, chatId, email, password)) {
            return;
        }

        // Handle duplicate emails in the same chat room
        chatRooms.putIfAbsent(chatId, new ConcurrentHashMap<>());
        emailSessionMap.putIfAbsent(chatId, new ConcurrentHashMap<>());

        // Disconnect the previous session for this email in the same chat room
        if (emailSessionMap.get(chatId).containsKey(email)) {
            Session oldSession = emailSessionMap.get(chatId).get(email);
            try {
                oldSession.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE,"Another session connected with the same email"));
            } catch (IOException e) {
                logger.error("Failed to close previous session for email {}", email, e);
            }
        }

        // Add the user to the chat room
        chatRooms.get(chatId).put(session, email);
        emailSessionMap.get(chatId).put(email, session);

        // Notify all users in the chat room
        broadcast(chatId, "User " + email + " has joined the chat");
        Long userId = userService.getUserByEmail(email).getUserId();
       // chatMessageService.markMessagesAsSeen(chatId, userId);
        sendChatHistory(email, chatId);
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("chatId") Long chatId, String message) throws IOException {
        ChatMessageDto dto = objectMapper.readValue(message, ChatMessageDto.class);
        String email = chatRooms.get(chatId).get(session);

        // Validate message
        if (!this.validMessage(session, chatId, email, dto)) {
            return;
        }

        // Notify all users in the chat room
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dto.setTimestamp(time);
        dto = chatMessageService.createChatMessage(dto);
        String jsonMessage = objectMapper.writeValueAsString(dto);
        broadcast(chatId, jsonMessage);
        logger.info("[onMessage] {}: {} at {}", email, dto.getMessage(), time);

        // Notify user's unread message
        UnreadMessagesSocket.notifyUnreadMessage(email, dto);
    }

    @OnClose
    public void onClose(Session session, @PathParam("chatId") Long chatId) throws IOException {
        if (!chatRooms.containsKey(chatId)) {
            return;
        }
        String email = chatRooms.get(chatId).remove(session);
        emailSessionMap.get(chatId).remove(email);
        logger.info("[onClose] {} disconnected from chat room {}", email, chatId);
        broadcast(chatId, email + " has left the chat room");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("[onError] Session error: {}", throwable.getMessage());
    }

    private void broadcast(Long chatId, String message) {
        chatRooms.get(chatId).forEach((session, email) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("[Broadcast Exception] {}", e.getMessage());
            }
        });
    }

    private void sendChatHistory(String email, long chatId) {
        Session session = emailSessionMap.get(chatId).get(email);
        try {
            String jsonMessage = objectMapper.writeValueAsString(chatMessageService.getChatMessagesByChatId(chatId));
            session.getBasicRemote().sendText(jsonMessage);
        } catch (IOException e) {
            logger.error("[ChatHistory Exception] {}", e.getMessage());
        }
    }
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private Boolean validOpen(Session session, Long chatId, String email, String password) throws IOException {
        // Validate parameters
        if (email == null || password == null) {
            session.getBasicRemote().sendText("Missing email or password");
            session.close();
            return false;
        }

        // Validate password
        if (!authenticationService.existsByEmailAndPassword(email, password)) {
            session.getBasicRemote().sendText("Invalid email or password");
            session.close();
            return false;
        }

        // Validate Chat ID
        if (!chatService.getChatIdsByUser(userService.getUserByEmail(email)).contains(chatId)) {
            logger.warn("User {} attempted to join an invalid chat ID: {}", email, chatId);
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid chat ID for user " + email));
            return false;
        }

        return true;
    }

    private Boolean validMessage(Session session, Long chatId, String email, ChatMessageDto dto) throws IOException {
        // Validate Chat Id
        if (!dto.getChatId().equals(chatId)) {
            session.getBasicRemote().sendText("Invalid Chat ID");
            session.close();
            return false;
        }

        // Validate User Id
        else if (!dto.getUserId().equals(userService.getUserByEmail(email).getUserId())) {
            session.getBasicRemote().sendText("Invalid User ID");
            session.close();
            return false;
        }

        return true;
    }
}
