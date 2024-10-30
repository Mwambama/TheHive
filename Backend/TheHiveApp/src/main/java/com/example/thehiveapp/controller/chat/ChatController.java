package com.example.thehiveapp.controller.chat;

import com.example.thehiveapp.service.chat.ChatMessageService;
import com.example.thehiveapp.service.chat.ChatService;
import com.example.thehiveapp.service.user.UserService;
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
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/chat/{chatId}")
@Component
public class ChatController {

    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    // Maps each chatId to its map of session-email pairs
    private static Map<String, Map<Session, String>> chatRooms = new ConcurrentHashMap<>();
    private static Map<String, Map<String, Session>> emailSessionMap = new ConcurrentHashMap<>();

    private static ChatService chatService;
    private static ChatMessageService chatMessageService;
    private static UserService userService;

    @Autowired
    public void setServices(
            ChatService chatService,
            ChatMessageService chatMessageService,
            UserService userService
    ) {
        ChatController.chatService = chatService;
        ChatController.chatMessageService = chatMessageService;
        ChatController.userService = userService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") String chatId) throws IOException {
        URI uri = session.getRequestURI();
        String query = uri.getQuery(); // example: "email=foo&password=bar"

        String email = null;
        String password = null;

        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue[0].equals("email")) {
                    email = keyValue[1];
                } else if (keyValue[0].equals("password")) {
                    password = keyValue[1];
                }
            }
        }

        if (email == null || password == null) {
            session.getBasicRemote().sendText("Missing email or password");
            session.close();
            return;
        }

//        // Validate password (using userService)
        // TODO
//        if (!userService.validateUser(email, password)) {
//            session.getBasicRemote().sendText("Invalid email or password");
//            session.close();
//            return;
//        }

        // Validate chatId
        // TODO

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
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("chatId") String chatId, String message) throws IOException {
        String email = chatRooms.get(chatId).get(session);
        logger.info("[onMessage] " + email + ": " + message);
        broadcast(chatId, email + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("chatId") String chatId) throws IOException {
        String email = chatRooms.get(chatId).remove(session);
        emailSessionMap.get(chatId).remove(email);
        logger.info("[onClose] {} disconnected from chat room {}", email, chatId);

        // Notify all users in the chat room
        broadcast(chatId, email + " has left the chat room");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("[onError] Session error: {}", throwable.getMessage());
    }

    private void broadcast(String chatId, String message) {
        chatRooms.get(chatId).forEach((session, email) -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("[Broadcast Exception] {}", e.getMessage());
            }
        });
    }
}
