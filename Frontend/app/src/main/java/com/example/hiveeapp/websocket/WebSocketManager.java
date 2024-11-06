package com.example.hiveeapp.websocket;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSocketManager {

    private static WebSocketManager instance;
    private MyWebSocketClient webSocketClient;
    private WebSocketListener webSocketListener;
    private String lastUrl;
    private boolean shouldReconnect = true;

    private WebSocketManager() {}

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void setWebSocketListener(WebSocketListener listener) {
        this.webSocketListener = listener;
    }

    public void connectWebSocket(String serverUrl) {
        this.lastUrl = serverUrl;
        shouldReconnect = true;

        try {
            URI serverUri = URI.create(serverUrl);
            webSocketClient = new MyWebSocketClient(serverUri);
            webSocketClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    public void sendMessage(String message) {
        if (isConnected()) {
            webSocketClient.send(message);
        } else {
            Log.d("WebSocketManager", "WebSocket not connected, attempting to reconnect...");
            reconnectWebSocket();
        }
    }

    public void disconnectWebSocket() {
        shouldReconnect = false;
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

    private void reconnectWebSocket() {
        if (shouldReconnect && lastUrl != null) {
            connectWebSocket(lastUrl);
        }
    }

    private class MyWebSocketClient extends WebSocketClient {
        private MyWebSocketClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d("WebSocket", "Connected");
            if (webSocketListener != null) {
                webSocketListener.onWebSocketOpen(handshakedata);
            }
        }

        @Override
        public void onMessage(String message) {
            Log.d("WebSocket", "Received message: " + message);
            if (webSocketListener != null) {
                webSocketListener.onWebSocketMessage(message);
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d("WebSocket", "Closed: " + reason);
            if (webSocketListener != null) {
                webSocketListener.onWebSocketClose(code, reason, remote);
            }

            // Attempt to reconnect only if shouldReconnect is true and disconnection wasn't manual
            if (!remote && shouldReconnect) {
                reconnectWebSocket();
            }
        }

        @Override
        public void onError(Exception ex) {
            Log.d("WebSocket", "Error: " + ex.getMessage());
            if (webSocketListener != null) {
                webSocketListener.onWebSocketError(ex);
            }
        }
    }
}
