package com.example.hiveeapp.student_user.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout; // Import for LinearLayout
import android.widget.TextView; // Import TextView
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

import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity implements MessageAdapter.OnMessageClickListener {

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

    private Integer replyToMessageId = null;
    private LinearLayout replyPreviewContainer;
    private TextView replyingToTextView;
    private TextView replyMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("Chat");

        // Retrieve and log SharedPreferences data
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", null);
        userPassword = preferences.getString("password", null);
        chatId = getIntent().getIntExtra("chatId", -1);

        Log.d(TAG, "Retrieved userId: " + userId);
        Log.d(TAG, "Retrieved userEmail: " + userEmail);
        Log.d(TAG, "Retrieved userPassword: " + userPassword);
        Log.d(TAG, "Retrieved chatId: " + chatId);

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

        // Initialize the reply preview container and TextViews
        replyPreviewContainer = findViewById(R.id.replyPreviewContainer);
        replyPreviewContainer.setVisibility(View.GONE); // Initially hidden

        replyingToTextView = findViewById(R.id.replyingToTextView);
        replyMessageTextView = findViewById(R.id.replyMessageTextView);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, userId);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Set the click listener on the adapter
        messageAdapter.setOnMessageClickListener(this);

        // Allow users to cancel a reply by clicking on the reply preview container
        replyPreviewContainer.setOnClickListener(v -> {
            replyToMessageId = null;
            replyPreviewContainer.setVisibility(View.GONE);
            messageAdapter.setSelectedMessageId(-1); // Reset selected message
        });

        // Send button click listener
        sendBtn.setOnClickListener(v -> {
            String messageText = msgEtx.getText().toString().trim();
            if (!messageText.isEmpty() && chatId != -1) {
                sendMessage(chatId, messageText, userId, replyToMessageId);
                msgEtx.setText("");
                replyToMessageId = null;
                replyPreviewContainer.setVisibility(View.GONE);
                messageAdapter.setSelectedMessageId(-1); // Reset selected message
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty or invalid chat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageClick(Message message) {
        // Set replyToMessageId when a message is clicked to reply
        if (replyToMessageId != null && replyToMessageId.equals(message.getMessageId())) {
            replyToMessageId = null;
            replyPreviewContainer.setVisibility(View.GONE);
            messageAdapter.setSelectedMessageId(-1);
        } else {
            replyToMessageId = message.getMessageId();
            replyMessageTextView.setText(message.getText());
            replyPreviewContainer.setVisibility(View.VISIBLE);

            messageAdapter.setSelectedMessageId(message.getMessageId());
        }
    }

    @Override
    public void onMessageUnselected() {
        replyToMessageId = null;
        replyPreviewContainer.setVisibility(View.GONE);
        messageAdapter.setSelectedMessageId(-1);
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
            public void onWebSocketMessage(Message message) {
                runOnUiThread(() -> {
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecyclerView.scrollToPosition(messageList.size() - 1);
                });
            }

            @Override
            public void onWebSocketClose(int code, String reason, boolean remote) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Disconnected: " + reason, Toast.LENGTH_SHORT).show());
                Log.d(TAG, "WebSocket closed with reason: " + reason);
                if (!remote && shouldReconnect) {
                    new android.os.Handler().postDelayed(() -> connectWebSocket(), 5000);
                }
            }

            @Override
            public void onWebSocketError(Exception ex) {
                runOnUiThread(() -> Toast.makeText(ChatActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show());
                Log.e(TAG, "WebSocket error: " + ex.getMessage(), ex);
            }

            @Override
            public int getCurrentUserId() {
                return userId;
            }
        });

        WebSocketManager.getInstance().connectWebSocket(webSocketUrl);
    }

    private String generateWebSocketUrl(int chatId, String email, String password) {
        String url = "ws://coms-3090-063.class.las.iastate.edu:8080/ws/chat/" + chatId +
                "?email=" + email + "&password=" + password;

        Log.d(TAG, "Generated WebSocket URL: " + url);
        return url;
    }

    private void sendMessage(int chatId, String message, int userId, Integer replyToId) {
        if (WebSocketManager.getInstance().isConnected()) {
            // Only include replyToId if not null
            WebSocketManager.getInstance().sendMessage(chatId, message, userId, replyToId);
            Log.d(TAG, "Sent message: " + message + " with replyToId: " + replyToId);

            // Reset reply after sending
            replyToMessageId = null;
            replyPreviewContainer.setVisibility(View.GONE);
            messageAdapter.setSelectedMessageId(-1); // Reset selected message
        } else {
            Toast.makeText(this, "WebSocket not connected. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Overloaded method for backward compatibility
    private void sendMessage(int chatId, String message, int userId) {
        sendMessage(chatId, message, userId, null);
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
