package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    private static final String TAG = "StudentMainActivity";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1); // Use getInt with a default value

        // Debug: Log retrieved userId
        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to login screen
            Log.e(TAG, "User ID is invalid. Redirecting to login screen.");
            // startActivity(new Intent(this, LoginActivity.class));
            // finish();
        }

        // Set up bottom navigation view for navigation to Profile, Apply, and Chat
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                Intent intent = new Intent(StudentMainActivity.this, StudentProfileViewActivity.class);
                intent.putExtra("USER_ID", userId); // Pass userId to profile activity
                Log.d(TAG, "Navigating to StudentProfileViewActivity with userId: " + userId);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
