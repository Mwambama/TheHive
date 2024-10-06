package com.example.hiveeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToCompanyActivityButton;
    private Button goToLoginActivityButton;
    private Button goToStudentMainActivityButton; // Declare the student main activity button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton);
        goToStudentMainActivityButton = findViewById(R.id.goToStudentMainActivityButton); // Initialize the student main activity button

        // Navigate to CompanyActivity
        goToCompanyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompanyActivity.class);
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
