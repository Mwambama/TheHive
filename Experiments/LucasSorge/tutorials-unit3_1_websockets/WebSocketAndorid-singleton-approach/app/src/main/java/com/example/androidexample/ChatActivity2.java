package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ScrollView;
import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity2 extends AppCompatActivity implements WebSocketListener {

    private Button sendBtn, clearChatBtn;
    private EditText msgEtx;
    private TextView msgTv, statusTv, typingTv, messageCountTv;
    private ScrollView scrollView;
    private int messageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        /* initialize UI elements */
        sendBtn = findViewById(R.id.sendBtn2);
        clearChatBtn = findViewById(R.id.clearChatBtn2);
        msgEtx = findViewById(R.id.msgEdt2);
        msgTv = findViewById(R.id.tx2);
        statusTv = findViewById(R.id.statusTv2);
        typingTv = findViewById(R.id.typingTv2);
        messageCountTv = findViewById(R.id.messageCountTv2);
        scrollView = findViewById(R.id.scrollViewChat2);

        /* connect this activity to the websocket instance */
        WebSocketManager2.getInstance().setWebSocketListener(ChatActivity2.this);

        /* Typing indicator listener */
        msgEtx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                typingTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                typingTv.setVisibility(View.GONE);
            }
        });

        /* Send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                WebSocketManager2.getInstance().sendMessage(msgEtx.getText().toString());
                typingTv.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage());
            }
        });

        /* Clear chat button listener */
        clearChatBtn.setOnClickListener(v -> {
            msgTv.setText("");
            messageCount = 0;
            updateMessageCount();
        });
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + message);
            messageCount++;
            updateMessageCount();
            autoScroll();
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nConnection closed by " + closedBy + "\nReason: " + reason);
            statusTv.setText("Disconnected");
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        runOnUiThread(() -> statusTv.setText("Connected"));
    }

    @Override
    public void onWebSocketError(Exception ex) {
        runOnUiThread(() -> statusTv.setText("Error occurred"));
    }

    private void updateMessageCount() {
        messageCountTv.setText("Messages: " + messageCount);
    }

    private void autoScroll() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}