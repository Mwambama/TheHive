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
import com.example.hiveeapp.employer_user.model.postedjobs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class EmployerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView employerNameTextView;
    private TextView jobTitleTextView;
    private TextView emailTextView;
    private Button backButton;

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

        // Initialize Back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        // Display posted jobs
        displayPostedJobs();

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void displayPostedJobs() {
        List<postedjobs> jobs = loadJobsFromAssets();
        if (jobs != null) {
            RecyclerView recyclerView = findViewById(R.id.posted_jobs_recycler_view);
            JobAdapter adapter = new JobAdapter(jobs);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private List<postedjobs> loadJobsFromAssets() {
        Gson gson = new Gson();
        Type jobListType = new TypeToken<List<postedjobs>>() {}.getType();
        try {
            InputStream inputStream = getAssets().open("jobs.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return gson.fromJson(reader, jobListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_add_job) {
            startActivity(new Intent(this, CreateJobsActivity.class)); // Navigate to CreateJobsActivity
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
}
