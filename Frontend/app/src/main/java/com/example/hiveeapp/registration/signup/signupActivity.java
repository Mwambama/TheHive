package com.example.hiveeapp.registration.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;

public class signupActivity extends AppCompatActivity {
    private LinearLayout userTypeButtonsLayout;
    private Button studentButton;
    private Button employerButton;
    private Button companyButton;
    private TextView goToLoginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize user type buttons
        userTypeButtonsLayout = findViewById(R.id.user_type_buttons_layout);
        studentButton = findViewById(R.id.signup_student_btn);
        employerButton = findViewById(R.id.signup_employer_btn);
        companyButton = findViewById(R.id.signup_company_btn);
        goToLoginText = findViewById(R.id.go_to_login_text);

        // Set click listener for student button
        studentButton.setOnClickListener(v -> navigateToSignup("student"));

        // Set click listener for employer button
        employerButton.setOnClickListener(v -> navigateToSignup("employer"));

        // Set click listener for company button
        companyButton.setOnClickListener(v -> navigateToSignup("company"));

        // Set click listener for "Go to Login" text
        goToLoginText.setOnClickListener(v -> {
            Intent intent = new Intent(signupActivity.this, LoginActivity.class);
            startActivity(intent);
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
