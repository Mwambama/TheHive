package com.example.hiveeapp.student_user.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.swipe.ApplyActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ChatListActivity";
    private RecyclerView chatRecyclerView;
    private ChatListAdapter chatListAdapter;
    private int userId;
    private String userEmail;
    private String userPassword;
    private Map<Integer, String> applicationJobTitles = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", "");

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadApplicationsAndChats();
        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_apply) {
                startActivity(new Intent(ChatListActivity.this, StudentMainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(ChatListActivity.this, StudentProfileViewActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_chat) {
                return true; // Already in Chat screen
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_chat);
    }

    private void loadApplicationsAndChats() {
        StudentApi.getStudentApplications(this, userId,
                applications -> {
                    for (int i = 0; i < applications.length(); i++) {
                        try {
                            JSONObject application = applications.getJSONObject(i);
                            int jobPostingId = application.getInt("jobPostingId");
                            String jobTitle = application.optString("jobTitle", "Unknown Title");
                            applicationJobTitles.put(jobPostingId, jobTitle);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing application JSON: " + e.getMessage());
                        }
                    }
                    loadChats();
                },
                error -> Toast.makeText(this, "Failed to load applications", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadChats() {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/chat";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<ChatDto> matchedChats = new ArrayList<>();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chatJson = response.getJSONObject(i);
                            int chatId = chatJson.getInt("chatId");
                            int employerId = chatJson.getInt("employerId");
                            int studentId = chatJson.getInt("studentId");
                            int jobPostingId = chatJson.optInt("jobPostingId", -1);

                            // Extract lastMessage and lastMessageTime from the JSON response
                            String lastMessage = chatJson.optString("lastMessage", null);
                            String lastMessageTime = chatJson.optString("lastMessageTime", null);

                            // Filter chats by studentId (current user's ID)
                            if (studentId == userId) {
                                String jobTitle = applicationJobTitles.getOrDefault(jobPostingId, "Unknown Title");

                                // Create ChatDto instance with all parameters
                                ChatDto chat = new ChatDto(
                                        chatId,
                                        employerId,
                                        studentId,
                                        jobPostingId,
                                        jobTitle,
                                        lastMessage,
                                        lastMessageTime
                                );
                                matchedChats.add(chat);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing chat JSON: " + e.getMessage());
                        Toast.makeText(this, "Error loading chats", Toast.LENGTH_SHORT).show();
                    }

                    chatListAdapter = new ChatListAdapter(matchedChats, this::openChat, this);
                    chatRecyclerView.setAdapter(chatListAdapter);
                },
                error -> Toast.makeText(this, "Failed to load chats", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private Map<String, String> getAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String credentials = userEmail + ":" + userPassword;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }

    private void openChat(ChatDto chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("chatId", chat.getChatId());
        intent.putExtra("jobPostingId", chat.getJobPostingId());
        startActivity(intent);
    }
}
