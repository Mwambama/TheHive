package com.example.hiveeapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.registration.signup.signupActivity;
import com.example.hiveeapp.registration.signup.studentsignupActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.company_user.CompanyMainActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

public class MainActivity extends AppCompatActivity {
    private Button mainSignupBtn;
    private Button studentmainSignupBtn;
    private Button employermainSignupBtn;

    private Button goToCompanyActivityButton;
    private Button goToStudentActivityButton;
    private Button goToLoginActivityButton; // Declare the login button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views from the layout
        mainSignupBtn = findViewById(R.id.main_signup_btn);
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        goToStudentActivityButton = findViewById(R.id.goToStudentActivityButton);
        goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton); // Initialize the login button

        // Set OnClickListener for Signup Button
        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Start SignupActivity

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

        // Navigate to StudentActivity
        goToStudentActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to employer Activity
        goToStudentActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmployerMainActivity.class);
                startActivity(intent);
            }
        });


        // Navigate to LoginActivity
        goToLoginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
