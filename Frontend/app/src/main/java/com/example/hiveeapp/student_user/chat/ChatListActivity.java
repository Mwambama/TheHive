package com.example.hiveeapp.student_user.chat;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        Log.d("ChatListActivity", "Retrieved userId from SharedPreferences: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize RecyclerView
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch applications with job titles
        loadApplications();

        // Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
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

    private void loadApplications() {
        StudentApi.getStudentApplications(this, userId,
                applications -> {
                    List<ChatDto> chatList = new ArrayList<>();
                    try {
                        for (int i = 0; i < applications.length(); i++) {
                            JSONObject application = applications.getJSONObject(i);
                            int chatId = -1; // Placeholder if chatId is not present
                            int jobPostingId = application.getInt("jobPostingId");
                            String jobTitle = application.getString("jobTitle");
                            int employerId = -1; // Set based on your data structure

                            ChatDto chat = new ChatDto(chatId, employerId, userId, jobPostingId, jobTitle);
                            chatList.add(chat);
                        }
                        // Pass the list with job titles to the adapter
                        chatListAdapter = new ChatListAdapter(chatList, this::openChat, this);
                        chatRecyclerView.setAdapter(chatListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing applications data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load applications", Toast.LENGTH_SHORT).show()
        );
    }

    private void openChat(ChatDto chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chat.getChatId());
        startActivity(intent);
    }
}
