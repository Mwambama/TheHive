package com.example.hiveeapp.employer_user.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.chat.message.Message;
import com.example.hiveeapp.employer_user.chat.message.MessageAdapter;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

public class EmployerChatActivity extends AppCompatActivity {

    private static final String TAG = "EmployerChatActivity";
    private RecyclerView chatRecyclerView;
    private EditText msgEtx;
    private Button sendBtn;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private int chatId = -1;
    private int userId;
    private String userEmail;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", null);
        userPassword = preferences.getString("password", null);

        chatId = getIntent().getIntExtra("chatId", -1);

        if (userId == -1 || userEmail == null || userPassword == null || chatId == -1) {
            Toast.makeText(this, "Chat information missing. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
        connectWebSocket();
    }

    private void setupUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        msgEtx = findViewById(R.id.msgEtx);
        sendBtn = findViewById(R.id.sendBtn);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        sendBtn.setOnClickListener(v -> {
            String messageText = msgEtx.getText().toString().trim();
            if (!messageText.isEmpty() && chatId != -1) {
                sendMessage(chatId, messageText, userId);
                msgEtx.setText("");
            } else {
                Toast.makeText(EmployerChatActivity.this, "Message cannot be empty or invalid chat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectWebSocket() {
        if (WebSocketManager.getInstance().isConnected()) {
            Log.d(TAG, "WebSocket already connected.");
            return;
        }

        String webSocketUrl = generateWebSocketUrl(chatId, userEmail, userPassword);
        WebSocketManager.getInstance().setWebSocketListener(new WebSocketListener() {
            @Override
            public void onWebSocketOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Connected", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onWebSocketMessage(String message) {
                runOnUiThread(() -> displayReceivedMessage(message));
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onWebSocketError(Exception ex) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);
    }

    private String generateWebSocketUrl(int chatId, String email, String password) {
        return "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId + "?email=" + email + "&password=" + password;
    }

    private void sendMessage(int chatId, String message, int userId) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("chatId", chatId);
            messageObject.put("message", message);
            messageObject.put("userId", userId);

            if (WebSocketManager.getInstance().isConnected()) {
                WebSocketManager.getInstance().sendMessage(messageObject.toString());
                displaySentMessage(message);
            } else {
                Toast.makeText(this, "WebSocket not connected. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displaySentMessage(String message) {
        messageList.add(new Message(message, true));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void displayReceivedMessage(String rawMessage) {
        if (isJsonMessage(rawMessage)) {
            try {
                JSONObject messageObject = new JSONObject(rawMessage);
                String text = messageObject.getString("message");
                int senderId = messageObject.getInt("userId");
                boolean isSentByUser = (senderId == userId);

                messageList.add(new Message(text, isSentByUser));
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON message: " + rawMessage, e);
            }
        } else {
            messageList.add(new Message(rawMessage, false));
        }

        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private boolean isJsonMessage(String message) {
        try {
            new JSONObject(message);
            return true;
        } catch (JSONException ex) {
            return false;
        }
    }
}