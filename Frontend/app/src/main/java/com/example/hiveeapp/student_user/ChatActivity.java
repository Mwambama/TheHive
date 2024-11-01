package com.example.hiveeapp.student_user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;
import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity extends AppCompatActivity {

    private TextView msgTv;
    private EditText msgEtx;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize UI components
        msgTv = findViewById(R.id.msgTv); // Make sure these IDs match your XML layout
        msgEtx = findViewById(R.id.msgEtx);
        sendBtn = findViewById(R.id.sendBtn);

        // Replace with actual values
        Long chatId = 123L; // The chatId you want to connect to
        String email = "teststudent1@example.com";
        String password = "TestStudent1234@";
        Long userId = 1L; // Replace with the actual user ID

        // Generate WebSocket URL
        String webSocketUrl = generateWebSocketUrl(chatId, email, password);

        // Set up WebSocket listener
        WebSocketManager.getInstance().setWebSocketListener(new WebSocketListener() {
            @Override
            public void onWebSocketOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Connected", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onWebSocketMessage(String message) {
                runOnUiThread(() -> displayReceivedMessage(message));
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onWebSocketError(Exception ex) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Connect WebSocket
        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);

        // Set up send button action
        sendBtn.setOnClickListener(v -> {
            String messageText = msgEtx.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(chatId, messageText, userId); // Use actual chatId and userId
                msgEtx.setText(""); // Clear input field
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateWebSocketUrl(Long chatId, String email, String password) {
        return "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId +
                "?email=" + email + "&password=" + password;
    }

    private void sendMessage(Long chatId, String message, Long userId) {
        String formattedMessage = "{"
                + "\"chatId\":" + chatId + ","
                + "\"message\":\"" + message + "\","
                + "\"userId\":" + userId
                + "}";

        WebSocketManager.getInstance().sendMessage(formattedMessage);
    }

    private void displayReceivedMessage(String message) {
        msgTv.append("Employer: " + message + "\n");
    }
}
