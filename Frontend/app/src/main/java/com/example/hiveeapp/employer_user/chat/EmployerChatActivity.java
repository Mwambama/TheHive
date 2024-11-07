package com.example.hiveeapp.employer_user.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.websocket.message.Message;
import com.example.hiveeapp.websocket.message.MessageAdapter;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.handshake.ServerHandshake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EmployerChatActivity extends AppCompatActivity {
    private static final String TAG = "EmployerChatActivity";
    private RecyclerView chatRecyclerView;
    private EditText msgEtx;
    private Button sendBtn;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private Set<Integer> receivedMessageIds;
    private int chatId = -1;
    private int userId;
    private String userEmail;
    private String userPassword;
    private boolean shouldReconnect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        toolbar.setTitle("Employer Chat");

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

        receivedMessageIds = new HashSet<>();
        setupUI();
        connectWebSocket();
    }

    private void setupUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        msgEtx = findViewById(R.id.msgEtx);
        sendBtn = findViewById(R.id.sendBtn);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, userId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        sendBtn.setOnClickListener(v -> {
            String messageText = msgEtx.getText().toString().trim();
            if (!messageText.isEmpty() && chatId != -1) {
                sendMessage(chatId, messageText, userId);
                msgEtx.setText("");
            } else {
                Toast.makeText(this, "Message cannot be empty or invalid chat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectWebSocket() {
        if (!shouldReconnect || WebSocketManager.getInstance().isConnected()) {
            Log.d(TAG, "WebSocket already connected or reconnection is disabled.");
            return;
        }

        String webSocketUrl = generateWebSocketUrl(chatId, userEmail, userPassword);
        Log.d(TAG, "Connecting to WebSocket with URL: " + webSocketUrl);

        WebSocketManager.getInstance().setWebSocketListener(new WebSocketListener() {
            @Override
            public void onWebSocketOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Connected", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onWebSocketMessage(Message message) {
                runOnUiThread(() -> {
                    if (!receivedMessageIds.contains(message.getMessageId())) {
                        messageList.add(message);
                        receivedMessageIds.add(message.getMessageId());
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        chatRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
                if (!remote && shouldReconnect) {
                    reconnectWebSocket();
                }
            }

            @Override
            public void onWebSocketError(Exception ex) {
                runOnUiThread(() -> Toast.makeText(EmployerChatActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
                Log.e(TAG, "WebSocket error: " + ex.getMessage(), ex);
            }

            @Override
            public int getCurrentUserId() {
                return userId;
            }
        });

        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);
    }

    private void reconnectWebSocket() {
        new android.os.Handler().postDelayed(() -> connectWebSocket(), 5000); // Retry after 5 seconds
    }

    private String generateWebSocketUrl(int chatId, String email, String password) {
        return "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId +
                "?email=" + email + "&password=" + password;
    }

    private void sendMessage(int chatId, String message, int userId) {
        if (WebSocketManager.getInstance().isConnected()) {
            WebSocketManager.getInstance().sendMessage(chatId, message, userId);
            Log.d(TAG, "Sent message with details: chatId=" + chatId + ", userId=" + userId + ", message=" + message);
        } else {
            Toast.makeText(this, "WebSocket not connected. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectWebSocket();
    }

    private void disconnectWebSocket() {
        shouldReconnect = false;
        if (WebSocketManager.getInstance().isConnected()) {
            WebSocketManager.getInstance().disconnectWebSocket();
            Log.d(TAG, "WebSocket disconnected.");
        }
    }
}