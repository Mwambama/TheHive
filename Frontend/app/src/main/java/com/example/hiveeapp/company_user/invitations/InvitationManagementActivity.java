package com.example.hiveeapp.company_user.invitations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.company_user.handleEmployers.EmployerListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InvitationManagementActivity extends AppCompatActivity {

    private RecyclerView invitationsRecyclerView;
    private InvitationAdapter invitationAdapter;
    private Button addInvitationButton;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_management);

        // Initialize views
        invitationsRecyclerView = findViewById(R.id.invitationsRecyclerView);
        addInvitationButton = findViewById(R.id.addInvitationButton);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set up RecyclerView
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        invitationAdapter = new InvitationAdapter(this, true);
        invitationsRecyclerView.setAdapter(invitationAdapter);

        // Load invitations automatically
        getInvitations();

        // Handle Add Invitation Button Click
        addInvitationButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitationManagementActivity.this, InvitationCreationActivity.class);
            startActivity(intent);
        });

        // Set up Bottom Navigation Listener using the updated format
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                // Stay on this activity since it's the invitation page
                Toast.makeText(InvitationManagementActivity.this, "You are already on this page", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_employers) {
                // Navigate to Employer List Activity
                startActivity(new Intent(InvitationManagementActivity.this, EmployerListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                // Navigate to the Company Main User Page
                startActivity(new Intent(InvitationManagementActivity.this, CompanyActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the invitation list when returning to this activity
        getInvitations();
    }

    private void getInvitations() {
        InvitationApi.getInvitations(
                this,
                response -> {
                    invitationAdapter.setInvitations(response);
                },
                error -> {
                    Toast.makeText(this, "Failed to retrieve invitations: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }
}