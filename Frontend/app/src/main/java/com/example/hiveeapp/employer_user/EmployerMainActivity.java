package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.chat.EmployerChatListActivity;
import com.example.hiveeapp.employer_user.setting.EmployerProfileActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class EmployerMainActivity extends AppCompatActivity {

    private static final String TAG = "EmployerMainActivity";
    private int userId;
    private String userEmail;
    private String userPassword;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigationView();

        // Retrieve user details from SharedPreferences
        retrieveUserDetails();

        // Initialize TabLayout if necessary
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setupTabLayout(tabLayout);
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
                Log.d(TAG, "Navigating to User Page");
                navigateToProfile();
                return true;
            } else if (itemId == R.id.nav_chat) {
                Log.d(TAG, "Navigating to Chat");
                navigateToChatList();
                return true;
            } else if (itemId == R.id.nav_add_job) {
                Log.d(TAG, "Navigating to Add Job");
                // Add functionality to navigate to Add Job activity
                return true;
            } else if (itemId == R.id.nav_tracking) {
                Log.d(TAG, "Navigating to Tracking");
                // Add functionality to navigate to Tracking activity
                return true;
            } else {
                return false;
            }
        });
    }

    private void retrieveUserDetails() {
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", "");

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void navigateToProfile() {
        Log.d(TAG, "Navigating to EmployerProfileActivity");
        Intent intent = new Intent(EmployerMainActivity.this, EmployerProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    private void navigateToChatList() {
        Log.d(TAG, "Navigating to EmployerChatListActivity");
        Intent intent = new Intent(EmployerMainActivity.this, EmployerChatListActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("email", userEmail);
        intent.putExtra("password", userPassword);
        startActivity(intent);
    }

    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                // Handle fragment switching if needed
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
}
