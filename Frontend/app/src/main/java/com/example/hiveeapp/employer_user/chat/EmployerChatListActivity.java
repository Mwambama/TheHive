package com.example.hiveeapp.employer_user.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.employer_user.display.AddJobActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.employer_user.setting.NetworkUtils;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployerChatListActivity extends AppCompatActivity {

    private static final String TAG = "EmployerChatListActivity";
    private RecyclerView chatRecyclerView;
    private EmployerChatListAdapter chatListAdapter;
    private int userId;
    private String userEmail;
    private String userPassword;
    private Map<Integer, String> studentNamesMap = new HashMap<>();
    private List<EmployerChatDto> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_employer);

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

        setupUI();
        setupBottomNavigationView();
    }

    private void setupUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadChats();
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);  // Ensure this matches your layout ID
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_main_user_page) {
                Log.d(TAG, "Navigating to User Page");
                navigateToProfile();
                return true;
            } else if (itemId == R.id.nav_chat) {
                Log.d(TAG, "Navigating to Chat");
                // Already on Chat screen, do nothing
                return true;
            } else if (itemId == R.id.nav_add_job) {
                Log.d(TAG, "Navigating to Add Job");
                navigateToAddJob();
                return true;
            } else if (itemId == R.id.nav_tracking) {
                Log.d(TAG, "Navigating to Tracking");
                navigateToTracking();
                return true;
            } else {
                return false;
            }
        });

        // Set the current item to "Chat" so it highlights by default
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, EmployerMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAddJob() {
        Intent intent = new Intent(this, AddJobActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTracking() {
        Intent intent = new Intent(this, TrackingApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadChats() {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/chat";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject chatJson = response.getJSONObject(i);
                            int chatId = chatJson.getInt("chatId");
                            int employerId = chatJson.getInt("employerId");
                            int studentId = chatJson.getInt("studentId");
                            int jobPostingId = chatJson.optInt("jobPostingId", -1);

                            if (employerId == userId) {
                                EmployerChatDto chat = new EmployerChatDto(chatId, employerId, studentId, "Loading...");
                                chatList.add(chat);
                                loadStudentName(studentId);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing chat JSON", e);
                    }

                    chatListAdapter = new EmployerChatListAdapter(chatList, this::openChat, this);
                    chatRecyclerView.setAdapter(chatListAdapter);
                },
                error -> Toast.makeText(this, "Failed to load chats", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return NetworkUtils.getHeaders(EmployerChatListActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void loadStudentName(int studentId) {
        if (studentNamesMap.containsKey(studentId)) {
            updateChatTitle(studentId, studentNamesMap.get(studentId));
            return;
        }

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/student/" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String studentName = response.optString("name", "Unknown Student");
                    studentNamesMap.put(studentId, studentName);
                    updateChatTitle(studentId, studentName);
                },
                error -> Log.e(TAG, "Failed to load student name for ID: " + studentId, error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return NetworkUtils.getHeaders(EmployerChatListActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateChatTitle(int studentId, String studentName) {
        for (EmployerChatDto chat : chatList) {
            if (chat.getStudentId() == studentId) {
                chat.setJobTitle(studentName);
            }
        }
        chatListAdapter.notifyDataSetChanged();
    }

    private void openChat(EmployerChatDto chat) {
        Intent intent = new Intent(this, EmployerChatActivity.class);
        intent.putExtra("chatId", chat.getChatId());
        intent.putExtra("userId", userId);
        intent.putExtra("studentId", chat.getStudentId());
        intent.putExtra("jobTitle", chat.getJobTitle());
        startActivity(intent);
    }
}
