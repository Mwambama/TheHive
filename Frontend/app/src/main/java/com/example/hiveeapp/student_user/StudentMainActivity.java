package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentUpdatesActivity;

public class StudentMainActivity extends AppCompatActivity {
    private ImageButton backArrowIcon;
    private TextView userTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstudent);

        // Initialize views
        backArrowIcon = findViewById(R.id.backArrowIcon);
        userTypeTextView = findViewById(R.id.userTypeTextView);

        // Set the text dynamically based on the user type
        String userType = "Student";  // Set user type dynamically here
        userTypeTextView.setText("This is the main page for " + userType);
    }

    // Method to apply for a job
    private void applyJob() {
        // Logic to apply for the job
        Toast.makeText(this, "Job applied", Toast.LENGTH_SHORT).show();
        loadJobCard(); // Load the next job (you should implement a proper queue)
    }

    // Method to navigate to StudentProfileActivity
    private void goToProfile() {
        Intent intent = new Intent(StudentMainActivity.this, StudentUpdatesActivity.class);
        startActivity(intent);
    }

    private void loadJobCard() {
        // Logic to load the next job
    }
}