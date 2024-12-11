package com.example.hiveeapp.employer_user.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.employer_user.display.AddJobActivity;
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

/**
 * EmployerChatListActivity displays a list of chats for the employer user,
 * handles navigation, and loads chat data from the server.
 */
public class EmployerChatListActivity extends AppCompatActivity {

    private static final String TAG = "EmployerChatListActivity";
    private RecyclerView chatRecyclerView;
    private EmployerChatListAdapter chatListAdapter;
    private int userId;
    private String userEmail;
    private String userPassword;
    private Map<Integer, String> studentNamesMap = new HashMap<>();
    private List<EmployerChatDto> chatList = new ArrayList<>();

    /**
     * Initializes the activity and sets up the UI and navigation.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
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

    /**
     * Sets up the RecyclerView for displaying chats.
     */
    private void setupUI() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadChats();
    }

    /**
     * Configures the bottom navigation view for navigation between different activities.
     */
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_main_user_page) {
                Log.d(TAG, "Navigating to User Page");
                navigateToProfile();
                return true;
            } else if (itemId == R.id.nav_chat) {
                Log.d(TAG, "Navigating to Chat");
                return true;
            } else if (itemId == R.id.nav_add_job) {
                Log.d(TAG, "Navigating to Add Job");
                navigateToAddJob();
                return true;
            } else {
                return false;
            }
        });

        // Set the current item to "Chat" so it highlights by default
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);
    }

    /**
     * Navigates to the Employer Main Profile activity.
     */
    private void navigateToProfile() {
        Intent intent = new Intent(this, EmployerMainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigates to the Add Job activity.
     */
    private void navigateToAddJob() {
        Intent intent = new Intent(this, AddJobActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Loads the list of chats from the server and updates the RecyclerView.
     */
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

                            // Extract lastMessage and lastMessageTime from the JSON response
                            String lastMessage = chatJson.optString("lastMessage", null);
                            String lastMessageTime = chatJson.optString("lastMessageTime", null);

                            if (employerId == userId) {
                                // Create EmployerChatDto instance with all parameters
                                EmployerChatDto chat = new EmployerChatDto(
                                        chatId,
                                        employerId,
                                        studentId,
                                        "Loading...",
                                        lastMessage,
                                        lastMessageTime
                                );
                                chatList.add(chat);

                                // Load student name and update jobTitle in the chat object
                                loadStudentName(studentId, chat);
                            }
                        }

                        // Initialize the adapter and set it to the RecyclerView
                        chatListAdapter = new EmployerChatListAdapter(chatList, this::openChat, this);
                        chatRecyclerView.setAdapter(chatListAdapter);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing chat JSON", e);
                    }
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

    /**
     * Loads the student's name from the server and updates the chat object.
     *
     * @param studentId The ID of the student.
     * @param chat      The chat object to update.
     */
    private void loadStudentName(int studentId, EmployerChatDto chat) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/users/" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d(TAG, "User JSON Response: " + response.toString());

                        String fullName;

                        // Check if the "name" key exists
                        if (response.has("name")) {
                            fullName = response.getString("name");
                        } else if (response.has("firstName") && response.has("lastName")) {
                            // Fallback in case "firstName" and "lastName" are present
                            String firstName = response.getString("firstName");
                            String lastName = response.getString("lastName");
                            fullName = firstName + " " + lastName;
                        } else {
                            fullName = "Unknown User";
                        }

                        // Update the studentName in the chat object
                        chat.setStudentName(fullName);

                        // Notify the adapter that the data has changed
                        chatListAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing user JSON", e);
                    }
                },
                error -> Toast.makeText(this, "Failed to load student name", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return NetworkUtils.getHeaders(EmployerChatListActivity.this);
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Updates the chat title with the student's name.
     *
     * @param studentId   The ID of the student.
     * @param studentName The name of the student.
     */
    private void updateChatTitle(int studentId, String studentName) {
        for (EmployerChatDto chat : chatList) {
            if (chat.getStudentId() == studentId) {
                chat.setJobTitle(studentName);
            }
        }
        chatListAdapter.notifyDataSetChanged();
    }

    /**
     * Opens the chat activity for the selected chat.
     *
     * @param chat The selected chat object.
     */
    private void openChat(EmployerChatDto chat) {
        Intent intent = new Intent(this, EmployerChatActivity.class);
        intent.putExtra("chatId", chat.getChatId());
        intent.putExtra("userId", userId);
        intent.putExtra("studentId", chat.getStudentId());
        intent.putExtra("jobTitle", chat.getJobTitle());
        startActivity(intent);
    }
}
