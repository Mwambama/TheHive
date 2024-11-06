package com.example.hiveeapp.student_user.chat;

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
import com.example.hiveeapp.student_user.chat.message.Message;
import com.example.hiveeapp.student_user.chat.message.MessageAdapter;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
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

        // Set up Toolbar with back arrow and title
        Toolbar toolbar = findViewById(R.id.chatToolbar);
//        setSupportActionBar(toolbar); // Initialize the Toolbar as ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back arrow
        }
        toolbar.setTitle("Chat"); // Set the title directly on the Toolbar

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
                Toast.makeText(ChatActivity.this, "Message cannot be empty or invalid chat ID", Toast.LENGTH_SHORT).show();
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

        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);
    }

    private String generateWebSocketUrl(int chatId, String email, String password) {
        return "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId +
                "?email=" + email + "&password=" + password;
    }

    private void sendMessage(int chatId, String message, int userId) {
        try {
            JSONObject messageObject = new JSONObject();
            messageObject.put("chatId", chatId);
            messageObject.put("message", message);
            messageObject.put("userId", userId);

            if (WebSocketManager.getInstance().isConnected()) {
                WebSocketManager.getInstance().sendMessage(messageObject.toString());
            } else {
                Toast.makeText(this, "WebSocket not connected. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to send message. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayReceivedMessage(String rawMessage) {
        try {
            if (rawMessage.trim().startsWith("[")) {
                JSONArray messageArray = new JSONArray(rawMessage);
                for (int i = 0; i < messageArray.length(); i++) {
                    JSONObject messageObject = messageArray.getJSONObject(i);
                    processIndividualMessage(messageObject);
                }
            } else if (rawMessage.trim().startsWith("{")) {
                JSONObject messageObject = new JSONObject(rawMessage);
                processIndividualMessage(messageObject);
            } else {
                displaySystemMessage(rawMessage);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing received message JSON: " + rawMessage, e);
        }
    }

    private void processIndividualMessage(JSONObject messageObject) throws JSONException {
        String text = messageObject.getString("message");
        int senderId = messageObject.getInt("userId");
        int messageId = messageObject.getInt("messageId");

        if (!receivedMessageIds.contains(messageId)) {
            boolean isSentByUser = (senderId == userId);
            Message message = new Message(text, isSentByUser, senderId, messageId);
            messageList.add(message);
            receivedMessageIds.add(messageId);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    private void displaySystemMessage(String systemMessage) {
        Message message = new Message(systemMessage, false, -1, -1);
        messageList.add(message);
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