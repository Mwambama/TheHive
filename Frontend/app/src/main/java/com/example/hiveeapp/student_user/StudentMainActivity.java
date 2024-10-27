package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1); // Use getInt and set a default value

        if (userId == -1) {
            // Handle case where userId is not set or invalid
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login screen
        }

        // Set up bottom navigation view for navigation to Profile, Apply, and Chat
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                // Navigate to Profile Page
                startActivity(new Intent(StudentMainActivity.this, StudentProfileViewActivity.class));
                return true;
            } else if (itemId == R.id.navigation_apply) {
                // Navigate to Apply Page
                startActivity(new Intent(StudentMainActivity.this, ApplyActivity.class));
                return true;
            } else if (itemId == R.id.navigation_chat) {
                // Navigate to Chat Page
                startActivity(new Intent(StudentMainActivity.this, ChatActivity.class));
                return true;
            }
            return false;
        });
    }
}
