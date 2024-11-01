package com.example.hiveeapp.student_user.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.ApplyActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Initialize RecyclerView
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch chat data
        loadChats();

        // Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                // Navigate to Profile (StudentMainActivity)
                Intent intent = new Intent(this, StudentMainActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_apply) {
                Intent intent = new Intent(this, ApplyActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_chat) {
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);

    }

    private void loadChats() {
        StudentApi.getChats(this,
                chats -> {
                    chatListAdapter = new ChatListAdapter(chats, this::openChat, this);
                    chatRecyclerView.setAdapter(chatListAdapter);
                },
                error -> Toast.makeText(this, "Failed to load chats", Toast.LENGTH_SHORT).show()
        );
    }

    private void openChat(ChatDto chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chat.getChatId());
        startActivity(intent);
    }
}
