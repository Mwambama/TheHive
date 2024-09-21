package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.JsonArrReqActivity;

public class CompanyActivity extends AppCompatActivity {

    private Button loadJsonButton;
    private Button goToCompanyMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);  // Set the correct layout

        // Find the button in the layout
        loadJsonButton = findViewById(R.id.loadJsonButton);
        goToCompanyMainButton = findViewById(R.id.goToCompanyMainButton);

        // Set a click listener to start JsonArrReqActivity
        loadJsonButton.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, JsonArrReqActivity.class);
            startActivity(intent);
        });

        // Set a click listener to navigate to CompanyMainActivity
        goToCompanyMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, AddDeleteCompanyActivity.class);
            startActivity(intent);
        });

    }
}