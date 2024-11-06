package com.example.hiveeapp.websocket;

import com.example.hiveeapp.websocket.message.Message;
import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketListener {
    void onWebSocketOpen(ServerHandshake handshakedata);
    void onWebSocketMessage(Message message); // Accepts Message object
    void onWebSocketClose(int code, String reason, boolean remote);
    void onWebSocketError(Exception ex);

    int getCurrentUserId();
}