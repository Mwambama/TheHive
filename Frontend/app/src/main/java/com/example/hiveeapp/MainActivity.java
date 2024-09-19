package com.example.hiveeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.company_user.companyActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToCompanyActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button in the layout
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);

        // Set a click listener to navigate to CompanyActivity
        goToCompanyActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, companyActivity.class);
            startActivity(intent);
        });
    }
}
