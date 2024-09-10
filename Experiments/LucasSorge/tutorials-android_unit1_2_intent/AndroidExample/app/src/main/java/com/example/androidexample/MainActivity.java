package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;     // define message textview variable
    private Button counterButton;     // define counter button variable
    private Button aboutButton;       // define about button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // link to Main activity XML

        // Initialize UI elements
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML
        aboutButton = findViewById(R.id.main_about_btn);    // link to about button in the Main activity XML

        // Extract data passed into this activity from another activity
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            messageText.setText("Welcome to the Fun Counter App!");
        } else {
            String number = extras.getString("NUM");  // this will come from CounterActivity
            messageText.setText("Last counter value: " + number);
        }

        // Navigate to CounterActivity on button press
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to AboutActivity on button press
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
