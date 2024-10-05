package com.example.hiveeapp.student_user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

public class StudentMainActivity extends AppCompatActivity {

    private TextView jobTitle, companyName, jobDetails;
    private Button btnSkip, btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Initialize views
        jobTitle = findViewById(R.id.jobTitle);
        companyName = findViewById(R.id.companyName);
        jobDetails = findViewById(R.id.jobDetails);
        btnSkip = findViewById(R.id.btnSkip);
        btnApply = findViewById(R.id.btnApply);

        // Set up onClick listeners for Skip and Apply buttons
        btnSkip.setOnClickListener(v -> skipJob());
        btnApply.setOnClickListener(v -> applyJob());

        // Load the first job card
        loadJobCard();
    }

    private void loadJobCard() {
        // Fetch job data from the server (example data used here)
        jobTitle.setText("Software Engineer - Intern");
        companyName.setText("Amazon");
        jobDetails.setText("Remote | Full-time | $25/hr | Junior | New York");
    }

    private void skipJob() {
        // Logic to skip the job and load the next one
        Toast.makeText(this, "Job skipped", Toast.LENGTH_SHORT).show();
        loadJobCard(); // Load the next job (you should implement a proper queue)
    }

    private void applyJob() {
        // Logic to apply for the job
        Toast.makeText(this, "Job applied", Toast.LENGTH_SHORT).show();
        loadJobCard(); // Load the next job (you should implement a proper queue)
    }
}
