package com.example.hiveeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.company_user.companyActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToCompanyActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);

        goToCompanyActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CompanyActivity
                Intent intent = new Intent(MainActivity.this, companyActivity.class);
                startActivity(intent);
            }
        });
    }
}

