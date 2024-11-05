package com.example.hiveeapp.employer_user.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
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
    private Map<Integer, String> studentNamesMap = new HashMap<>(); // Stores studentId to studentName
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
    }

    private void setupUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadChats();
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
                                // Placeholder for the chat title, will be updated with student name
                                EmployerChatDto chat = new EmployerChatDto(chatId, employerId, studentId, "Loading...");
                                chatList.add(chat);

                                // Load the student's name for each chat
                                loadStudentName(studentId);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing chat JSON", e);
                    }

                    // Set up the adapter with the chat list
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
        // Check if the student name is already loaded to avoid duplicate requests
        if (studentNamesMap.containsKey(studentId)) {
            updateChatTitle(studentId, studentNamesMap.get(studentId));
            return;
        }

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/student/" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    String studentName = response.optString("name", "Unknown Student"); // No exception is thrown here
                    studentNamesMap.put(studentId, studentName);

                    // Update the chat title in the chat list with the student name
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
                chat.setJobTitle(studentName);  // Update jobTitle with student's name
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