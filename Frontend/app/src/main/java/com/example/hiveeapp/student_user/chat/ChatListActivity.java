package com.example.hiveeapp.student_user.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.student_user.chat.ChatDto;
import com.example.hiveeapp.student_user.chat.ChatListAdapter;

import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ChatListActivity";
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch chat list
        loadChats();
    }

    private void loadChats() {
        StudentApi.getChats(this,
                chats -> {
                    // Set up RecyclerView adapter
                    chatListAdapter = new ChatListAdapter(chats, chat -> openChat(chat));
                    chatRecyclerView.setAdapter(chatListAdapter);
                },
                error -> {
                    Log.e(TAG, "Error fetching chats: " + error.getMessage());
                    Toast.makeText(this, "Failed to load chats", Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void openChat(ChatDto chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chat.getChatId());
        startActivity(intent);
    }
}

