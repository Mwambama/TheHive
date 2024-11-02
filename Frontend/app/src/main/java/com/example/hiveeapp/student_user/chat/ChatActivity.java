package com.example.hiveeapp.student_user.chat;

import android.content.Context;
import android.os.Bundle;
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
import com.example.hiveeapp.student_user.chat.message.Message;
import com.example.hiveeapp.student_user.chat.message.MessageAdapter;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.volley.VolleySingleton;
import com.example.hiveeapp.websocket.WebSocketManager;
import com.example.hiveeapp.websocket.WebSocketListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.java_websocket.handshake.ServerHandshake;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText msgEtx;
    private Button sendBtn;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private int chatId = -1;
    private final int userId = 1014; // Replace this with the actual logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize UI components
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        msgEtx = findViewById(R.id.msgEtx);
        sendBtn = findViewById(R.id.sendBtn);

        // Initialize message list and adapter
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        // Fetch the chat ID from the server
        fetchChatId(this);

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

    private void fetchChatId(Context context) {
        StudentApi.getChats(context, new Response.Listener<List<ChatDto>>() {
            @Override
            public void onResponse(List<ChatDto> chatList) {
                for (ChatDto chat : chatList) {
                    if (chat.getStudentId() == userId) {
                        chatId = chat.getChatId();
                        connectWebSocket();
                        break;
                    }
                }
                if (chatId == -1) {
                    Toast.makeText(ChatActivity.this, "No chat found for the user", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this, "Failed to retrieve chat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectWebSocket() {
        String email = "teststudent1@example.com"; // Update this as necessary
        String password = "TestStudent1234@"; // Update this as necessary
        String webSocketUrl = generateWebSocketUrl(chatId, email, password);

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
            // Check if the message contains JSON by looking for a starting '{'
            int jsonStartIndex = rawMessage.indexOf("{");

            if (jsonStartIndex != -1) {
                // If JSON is detected, trim to JSON part and parse
                rawMessage = rawMessage.substring(jsonStartIndex);
                JSONObject messageObject = new JSONObject(rawMessage);

                String text = messageObject.getString("message");
                int userId = messageObject.getInt("userId");
                int currentUserId = 1014; // Replace with actual user ID dynamically if possible

                // Determine if the message is sent by the user or received from the employer
                boolean isSentByUser = (userId == currentUserId);

                // Add the message to the list with the correct sender type
                messageList.add(new Message(text, isSentByUser));
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                chatRecyclerView.scrollToPosition(messageList.size() - 1);
            } else {
                // If no JSON found, it's likely a plain text message
                Toast.makeText(ChatActivity.this, "Received non-JSON message: " + rawMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "Error parsing JSON message", Toast.LENGTH_SHORT).show();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "Unexpected message format", Toast.LENGTH_SHORT).show();
        }
    }
}
