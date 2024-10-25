package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity1 extends AppCompatActivity {

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private Timer typingTimer = new Timer();
    private final long TYPING_DELAY = 1000;  // Delay to consider "stopped typing"
    private final String[] typingDots = {"", ".", "..", "..."};
    private int dotIndex = 0;
    private Handler typingHandler = new Handler();

    private final String userId = "User1";  // Unique identifier for this user

    private final Runnable typingAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            msgTv.setText("User is typing" + typingDots[dotIndex]);
            dotIndex = (dotIndex + 1) % typingDots.length;
            typingHandler.postDelayed(this, 500);  // Update every 500ms
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn);
        backMainBtn = findViewById(R.id.backMainBtn);
        msgEtx = findViewById(R.id.msgEdt);
        msgTv = findViewById(R.id.tx1);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            String message = parseEmoji(msgEtx.getText().toString());  // Apply emoji parsing
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat1");
            intent.putExtra("message", message);
            intent.putExtra("userId", userId); // Include userId in the broadcast
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            msgEtx.setText("");  // Clear input after sending
            hideTypingIndicator();  // Hide typing indicator on send
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Typing indicator setup
        msgEtx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    sendTypingStatus(true);  // Broadcast "typing..."
                    typingTimer.cancel();    // Reset the timer
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Start timer for stopped typing
                typingTimer = new Timer();
                typingTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sendTypingStatus(false);  // Broadcast "stopped typing"
                    }
                }, TYPING_DELAY);
            }
        });
    }

    // Method to broadcast typing status
    private void sendTypingStatus(boolean isTyping) {
        Intent intent = new Intent("SendWebSocketMessage");
        intent.putExtra("key", "chat1");
        intent.putExtra("message", isTyping ? "typing..." : "stopped typing");
        intent.putExtra("userId", userId); // Include userId in the broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    // Method to parse and replace text with emojis
    private String parseEmoji(String text) {
        return text.replace(":)", "\uD83D\uDE42")  // Simple smiley emoji
                .replace(":(", "\uD83D\uDE41")  // Sad emoji
                .replace(";)", "\uD83D\uDE09")  // Wink emoji
                .replace(":D", "\uD83D\uDE03"); // Grin emoji
    }

    // Show typing animation
    private void showTypingIndicator() {
        dotIndex = 0;  // Reset dot index
        typingHandler.post(typingAnimationRunnable);
    }

    // Hide typing animation
    private void hideTypingIndicator() {
        typingHandler.removeCallbacks(typingAnimationRunnable);
        msgTv.setText("");  // Clear typing indicator
    }

    // For receiving messages and updating UI
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            String message = intent.getStringExtra("message");
            String senderId = intent.getStringExtra("userId"); // Get the sender's userId
            if ("chat1".equals(key)) {
                runOnUiThread(() -> {
                    if ("typing...".equals(message)) {
                        if (!userId.equals(senderId)) { // Show typing only for other user
                            showTypingIndicator();  // Start typing animation
                        }
                    } else if ("stopped typing".equals(message)) {
                        if (!userId.equals(senderId)) { // Hide typing only for other user
                            hideTypingIndicator();  // Stop typing animation
                        }
                    } else {
                        hideTypingIndicator();  // Ensure typing indicator is hidden after receiving a message
                        String s = msgTv.getText().toString();
                        msgTv.setText(s + "\n" + message);
                    }
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}
