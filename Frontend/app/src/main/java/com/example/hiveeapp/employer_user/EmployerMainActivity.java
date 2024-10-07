package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.example.hiveeapp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerMainActivity extends AppCompatActivity {
    private TextView employerNameTextView;
    private TextView jobTitleTextView;
    private TextView aboutDescriptionTextView;
    private Button addJobButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Initialize TextView fields
        employerNameTextView = findViewById(R.id.employer_name);
        jobTitleTextView = findViewById(R.id.job_title);
        aboutDescriptionTextView = findViewById(R.id.about_description);

        // Set employer information
        employerNameTextView.setText("John Steve");
        jobTitleTextView.setText("HR Manager");
        aboutDescriptionTextView.setText("I work at Pella Corporation and work with the engineering team.");

        // Initialize Add Job button
        addJobButton = findViewById(R.id.add_job_button);
        addJobButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerMainActivity.this, AddJobActivity.class);
            startActivity(intent);
        });

        // Display posted jobs
        displayPostedJobs();

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private void displayPostedJobs() {
        // Code to display posted jobs
    }
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_chat) {
            // Navigate to Chat activity
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_home) {
            // Already on home, do nothing or add intent if needed
            return true;
        } else if (item.getItemId() == R.id.nav_tracking) {
            // Navigate to Tracking activity
            startActivity(new Intent(this, TrackingApplicationActivity.class));
            return true;
        } else {
            return false;
        }
    }
}
