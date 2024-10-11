package com.example.hiveeapp.student_user;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

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

        // Set back arrow click listener to finish activity
        backArrowIcon.setOnClickListener(v -> finish());
    }
}
