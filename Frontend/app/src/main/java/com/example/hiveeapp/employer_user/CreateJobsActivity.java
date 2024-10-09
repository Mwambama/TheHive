package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class CreateJobsActivity extends AppCompatActivity {

    private static final int ADD_JOB_REQUEST_CODE = 1; // Request code for AddJobActivity

    private Button addJobButton; // Declare the add job button
    private TextView postedJobTextView; // TextView to display posted job details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs); // Set the layout for this activity

        addJobButton = findViewById(R.id.add_job_button); // Initialize the add job button
        postedJobTextView = findViewById(R.id.posted_job_text_view); // Initialize the TextView

        // Set a click listener for the add job button
        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddJobActivity when the button is clicked
                Intent intent = new Intent(CreateJobsActivity.this, AddJobActivity.class);
                startActivityForResult(intent, ADD_JOB_REQUEST_CODE); // Start for result
            }
        });
    }

    // Override onActivityResult to receive the new job
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_JOB_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("newJob")) {
                postedjobs newJob = (postedjobs) data.getSerializableExtra("newJob"); // Ensure postedjobs implements Serializable
                displayPostedJob(newJob); // Display the posted job
            }
        }
    }

    private void displayPostedJob(postedjobs job) {
        // Create a string representation of the job details
        String jobDetails = "Job Title: " + job.getJobTitle() +
                "\nDescription: " + job.getJobDescription() +
                "\nType: " + job.getJobType() +
                "\nSalary: " + job.getSalaryRequirements() +
                "\nAge Requirement: " + job.getAgeRequirement() +
                "\nMinimum GPA: " + job.getMinimumGpa();

        // Display job details in the TextView
        postedJobTextView.setText(jobDetails);
    }
}
