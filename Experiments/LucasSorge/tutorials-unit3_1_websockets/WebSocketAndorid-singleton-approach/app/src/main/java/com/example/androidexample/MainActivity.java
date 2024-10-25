package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button connectBtn, connectBtn2, sharedChatBtn, backBtn, backBtn2;
    private EditText serverEtx, usernameEtx, serverEtx2, usernameEtx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize UI elements */
        connectBtn = findViewById(R.id.connectBtn);
        connectBtn2 = findViewById(R.id.connectBtn2);
        backBtn = findViewById(R.id.backBtn);
        backBtn2 = findViewById(R.id.backBtn2);
        serverEtx = findViewById(R.id.serverEdt);
        usernameEtx = findViewById(R.id.unameEdt);
        serverEtx2 = findViewById(R.id.serverEdt2);
        usernameEtx2 = findViewById(R.id.unameEdt2);

        /* connect button listener for WebSocketManager1 (User 1) */
        connectBtn.setOnClickListener(view -> {
            String serverUrl = serverEtx.getText().toString() + usernameEtx.getText().toString();

            // Establish WebSocket connection for user 1
            WebSocketManager1.getInstance().connectWebSocket(serverUrl);

            // Go to ChatActivity1
            Intent intent = new Intent(this, ChatActivity1.class);
            startActivity(intent);
        });

        /* connect button listener for WebSocketManager2 (User 2) */
        connectBtn2.setOnClickListener(view -> {
            String serverUrl = serverEtx2.getText().toString() + usernameEtx2.getText().toString();

            // Establish WebSocket connection for user 2
            WebSocketManager2.getInstance().connectWebSocket(serverUrl);

            // Go to ChatActivity2
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });

        /* Back button listener to go back to ChatActivity1 */
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity1.class);
            startActivity(intent);
        });

        /* Back button listener to go back to ChatActivity2 */
        backBtn2.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });
    }
}