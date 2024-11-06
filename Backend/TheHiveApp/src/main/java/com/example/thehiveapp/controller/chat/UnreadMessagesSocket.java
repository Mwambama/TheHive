package com.example.thehiveapp.controller.chat;

import com.example.thehiveapp.dto.chat.ChatMessageDto;
import com.example.thehiveapp.service.authentication.AuthenticationService;
import com.example.thehiveapp.service.chat.ChatMessageService;
import com.example.thehiveapp.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@ServerEndpoint("/ws/unread-messages")
@Component
public class UnreadMessagesSocket {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(UnreadMessagesSocket.class);

    private static ChatMessageService chatMessageService;
    private static UserService userService;
    private static AuthenticationService authenticationService;

    // Map to track sessions by user email
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @Autowired
    public void setServices(
            ChatMessageService chatMessageService,
            UserService userService,
            AuthenticationService authenticationService
    ) {
        UnreadMessagesSocket.chatMessageService = chatMessageService;
        UnreadMessagesSocket.userService = userService;
        UnreadMessagesSocket.authenticationService = authenticationService;
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Map<String, String> queryParams = parseQueryParams(session.getRequestURI().getQuery());
        String email = queryParams.get("email");
        String password = queryParams.get("password");

        // Validate credentials
        if (!this.validOpen(session, email, password)) {
            return;
        }

        // Add session to userSessions
        userSessions.put(email, session);

        // Send unread messages
        sendUnreadMessages(email);
    }

    private void sendUnreadMessages(String email) throws IOException {
        Long userId = userService.getUserByEmail(email).getUserId();
        List<ChatMessageDto> unreadMessages = chatMessageService.getUnreadChatMessagesByUserId(userId);
        Session session = userSessions.get(email);
        if (session != null && session.isOpen()) {
            String jsonUnreadMessages = objectMapper.writeValueAsString(unreadMessages);
            session.getBasicRemote().sendText(jsonUnreadMessages);
        }
    }

    @OnClose
    public void onClose(Session session) {
        userSessions.values().removeIf(s -> s.equals(session));
        logger.info("[onClose] Session closed");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("[onError] Session error: {}", throwable.getMessage());
    }

    // Static method to notify a user's session with a new message
    public static void notifyUnreadMessage(String email, ChatMessageDto message) {
        Session session = userSessions.get(email);
        if (session != null && session.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(List.of(message));
                session.getBasicRemote().sendText(jsonMessage);
            } catch (IOException e) {
                LoggerFactory.getLogger(UnreadMessagesSocket.class).error("Error sending unread message", e);
            }
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

    private Boolean validOpen(Session session, String email, String password) throws IOException {
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

        return true;
    }
}
