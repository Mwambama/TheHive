package com.example.hiveeapp.company_user.invitations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
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
        backArrowIcon.setOnClickListener(v -> {
            finish(); // Go back to the previous activity
        });

        // Set up RecyclerView
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        invitationAdapter = new InvitationAdapter(this);
        invitationsRecyclerView.setAdapter(invitationAdapter);

        // Load invitations automatically
        getInvitations();

        // Handle Add Invitation Button Click
        addInvitationButton.setOnClickListener(v -> {
            Intent intent = new Intent(InvitationManagementActivity.this, InvitationCreationActivity.class);
            startActivity(intent);  // Navigate to Add Invitation Activity
        });
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

    // Adapter for the RecyclerView
    private class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

        private JSONArray invitations = new JSONArray();
        private Context context;

        public InvitationAdapter(Context context) {
            this.context = context;
        }

        public void setInvitations(JSONArray invitations) {
            this.invitations = invitations;
            notifyDataSetChanged();
        }

        @Override
        public InvitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_invitation, parent, false);
            return new InvitationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(InvitationViewHolder holder, int position) {
            try {
                JSONObject invitation = invitations.getJSONObject(position);
                String email = invitation.getString("email");
                String message = invitation.optString("message", "No message");

                holder.emailTextView.setText(email);
                holder.messageTextView.setText(message);

                int invitationId = invitation.getInt("id");

                // Update Button
                holder.updateButton.setOnClickListener(v -> {
                    showUpdateDialog(invitationId, email, message);
                });

                // Delete Button
                holder.deleteButton.setOnClickListener(v -> {
                    deleteInvitation(invitationId);
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return invitations.length();
        }

        class InvitationViewHolder extends RecyclerView.ViewHolder {
            TextView emailTextView, messageTextView;
            ImageButton updateButton, deleteButton;

            public InvitationViewHolder(View itemView) {
                super(itemView);
                emailTextView = itemView.findViewById(R.id.emailTextView);
                messageTextView = itemView.findViewById(R.id.messageTextView);
                updateButton = itemView.findViewById(R.id.updateButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }
    }

    private void showUpdateDialog(int invitationId, String currentEmail, String currentMessage) {
        // Create a dialog for updating invitation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Invitation");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_invitation, null);
        EditText emailField = dialogView.findViewById(R.id.emailField);
        EditText messageField = dialogView.findViewById(R.id.messageField);

        emailField.setText(currentEmail);
        messageField.setText(currentMessage);

        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newEmail = emailField.getText().toString().trim();
            String newMessage = messageField.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            } else {
                updateInvitation(invitationId, newEmail, newMessage);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void updateInvitation(int invitationId, String email, String message) {
        InvitationApi.updateInvitation(
                this,
                invitationId,
                email,
                message,
                response -> {
                    Toast.makeText(this, "Invitation updated successfully.", Toast.LENGTH_SHORT).show();
                    getInvitations(); // Refresh the list
                },
                error -> {
                    Toast.makeText(this, "Failed to update invitation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void deleteInvitation(int invitationId) {
        InvitationApi.deleteInvitation(
                this,
                invitationId,
                response -> {
                    Toast.makeText(this, "Invitation deleted successfully.", Toast.LENGTH_SHORT).show();
                    getInvitations(); // Refresh the list
                },
                error -> {
                    Toast.makeText(this, "Failed to delete invitation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }
}