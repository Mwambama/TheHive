package com.example.hiveeapp.employer_user;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

    public class mainEmployer extends AppCompatActivity {
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
            employerNameTextView.setText("John steve");
            jobTitleTextView.setText("HR Manager");
            aboutDescriptionTextView.setText("I work at pella corporation and work with the engineering team.");

            // Initialize Add Job button
            addJobButton = findViewById(R.id.add_job_button);
            addJobButton.setOnClickListener(view -> {
                Intent intent = new Intent(mainEmployer.this, AddJobActivity.class);
                startActivity(intent);
            });

            // Display posted jobs
            displayPostedJobs();
        }

        private void displayPostedJobs() {
            // Code to display posted jobs
        }
    }


