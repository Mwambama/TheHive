package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.PostedJobs;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.employer_user.model.CreateJobsActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EmployerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView employerNameTextView;
    private TextView jobTitleTextView;
    private TextView emailTextView;
    private Button logoutButton; // Declare logout button
    private Button viewInfoButton; // Declare view info button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Initialize TextView fields
        employerNameTextView = findViewById(R.id.employer_name);
        jobTitleTextView = findViewById(R.id.job_title);
        emailTextView = findViewById(R.id.email);

        // Set employer information
        employerNameTextView.setText("John Steve");
        jobTitleTextView.setText("Software Developer");
        emailTextView.setText("john.steve@example.com");

        // Initialize Log Out button
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logout());

        // Initialize View Info button
        viewInfoButton = findViewById(R.id.view_employer_info_button);
        viewInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerMainActivity.this, com.example.hiveeapp.employer_user.model.employerinfoActivity.class);
            startActivity(intent);
        });

        // Display posted jobs
        displayPostedJobs();

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void displayPostedJobs() {
        List<PostedJobs> jobs = loadJobsFromAssets();
        if (jobs != null) {
            RecyclerView recyclerView = findViewById(R.id.posted_jobs_recycler_view);
            JobAdapter adapter = new JobAdapter(jobs);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private List<PostedJobs> loadJobsFromAssets() {
        List<PostedJobs> jobs = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("jobs.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse JSON data
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonJob = jsonArray.getJSONObject(i);
                // Use constructor to create PostedJobs objects
                PostedJobs job = new PostedJobs(
                        jsonJob.getString("jobId"),
                        jsonJob.getString("jobTitle"),
                        jsonJob.getString("jobDescription"),
                        jsonJob.getString("jobType"),
                        jsonJob.getString("salaryRequirements"),
                        jsonJob.getString("ageRequirement"),
                        jsonJob.getString("minimumGpa")
                );
                jobs.add(job);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_add_job) {
            startActivity(new Intent(this, CreateJobsActivity.class));
            return true;
        } else if (itemId == R.id.nav_chat) {
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        } else if (itemId == R.id.nav_home) {
            return true;
        } else if (itemId == R.id.nav_tracking) {
            startActivity(new Intent(this, TrackingApplicationActivity.class));
            return true;
        } else {
            return false;
        }
    }

    private void logout() {
        Intent intent = new Intent(EmployerMainActivity.this, LoginActivity.class);
        // Clear the back stack so that user cannot return to the employer main activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish current activity
    }
}