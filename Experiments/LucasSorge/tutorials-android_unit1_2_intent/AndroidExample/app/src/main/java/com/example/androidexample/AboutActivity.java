package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView aboutTxt;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Initialize and set content
        aboutTxt = findViewById(R.id.about_txt);
        aboutTxt.setText("This is an app that demonstrates switching between activities using Intents.");

        // Initialize back button
        backBtn = findViewById(R.id.about_back_btn);

        // Handle back button press to navigate to MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

