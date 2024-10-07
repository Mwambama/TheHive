package com.example.hiveeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import com.example.hiveeapp.signup.signUP.SignupActivity;

public class MainActivity extends AppCompatActivity {

    private Button mainSignupBtn;
=======

import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.registration.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToCompanyActivityButton;
    private Button goToLoginActivityButton; // Declare the login button
>>>>>>> origin/main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        // Find the views from the layout
        mainSignupBtn = findViewById(R.id.main_signup_btn);

        // Set OnClickListener for Signup Button
        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignupActivity
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
=======
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);
        goToLoginActivityButton = findViewById(R.id.goToLoginActivityButton); // Initialize the login button

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
>>>>>>> origin/main
                startActivity(intent);
            }
        });
    }
}