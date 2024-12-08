package com.example.hiveeapp.student_user.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.swipe.JobPosting;

import java.util.List;

public class JobResultsActivity extends AppCompatActivity implements JobListAdapter.OnJobInteractionListener {

    private static final String TAG = "JobResultsActivity";
    private static final String PREFERENCES_NAME = "JobSwipePreferences";
    private static final String STUDENT_ID_KEY = "studentId";

    private RecyclerView jobResultsRecyclerView;
    private JobListAdapter jobListAdapter;
    private List<JobPosting> jobPostings;
    private int studentId = -1;

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
    }

    private void retrieveStudentId() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        studentId = preferences.getInt(STUDENT_ID_KEY, -1);

        if (studentId != -1) {
            Log.d(TAG, "Student ID retrieved from SharedPreferences: " + studentId);
        } else {
            Log.e(TAG, "Student ID not found in SharedPreferences.");
        }
    }

    @Override
    public void onJobClick(JobPosting jobPosting) {
        Toast.makeText(this, "Clicked on: " + jobPosting.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onJobApply(JobPosting jobPosting, int position) {
        Toast.makeText(this, "Apply functionality not implemented.", Toast.LENGTH_SHORT).show();
    }
}
