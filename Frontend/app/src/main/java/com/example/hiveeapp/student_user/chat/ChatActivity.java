package com.example.hiveeapp.student_user.chat;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.chat.message.Message;
import com.example.hiveeapp.student_user.chat.message.MessageAdapter;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.volley.VolleySingleton;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.handshake.ServerHandshake;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
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

        // Retrieve SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", null);
        userPassword = preferences.getString("password", null);

        // Retrieve jobPostingId and chatId from Intent
        int jobPostingId = getIntent().getIntExtra("jobPostingId", -1);
        chatId = getIntent().getIntExtra("chatId", -1);

        if (userId == -1 || userEmail == null || userPassword == null || chatId == -1 || jobPostingId == -1) {
            Toast.makeText(this, "Chat information missing. Please try again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();  // Initialize UI
        connectWebSocket();  // Set up WebSocket
    }

    private void setupUI() {
        // Initialize UI components
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        msgEtx = findViewById(R.id.msgEtx);
        sendBtn = findViewById(R.id.sendBtn);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Set up send button action
        sendBtn.setOnClickListener(v -> {
            String messageText = msgEtx.getText().toString().trim();
            if (!messageText.isEmpty() && chatId != -1) {
                sendMessage(chatId, messageText, userId);
                displaySentMessage(messageText); // Display the sent message
                msgEtx.setText(""); // Clear input field
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty or invalid chat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, String> getAuthorizationHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String username = preferences.getString("email", "");
        String password = preferences.getString("password", "");

        if (!username.isEmpty() && !password.isEmpty()) {
            String credentials = username + ":" + password;
            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            headers.put("Authorization", auth);
        } else {
            Log.e(TAG, "User credentials are missing. Cannot set Authorization header.");
        }

        return headers;
    }

    private void fetchChatId(Context context, int studentId, int jobPostingId) {
        StudentApi.getChats(context, new Response.Listener<List<ChatDto>>() {
            @Override
            public void onResponse(List<ChatDto> chatList) {
                boolean found = false;
                for (ChatDto chat : chatList) {
                    Log.d(TAG, "Processing chat: chatId=" + chat.getChatId() + ", studentId=" + chat.getStudentId() + ", employerId=" + chat.getEmployerId());
                    if (chat.getStudentId() == studentId && chat.getJobPostingId() == jobPostingId) {
                        chatId = chat.getChatId();
                        found = true;
                        Log.d(TAG, "Found matching chat with chatId=" + chatId);
                        connectWebSocket();  // Connect using the found chat ID
                        break;
                    }
                }
                if (!found) {
                    Log.e(TAG, "No chats matched for studentId=" + studentId + " and jobPostingId=" + jobPostingId);
                    Toast.makeText(ChatActivity.this, "No chat found for this student and job posting", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            Log.e(TAG, "Error fetching chat ID: " + error.getMessage());
            Toast.makeText(ChatActivity.this, "Failed to retrieve chat ID", Toast.LENGTH_SHORT).show();
        });
    }

    private void connectWebSocket() {
        String webSocketUrl = generateWebSocketUrl(chatId, userEmail, userPassword);
        Log.d(TAG, "Connecting to WebSocket with URL: " + webSocketUrl);

        WebSocketManager.getInstance().setWebSocketListener(new WebSocketListener() {
            @Override
            public void onWebSocketOpen(ServerHandshake handshakedata) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Connected", Toast.LENGTH_SHORT).show());
                Log.d(TAG, "WebSocket connection opened for chat ID: " + chatId);
            }

            @Override
            public void onWebSocketMessage(String message) {
                runOnUiThread(() -> displayReceivedMessage(message));
                Log.d(TAG, "Received WebSocket message: " + message);
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
                Log.e(TAG, "WebSocket disconnected. Reason: " + reason);
            }

            @Override
            public void onWebSocketError(Exception ex) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
                Log.e(TAG, "WebSocket error: " + ex.getMessage());
            }
        });

        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);
    }

    private String generateWebSocketUrl(int chatId, String email, String password) {
        return "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId +
                "?email=" + email + "&password=" + password;
    }

    private void sendMessage(int chatId, String message, int userId) {
        String formattedMessage = "{"
                + "\"chatId\":" + chatId + ","
                + "\"message\":\"" + message + "\","
                + "\"userId\":" + userId
                + "}";

        WebSocketManager.getInstance().sendMessage(formattedMessage);
    }

    private void displaySentMessage(String message) {
        messageList.add(new Message(message, true)); // 'true' indicates it's sent by the user
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);
    }

    private void displayReceivedMessage(String rawMessage) {
        try {
            int jsonStartIndex = rawMessage.indexOf("{");

            if (jsonStartIndex != -1) {
                rawMessage = rawMessage.substring(jsonStartIndex);
                JSONObject messageObject = new JSONObject(rawMessage);

                String text = messageObject.getString("message");
                int senderId = messageObject.getInt("userId");

                boolean isSentByUser = (senderId == userId);

                messageList.add(new Message(text, isSentByUser));
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
            } else {
                Toast.makeText(ChatActivity.this, "Received non-JSON message: " + rawMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "Error parsing JSON message", Toast.LENGTH_SHORT).show();
        }
    }
}
