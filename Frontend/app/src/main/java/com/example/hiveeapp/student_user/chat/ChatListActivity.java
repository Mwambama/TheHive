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
import com.example.hiveeapp.student_user.ApplyActivity;
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
    private Map<Integer, Integer> jobPostingMap = new HashMap<>(); // Maps employerId to jobPostingId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        // Retrieve userId, email, and password from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", "");

        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);
        Log.d(TAG, "Retrieved email from SharedPreferences: " + userEmail);

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User credentials are missing. Redirecting to login screen.");
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize RecyclerView
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load job postings first, then load applications
        loadJobPostings();

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

    private void loadJobPostings() {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/job-posting";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jobJson = response.getJSONObject(i);
                            int jobPostingId = jobJson.getInt("jobPostingId");
                            int employerId = jobJson.getInt("employerId");

                            jobPostingMap.put(employerId, jobPostingId); // Store employerId-jobPostingId pair
                        }
                        // Once job postings are loaded, proceed to load applications
                        loadApplications();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing job postings", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load job postings", Toast.LENGTH_SHORT).show()
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

                            // Use optInt to avoid JSONException if employerId is missing
                            int employerId = application.optInt("employerId", -1);

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
        intent.putExtra("jobPostingId", chat.getJobPostingId());
        intent.putExtra("userId", userId);
        intent.putExtra("email", userEmail);
        intent.putExtra("password", userPassword);
        startActivity(intent);
    }
}
