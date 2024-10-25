package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn, clearBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private SharedPreferences sharedPreferences;
    private static final String CHAT_HISTORY = "chat_history";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn);
        clearBtn = findViewById(R.id.clearBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);

        sharedPreferences = getSharedPreferences("ChatPrefs", MODE_PRIVATE);

        loadChatHistory();

        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString().trim();
            if (!message.isEmpty()) {
                try {
                    // send message
                    WebSocketManager.getInstance().sendMessage(message);
                    String timestamp = getCurrentTimestamp();
                    appendMessageToChat("You", message, timestamp);
                    saveChatHistory();  // Save message to chat history
                    msgEtx.setText("");
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage());
                }
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        clearBtn.setOnClickListener(v -> {
            msgTv.setText("");
            clearChatHistory();
        });
    }

    private void saveChatHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHAT_HISTORY, msgTv.getText().toString());
        editor.apply();
    }

    private void loadChatHistory() {
        String chatHistory = sharedPreferences.getString(CHAT_HISTORY, "");
        msgTv.setText(chatHistory);  // Display saved chat history
    }

    private void clearChatHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CHAT_HISTORY);
        editor.apply();
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void appendMessageToChat(String sender, String message, String timestamp) {
        String existingMessages = msgTv.getText().toString();
        String newMessage = sender + " [" + timestamp + "]: " + message;
        msgTv.setText(existingMessages + "\n" + newMessage);
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            // Display received message with timestamp
            String timestamp = getCurrentTimestamp();
            appendMessageToChat("Server", message, timestamp);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(() -> {
            String timestamp = getCurrentTimestamp();
            appendMessageToChat("System", "WebSocket connection established", timestamp);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> {
            String timestamp = getCurrentTimestamp();
            appendMessageToChat("Error", "WebSocket Error: " + ex.getMessage(), timestamp);
        });
    }
}
