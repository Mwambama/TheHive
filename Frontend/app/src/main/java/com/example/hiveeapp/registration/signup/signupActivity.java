package com.example.hiveeapp.registration.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class signupActivity extends AppCompatActivity {
    private LinearLayout userTypeButtonsLayout;
    private Button studentButton;
    private Button employerButton;
    private Button companyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize user type buttons
        userTypeButtonsLayout = findViewById(R.id.user_type_buttons_layout);
        studentButton = findViewById(R.id.signup_student_btn);
        employerButton = findViewById(R.id.signup_employer_btn);
        companyButton = findViewById(R.id.signup_company_btn);

        // Set click listener for student button
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup("student");
            }
        });

        // Set click listener for employer button
        employerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup("employer");
            }
        });

        // Set click listener for company button
        companyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup("company");
            }
        });
    }

    private void navigateToSignup(String userType) {
        Intent intent;
        switch (userType) {
            case "student":
                intent = new Intent(signupActivity.this, studentsignupActivity.class);
                break;
            case "employer":
                intent = new Intent(signupActivity.this, employersignupActivity.class);
                break;
            case "company":
                intent = new Intent(signupActivity.this, companysignupActivity.class);
                break;
            default:
                Toast.makeText(signupActivity.this, "User type not recognized.", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
    }
}
