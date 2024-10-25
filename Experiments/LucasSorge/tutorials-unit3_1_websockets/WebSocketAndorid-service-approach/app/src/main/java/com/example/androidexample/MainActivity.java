package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button connectBtn1, connectBtn2;
    private EditText serverEtx1, usernameEtx1, serverEtx2, usernameEtx2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBtn1 = findViewById(R.id.connectBtn);
        connectBtn2 = findViewById(R.id.connectBtn2);
        serverEtx1 = findViewById(R.id.serverEdt);
        usernameEtx1 = findViewById(R.id.unameEdt);
        serverEtx2 = findViewById(R.id.serverEdt2);
        usernameEtx2 = findViewById(R.id.unameEdt2);

        connectBtn1.setOnClickListener(view -> {
            String serverUrl = "ws://10.0.2.2:8080/chat1"; // Use emulator IP for localhost
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "chat1");
            serviceIntent.putExtra("url", serverUrl);
            startService(serviceIntent);

            Intent intent = new Intent(this, ChatActivity1.class);
            startActivity(intent);
        });

        connectBtn2.setOnClickListener(view -> {
            String serverUrl = "ws://10.0.2.2:8080/chat2"; // Use emulator IP for localhost
            Intent serviceIntent = new Intent(this, WebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "chat2");
            serviceIntent.putExtra("url", serverUrl);
            startService(serviceIntent);

            Intent intent = new Intent(this, ChatActivity2.class);
            startActivity(intent);
        });
    }
}