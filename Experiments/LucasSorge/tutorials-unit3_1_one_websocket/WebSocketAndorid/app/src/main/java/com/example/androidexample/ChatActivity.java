package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
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

    private Button sendBtn, clearBtn;  // Added clear button
    private EditText msgEtx;
    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);

        /* connect this activity to the websocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString().trim();
            if (!message.isEmpty()) {
                try {
                    // Send message with a timestamp
                    WebSocketManager.getInstance().sendMessage(message);
                    String timestamp = getCurrentTimestamp();
                    appendMessageToChat("You", message, timestamp);
                    msgEtx.setText("");
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            } else {
                // Show a toast if the message is empty
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        /* clear button listener */
        clearBtn.setOnClickListener(v -> {
            msgTv.setText("");  // Clear all chat messages
        });
    }

    /* Method to get the current timestamp */
    private String getCurrentTimestamp() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /* Helper method to append messages with a timestamp to the chat */
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
