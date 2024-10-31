package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.example.hiveeapp.student_user.swipe.JobPosting;
import com.example.hiveeapp.student_user.swipe.JobSwipeAdapter;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {

    private static final String TAG = "StudentMainActivity";
    private int userId;
    private ViewPager2 viewPager;
    private JobSwipeAdapter jobSwipeAdapter;
    private List<JobPosting> jobPostings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Retrieve userId from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User ID is invalid. Redirecting to login screen.");
        }

        // Initialize ViewPager and adapter
        viewPager = findViewById(R.id.viewPager);
        jobSwipeAdapter = new JobSwipeAdapter(jobPostings, this);
        viewPager.setAdapter(jobSwipeAdapter);
        viewPager.setUserInputEnabled(false); // Disable backward swipe

        loadJobPostings();

        // Set swipe listener for applying and skipping jobs
        viewPager.setOnTouchListener((v, event) -> {
            float startX = event.getX();
            float endX;

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

        // Set up bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            }
            return false;
        });
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