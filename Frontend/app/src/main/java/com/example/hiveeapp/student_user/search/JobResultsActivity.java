package com.example.hiveeapp.student_user.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobResultsActivity extends AppCompatActivity implements JobListAdapter.OnJobInteractionListener {

    private static final String TAG = "JobResultsActivity";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String APPLIED_JOBS_KEY = "AppliedJobs";

    private RecyclerView jobResultsRecyclerView;
    private JobListAdapter jobListAdapter;
    private List<JobPosting> jobPostings;
    private int studentId = -1;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_results);

        // Retrieve student ID
        retrieveStudentId();

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Student ID not found. Cannot proceed.");
            finish();
            return;
        }

        // Initialize RecyclerView and Adapter
        jobResultsRecyclerView = findViewById(R.id.job_results_recycler_view);
        jobListAdapter = new JobListAdapter(this);
        jobResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobResultsRecyclerView.setAdapter(jobListAdapter);

        // Retrieve job postings from Intent
        jobPostings = (List<JobPosting>) getIntent().getSerializableExtra("jobPostings");

        if (jobPostings != null && !jobPostings.isEmpty()) {
            Log.d(TAG, "Job postings loaded: " + jobPostings.size());
            jobListAdapter.setJobPostings(jobPostings);
        } else {
            Log.e(TAG, "No job postings found.");
            Toast.makeText(this, "No jobs to display. Try another search.", Toast.LENGTH_SHORT).show();
        }

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigationView();
    }

    private void retrieveStudentId() {
        // Correct SharedPreferences file name
        SharedPreferences preferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        studentId = preferences.getInt("userId", -1); // Make sure the key matches where `userId` is stored

        if (studentId != -1) {
            Log.d(TAG, "Student ID retrieved from SharedPreferences: " + studentId);
        } else {
            Log.e(TAG, "Student ID not found in SharedPreferences.");
            Toast.makeText(this, "Student ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StudentMainActivity.class)); // Redirect to main or login
            finish(); // End current activity
        }
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.navigation_chat) {
                navigateToChat();
                return true;
            } else if (itemId == R.id.navigation_apply) {
                navigateToApply();
                return true;
            } else if (itemId == R.id.navigation_search) {
                finish(); // Already in Job Search
                return true;
            }
            return false;
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, StudentProfileViewActivity.class);
        intent.putExtra("USER_ID", studentId);
        startActivity(intent);
    }

    private void navigateToChat() {
        Intent intent = new Intent(this, ChatListActivity.class);
        intent.putExtra("studentId", studentId);
        startActivity(intent);
    }

    private void navigateToApply() {
        Intent intent = new Intent(this, StudentMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJobClick(JobPosting jobPosting) {
        Toast.makeText(this, "Clicked on: " + jobPosting.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJobApply(JobPosting jobPosting, int position) {
        applyForJob(studentId, jobPosting.getJobPostingId(), position);
    }

    private void applyForJob(int studentId, int jobPostingId, int position) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> appliedJobs = preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>());

        if (appliedJobs.contains(String.valueOf(jobPostingId))) {
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object for application request", e);
            Toast.makeText(this, "Error creating application request.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_SHORT).show();
                    saveAppliedJob(jobPostingId);
                    jobListAdapter.updateAppliedJobStatus(position);
                },
                error -> {
                    String errorMessage = "Failed to apply for job.";
                    if (error.networkResponse != null) {
                        if (error.networkResponse.statusCode == 500) {
                            errorMessage = "Server error: You may have already applied for this job.";
                        } else if (error.networkResponse.statusCode == 401) {
                            errorMessage = "Unauthorized access. Please log in again.";
                        }
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error applying for job: ", error);
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.putAll(createAuthorizationHeaders());
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void saveAppliedJob(int jobPostingId) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> appliedJobs = preferences.getStringSet(APPLIED_JOBS_KEY, new HashSet<>());
        appliedJobs.add(String.valueOf(jobPostingId));

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(APPLIED_JOBS_KEY, appliedJobs);
        editor.apply();

        Log.d(TAG, "Job ID " + jobPostingId + " saved as applied.");
    }

    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        SharedPreferences preferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String username = preferences.getString("email", "teststudent1@example.com");
        String password = preferences.getString("password", "TestStudent1234@");

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }
}
