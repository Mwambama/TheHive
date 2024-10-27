package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

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
