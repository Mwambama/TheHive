package com.example.androidexample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WebSocketService extends Service {

    private final Map<String, WebSocketClient> webSockets = new HashMap<>();

    public WebSocketService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("CONNECT".equals(action)) {
                String url = intent.getStringExtra("url");
                String key = intent.getStringExtra("key");
                connectWebSocket(key, url);
            } else if ("DISCONNECT".equals(action)) {
                String key = intent.getStringExtra("key");
                disconnectWebSocket(key);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("SendWebSocketMessage"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (WebSocketClient client : webSockets.values()) {
            client.close();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connectWebSocket(String key, String url) {
        try {
            URI serverUri = URI.create(url);
            WebSocketClient webSocketClient = new WebSocketClient(serverUri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d(key, "Connected to WebSocket");
                }

                @Override
                public void onMessage(String message) {
                    Intent intent = new Intent("WebSocketMessageReceived");
                    intent.putExtra("key", key);
                    intent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d(key, "WebSocket closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.d(key, "WebSocket error: " + ex.getMessage());
                }
            };

            webSocketClient.connect();
            webSockets.put(key, webSocketClient);

        } catch (Exception e) {
            Log.d("WebSocketService", "Error connecting to WebSocket: " + e.getMessage());
        }
    }

    private void disconnectWebSocket(String key) {
        if (webSockets.containsKey(key)) {
            webSockets.get(key).close();
        }
    }

    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            String message = intent.getStringExtra("message");

            WebSocketClient webSocket = webSockets.get(key);
            if (webSocket != null && webSocket.isOpen()) {
                webSocket.send(message);
            } else {
                Log.d("WebSocket", "WebSocket is not open, cannot send message.");
            }
        }
    };
}