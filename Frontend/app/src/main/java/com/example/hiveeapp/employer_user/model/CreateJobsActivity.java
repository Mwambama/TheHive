package com.example.hiveeapp.employer_user.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.AddJobActivity;
import com.example.hiveeapp.employer_user.display.JobDetailsActivity;
import com.example.hiveeapp.employer_user.display.PostedJobs;
import com.example.hiveeapp.employer_user.EmployerMainActivity; // Import the main activity

public class CreateJobsActivity extends AppCompatActivity {

    private static final int ADD_JOB_REQUEST_CODE = 1;
    private static final int EDIT_JOB_REQUEST_CODE = 2;
    private Button addJobButton;
    private Button backButton; // Declare the back button
    private TextView postedJobTextView;
    private PostedJobs currentJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs);

        addJobButton = findViewById(R.id.add_job_button);
        backButton = findViewById(R.id.back_button); // Initialize the back button
        postedJobTextView = findViewById(R.id.posted_job_text_view);

        // Set up the back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJobsActivity.this, EmployerMainActivity.class);
                startActivity(intent);
                finish(); // Optional: call finish() to remove this activity from the back stack
            }
        });

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJobsActivity.this, AddJobActivity.class);
                startActivityForResult(intent, ADD_JOB_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_JOB_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("newJob")) {
                currentJob = (PostedJobs) data.getSerializableExtra("newJob");
                displayPostedJob(currentJob);
            }
        } else if (requestCode == EDIT_JOB_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("updatedJob")) {
                currentJob = (PostedJobs) data.getSerializableExtra("updatedJob");
                displayPostedJob(currentJob);
            }
        }
    }

    private void displayPostedJob(PostedJobs job) {
        String jobDetails = "Job Title: " + job.getJobTitle() +
                "\nDescription: " + job.getJobDescription() +
                "\nType: " + job.getJobType() +
                "\nSalary: " + job.getSalaryRequirements() +
                "\nAge Requirement: " + job.getAgeRequirement() +
                "\nMinimum GPA: " + job.getMinimumGpa();
        postedJobTextView.setText(jobDetails);

        postedJobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDetailsActivity dialog = JobDetailsActivity.newInstance(job);
                dialog.show(getSupportFragmentManager(), "JobDetailsDialog");
            }
        });
    }
}
