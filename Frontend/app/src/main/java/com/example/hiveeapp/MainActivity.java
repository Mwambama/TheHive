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

//import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;


public class MainActivity extends AppCompatActivity {
    private Button mainSignupBtn;
    private Button studentmainSignupBtn;
    private Button employermainSignupBtn;

    private Button goToCompanyActivityButton;
    private Button goToLoginActivityButton;
    private Button goToStudentMainActivityButton; // Declare the student main activity button
    //private Button goToCompanyActivityButton;
    //private Button goToLoginActivityButton; // Declare the login button
    private Button goToEmployerActivityButton; // Declare the employer profile button
   // private Button go

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_main);

        // Find the views from the layout
        mainSignupBtn = findViewById(R.id.main_signup_btn);
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton);
        goToStudentMainActivityButton = findViewById(R.id.goToStudentMainActivityButton); // Initialize the student main activity button

        // Set OnClickListener for Signup Button
        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Start SignupActivity

                Intent intent = new Intent(MainActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });
        //goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        //goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton); // Initialize the login button
        goToEmployerActivityButton = findViewById(R.id.goToEmployerActivityButton); // Initialize the employer profile button

        // Navigate to CompanyActivity
        goToCompanyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompanyMainActivity.class);
                startActivity(intent);
            }
        });
        // Navigate to LoginActivity
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
