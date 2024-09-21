package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.JsonArrReqActivity;

public class companyActivity extends AppCompatActivity {

    private Button loadJsonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);  // Set the correct layout

        // Find the button in the layout
        loadJsonButton = findViewById(R.id.loadJsonButton);

        // Set a click listener to start JsonArrReqActivity
        loadJsonButton.setOnClickListener(v -> {
            Intent intent = new Intent(companyActivity.this, JsonArrReqActivity.class);
            startActivity(intent);
        });
    }
}