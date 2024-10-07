package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class TrackingApplicationActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_application); // Ensure this matches your layout file name

        // Initialize the Back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the activity and go back to the previous one
                finish();
            }
        });

        // Load any data or additional functionality you need here
        loadTrackingData();
    }
    private void loadTrackingData() {
        // Implement logic to load and display tracking data
    }
}
