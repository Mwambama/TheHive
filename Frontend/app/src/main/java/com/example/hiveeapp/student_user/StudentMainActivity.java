package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.application.JobApplicationFragment;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
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
    private String userEmail;
    private String userPassword;
    private RecyclerView recyclerView;
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
            return;
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.navigation_apply) {
                return true;
            } else if (itemId == R.id.navigation_chat) {
                Intent intent = new Intent(StudentMainActivity.this, ChatListActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new JobApplicationFragment())
                .commit();


        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);

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

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        jobSwipeAdapter = new JobSwipeAdapter(this, userId);
        recyclerView.setAdapter(jobSwipeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadJobPostings();

        // Attach ItemTouchHelper for swipe functionality
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(jobSwipeAdapter.getSwipeCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void navigateToProfile() {
        Intent intent = new Intent(StudentMainActivity.this, StudentProfileViewActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    public int getUserId() {
        return userId;
    }
}