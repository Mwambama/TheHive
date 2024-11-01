package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.student_user.swipe.JobPosting;
import com.example.hiveeapp.student_user.swipe.JobSwipeAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {

    private static final String TAG = "StudentMainActivity";
    private int userId;
    private ViewPager2 viewPager;
    private JobSwipeAdapter jobSwipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (bottomNavigationView == null) {
            Log.e(TAG, "BottomNavigationView is null. Check if the ID matches in XML.");
            Toast.makeText(this, "Navigation view setup error", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if the view is not found
        }

        // Set up bottom navigation view listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.navigation_apply) {
                return true;
            } else if (itemId == R.id.navigation_chat) {
                // Navigate to ChatActivity
                Intent intent = new Intent(StudentMainActivity.this, ChatActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Set the initial selected item
        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User ID is invalid. Redirecting to login screen.");
            // Uncomment this to redirect to LoginActivity if needed
            // startActivity(new Intent(this, LoginActivity.class));
            // finish();
        }

        // Initialize ViewPager and adapter
        viewPager = findViewById(R.id.viewPager);
        jobSwipeAdapter = new JobSwipeAdapter(jobPostings, this);
        viewPager.setAdapter(jobSwipeAdapter);
        viewPager.setUserInputEnabled(false); // Disable backward swipe

        loadJobPostings();

        // Handle ViewPager swipe listener
        viewPager.setOnTouchListener((v, event) -> {
            float startX = 0, endX;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    endX = event.getX();
                    int position = viewPager.getCurrentItem();

                    if (startX < endX) {  // Swipe Right to apply for job
                        applyForJob(position);
                    }
                    viewPager.setCurrentItem(position + 1); // Move to the next job
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure that "Apply" is selected when returning to this activity
        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);
    }

    private void loadJobPostings() {
        StudentApi.getJobPostings(this,
                jobPostings -> {
                    this.jobPostings.clear();
                    this.jobPostings.addAll(jobPostings);
                    jobSwipeAdapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Error fetching job postings", Toast.LENGTH_SHORT).show()
        );
    }

    private void applyForJob(int position) {
        if (position < jobPostings.size()) {
            JobPosting job = jobPostings.get(position);
            StudentApi.applyForJob(this, userId, job.getJobPostingId(),
                    response -> Toast.makeText(this, response, Toast.LENGTH_SHORT).show(),
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void navigateToProfile() {
        Intent intent = new Intent(StudentMainActivity.this, StudentProfileViewActivity.class);
        intent.putExtra("USER_ID", userId);
        Log.d(TAG, "Navigating to StudentProfileViewActivity with userId: " + userId);
        startActivity(intent);
    }
}
