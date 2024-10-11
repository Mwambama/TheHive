package com.example.hiveeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.registration.signup.signupActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.company_user.CompanyMainActivity;

public class MainActivity extends AppCompatActivity {
    private Button mainSignupBtn;
    private Button goToCompanyActivityButton;
    private Button goToLoginActivityButton;
    private Button goToStudentMainActivityButton;
    private Button goToEmployerActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Use the correct layout, not 'activity_employer_main'

        // Find the views from the layout
        mainSignupBtn = findViewById(R.id.main_signup_btn);
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton);
        goToStudentMainActivityButton = findViewById(R.id.goToStudentMainActivityButton);
        goToEmployerActivityButton = findViewById(R.id.goToEmployerActivityButton); // Add the correct button ID

        // Set OnClickListener for Signup Button
        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to CompanyActivity
        goToCompanyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompanyMainActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to EmployerMainActivity (login activity for employer)
        goToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployerMainActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to StudentMainActivity
        goToStudentMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });
    }
}