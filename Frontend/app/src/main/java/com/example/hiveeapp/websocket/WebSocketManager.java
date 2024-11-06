package com.example.hiveeapp.websocket;

import android.util.Log;
import com.example.hiveeapp.websocket.message.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
            Log.e("WebSocketManager", "Failed to connect WebSocket", e);
        }
    }

    public boolean isConnected() {
        return webSocketClient != null && webSocketClient.isOpen();
    }

    public void sendMessage(String message) {
        if (isConnected()) {
            try {
                JSONObject messageJson = new JSONObject();
                messageJson.put("message", message);

                // Format current timestamp as string for consistency
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                messageJson.put("timestamp", timestamp);

                webSocketClient.send(messageJson.toString());
            } catch (JSONException e) {
                Log.e("WebSocketManager", "Failed to send message JSON", e);
            }
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

            try {
                if (message.trim().startsWith("[")) {
                    JSONArray messageArray = new JSONArray(message);
                    for (int i = 0; i < messageArray.length(); i++) {
                        JSONObject messageObject = messageArray.getJSONObject(i);
                        processJsonMessage(messageObject);
                    }
                } else if (message.trim().startsWith("{")) {
                    JSONObject messageJson = new JSONObject(message);
                    processJsonMessage(messageJson);
                } else {
                    // Process as a system message if it’s plain text
                    if (webSocketListener != null) {
                        Message systemMessage = new Message(message, false, -1, -1, -1, System.currentTimeMillis(), null, false);
                        webSocketListener.onWebSocketMessage(systemMessage);
                    }
                }
            } catch (JSONException e) {
                Log.e("WebSocket", "Error processing message: " + message, e);
            }
        }

        private void processJsonMessage(JSONObject messageJson) {
            try {
                String messageText = messageJson.getString("message");
                int chatId = messageJson.getInt("chatId");
                int messageId = messageJson.getInt("messageId");
                int senderId = messageJson.getInt("userId");
                String timestampStr = messageJson.getString("timestamp");
                Integer replyToId = messageJson.isNull("replyToId") ? null : messageJson.getInt("replyToId");
                boolean seen = messageJson.getBoolean("seen");

                // Parse timestamp string to milliseconds
                long timestampMillis = parseTimestamp(timestampStr);

                if (webSocketListener != null) {
                    Message receivedMessage = new Message(
                            messageText,
                            senderId == webSocketListener.getCurrentUserId(),
                            senderId,
                            messageId,
                            chatId,
                            timestampMillis,
                            replyToId,
                            seen
                    );
                    webSocketListener.onWebSocketMessage(receivedMessage);
                }
            } catch (JSONException e) {
                Log.e("WebSocket", "Error parsing JSON message", e);
            }
        }

        private long parseTimestamp(String timestampStr) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = inputFormat.parse(timestampStr);
                return (date != null) ? date.getTime() : System.currentTimeMillis();
            } catch (ParseException e) {
                Log.e("WebSocket", "Timestamp parsing failed: " + timestampStr, e);
                return System.currentTimeMillis();
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d("WebSocket", "Closed: " + reason);
            if (webSocketListener != null) {
                webSocketListener.onWebSocketClose(code, reason, remote);
            }

            if (!remote && shouldReconnect) {
                reconnectWebSocket();
            }
        }

        @Override
        public void onError(Exception ex) {
            Log.e("WebSocket", "Error: " + ex.getMessage(), ex);
            if (webSocketListener != null) {
                webSocketListener.onWebSocketError(ex);
            }
        }
    }
}
