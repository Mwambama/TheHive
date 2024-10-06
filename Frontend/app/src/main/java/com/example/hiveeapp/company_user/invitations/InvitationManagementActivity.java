package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InvitationManagementActivity extends AppCompatActivity {

    private RecyclerView invitationsRecyclerView;
    private InvitationAdapter invitationAdapter;
    private ImageButton backArrowIcon;
    private Button addInvitationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_management);

        // Initialize views
        invitationsRecyclerView = findViewById(R.id.invitationsRecyclerView);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        addInvitationButton = findViewById(R.id.addInvitationButton);

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> finish());

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