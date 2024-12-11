package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.chat.EmployerChatListActivity;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
import com.example.hiveeapp.employer_user.setting.EmployerProfileActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmployerMainActivity extends AppCompatActivity {

    private static final String TAG = "EmployerMainActivity";
    private int userId, companyId;
    private String userEmail, userPassword, userRole;
    private BottomNavigationView bottomNavigationView;

    // UI Elements
    private TextView employerNameTextView;
    private TextView employerEmailTextView;
    private TextView employerAddressTextView;
    private TextView employerPhoneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigationView();

        // Initialize UI components
        initializeUI();

        // Retrieve user details from SharedPreferences
        retrieveUserDetails();

        // Fetch and display employer details
        fetchEmployerDetails(userId);

        // Initialize TabLayout if necessary
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setupTabLayout(tabLayout);
    }

    private void initializeUI() {
        employerNameTextView = findViewById(R.id.employerNameTextView);
        employerEmailTextView = findViewById(R.id.employerEmailTextView);
        employerAddressTextView = findViewById(R.id.employerAddressTextView);
        employerPhoneTextView = findViewById(R.id.employerPhoneTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_main_user_page) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.nav_chat) {
                navigateToChatList();
                return true;
            } else if (itemId == R.id.nav_add_job) {
                navigateToAddJob();
                return true;
            }
            return false;
        });
    }

    private void retrieveUserDetails() {
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", ""); // Retrieve userPassword
        userRole = preferences.getString("role", "EMPLOYER");

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void fetchEmployerDetails(int employerId) {
        String apiUrl = "http://coms-3090-063.class.las.iastate.edu:8080/employer/" + employerId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    try {
                        // Parse the response and update UI
                        String name = response.getString("name");
                        String email = response.getString("email");
                        String phone = response.getString("phone");
                        JSONObject address = response.getJSONObject("address");
                        String addressText = address.getString("street") + ", " +
                                address.getString("city") + ", " +
                                address.getString("state") + " - " +
                                address.getString("zipCode");

                        // Update UI elements
                        employerNameTextView.setText("Name: " + name);
                        employerEmailTextView.setText("Email: " + email);
                        employerAddressTextView.setText("Address: " + addressText);
                        employerPhoneTextView.setText("Phone: " + phone);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing employer details", e);
                        Toast.makeText(this, "Error loading employer details", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Error fetching employer details", error);
                    Toast.makeText(this, "Error fetching employer details. Please try again.", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");

                // Add the Basic Auth header
                String credentials = userEmail + ":" + userPassword;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);

                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void navigateToChatList() {
        Intent intent = new Intent(EmployerMainActivity.this, EmployerChatListActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("email", userEmail);
        intent.putExtra("password", userPassword);
        startActivity(intent);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, EmployerProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("COMPANY_ID", companyId);
        startActivity(intent);
    }

    private void navigateToAddJob() {
        Intent intent = new Intent(this, EditJobActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                // Handle fragment switching if needed
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
