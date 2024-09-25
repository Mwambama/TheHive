package com.example.hiveeapp.company_user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.CompanyActivityApi;
import com.example.hiveeapp.company_user.invitations.AddInvitationActivity;

public class CompanyActivity extends AppCompatActivity {

    private ImageButton backArrowIcon;
    private ImageButton manageEmployersIcon;
    private ImageButton mainUserPageIcon;
    private ImageButton manageInvitationsIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        // Find the ImageButtons in the layout
        backArrowIcon = findViewById(R.id.backArrowIcon);
        manageEmployersIcon = findViewById(R.id.manageEmployersIcon);
        mainUserPageIcon = findViewById(R.id.mainUserPageIcon);
        manageInvitationsIcon = findViewById(R.id.manageInvitationsIcon);

        // Set click listener for back arrow
        backArrowIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Set click listener to navigate to CompanyActivityApi
        manageEmployersIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, CompanyActivityApi.class);
            startActivity(intent);
        });

        // Set up the main user page icon click listener
        mainUserPageIcon.setOnClickListener(v -> {
            Toast.makeText(CompanyActivity.this, "You are already on this page", Toast.LENGTH_SHORT).show();
        });

        // Set click listener to navigate to AddInvitationActivity
        manageInvitationsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(CompanyActivity.this, AddInvitationActivity.class);
            startActivity(intent);
        });
    }
}