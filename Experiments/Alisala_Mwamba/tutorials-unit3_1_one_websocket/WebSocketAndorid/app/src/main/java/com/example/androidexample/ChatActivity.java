package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private Timer typingTimer;
    private final long TYPING_DELAY = 1000;  // Delay in ms before detecting "stopped typing"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);

        /* connect this activity to the websocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                String message = msgEtx.getText().toString();
                WebSocketManager.getInstance().sendMessage(message);
                msgEtx.setText("");  // Clear input field after sending
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });

        /* Detect typing */
        msgEtx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // User starts typing, send "typing..." message
                WebSocketManager.getInstance().sendMessage("typing...");
                if (typingTimer != null) {
                    typingTimer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Delay before sending "stopped typing..." message
                typingTimer = new Timer();
                typingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        WebSocketManager.getInstance().sendMessage("stopped typing...");
                    }
                }, TYPING_DELAY);
            }
        });
    }
        //made changes to the typing indicators to show someone is typing
    @Override
    public void onWebSocketMessage(String message) {
        /**
         * Handle incoming messages and typing indicators.
         * Update UI on the main thread.
         */
        runOnUiThread(() -> {
            if (message.equals("typing...")) {
                // Show typing indicator
                msgTv.setText("User is typing...");
            } else if (message.equals("stopped typing...")) {
                // Remove typing indicator
                msgTv.setText("");
            } else {
                // Append the received message to the message TextView
                String currentMessages = msgTv.getText().toString();
                msgTv.setText(currentMessages + "\n" + message);
            }
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
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}
}
