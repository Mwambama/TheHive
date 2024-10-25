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

public class ChatActivity2 extends AppCompatActivity {

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private Timer typingTimer = new Timer();
    private final long TYPING_DELAY = 1000;  // Delay to consider "stopped typing"
    private final String[] typingDots = {"", ".", "..", "..."};  // Typing animation dots
    private int dotIndex = 0;  // Current dot index
    private Handler typingHandler = new Handler();  // Handler for typing animation

    private final Runnable typingAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            msgTv.setText("User is typing" + typingDots[dotIndex]);
            dotIndex = (dotIndex + 1) % typingDots.length;  // Cycle through dots
            typingHandler.postDelayed(this, 500);  // Update every 500ms
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn2);
        msgEtx = findViewById(R.id.msgEdt2);
        msgTv = findViewById(R.id.tx2);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            String message = parseEmoji(msgEtx.getText().toString());  // Apply emoji parsing
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat2");
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            msgEtx.setText("");  // Clear input after sending
            hideTypingIndicator();  // Hide typing indicator on send
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
        intent.putExtra("key", "chat2");
        intent.putExtra("message", isTyping ? "typing..." : "stopped typing");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (isTyping) {
            showTypingIndicator();  // Show typing animation when typing
        } else {
            hideTypingIndicator();  // Hide typing animation when stopped
        }
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
        typingHandler.post(typingAnimationRunnable);  // Start typing animation
    }

    // Hide typing animation
    private void hideTypingIndicator() {
        typingHandler.removeCallbacks(typingAnimationRunnable);  // Stop animation
        msgTv.setText("");  // Clear typing indicator
    }

    // For receiving messages and updating UI
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            String message = intent.getStringExtra("message");
            if ("chat2".equals(key)) {
                runOnUiThread(() -> {
                    if ("typing...".equals(message)) {
                        showTypingIndicator();  // Start typing animation
                    } else if ("stopped typing".equals(message)) {
                        hideTypingIndicator();  // Stop typing animation
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
