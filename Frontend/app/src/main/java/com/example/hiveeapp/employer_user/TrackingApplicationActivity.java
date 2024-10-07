package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import java.util.ArrayList;
import java.util.List;

public class TrackingApplicationActivity extends AppCompatActivity {

    private Button backButton;
    private RecyclerView trackingRecyclerView;
    private TrackingAdapter trackingAdapter;
    private List<String> trackingItems; // Replace String with your data model

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

        // Initialize RecyclerView and its adapter
        trackingRecyclerView = findViewById(R.id.tracking_recycler_view);
        trackingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load tracking items
        loadTrackingData();
    }

    private void loadTrackingData() {
        // For demonstration purposes, we'll use sample data
        trackingItems = new ArrayList<>();
        trackingItems.add("Application 1: Status - In Review");
        trackingItems.add("Application 2: Status - Accepted");
        trackingItems.add("Application 3: Status - Rejected");
        trackingItems.add("Application 4: Status - Interview Scheduled");

        // Set up the adapter
        trackingAdapter = new TrackingAdapter(trackingItems);
        trackingRecyclerView.setAdapter(trackingAdapter);
    }
}
