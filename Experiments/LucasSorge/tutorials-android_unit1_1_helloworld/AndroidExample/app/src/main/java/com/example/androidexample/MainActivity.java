package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvMainMessage;    // Main message TextView
    private TextView tvAdditionalMessage;  // Additional message TextView
    private Button btnCounter;         // Button to increase the counter and change the text
    private int clickCounter = 0;      // Counter to track button clicks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Link to activity_main.xml

        // Initialize UI elements
        tvMainMessage = findViewById(R.id.tv_main_message);      // Main message TextView
        tvMainMessage.setText("Welcome to the App!");            // Set initial message

        tvAdditionalMessage = findViewById(R.id.tv_additional_message);  // Additional message TextView, initially hidden

        btnCounter = findViewById(R.id.btn_counter);             // Button to trigger counter and message change

        // Set OnClickListener for the button
        btnCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment the click counter
                clickCounter++;

                // Update the main message with the counter value
                String updatedText = "Button clicked " + clickCounter + " times";
                tvMainMessage.setText(updatedText);

                // Display a toast message
                Toast.makeText(MainActivity.this, updatedText, Toast.LENGTH_SHORT).show();

                // Show the additional message after 5 clicks
                if (clickCounter == 5) {
                    tvAdditionalMessage.setVisibility(View.VISIBLE); // Make the additional message visible
                    tvAdditionalMessage.setText("You clicked 5 times!");
                    Toast.makeText(MainActivity.this, "You unlocked a new message!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
