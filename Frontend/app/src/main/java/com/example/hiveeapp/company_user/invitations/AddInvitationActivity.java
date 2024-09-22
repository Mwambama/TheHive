package com.example.hiveeapp.company_user.invitations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddInvitationActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendInvitationButton, deleteInvitationButton, getInvitationsButton, updateInvitationButton;
    private TextView responseTextView;
    private int companyId = 1;  // Example company ID
    private int invitationId = 1;  // Example invitation ID (this will change in real use)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);

        // Initialize views
        emailField = findViewById(R.id.emailField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);
        deleteInvitationButton = findViewById(R.id.deleteInvitationButton);
        getInvitationsButton = findViewById(R.id.getInvitationsButton);
        updateInvitationButton = findViewById(R.id.updateInvitationButton);
        responseTextView = findViewById(R.id.responseTextView);

        // Send Invitation
        sendInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(AddInvitationActivity.this, "Please enter an email.", Toast.LENGTH_SHORT).show();
            } else {
                sendInvitation(companyId, email);
            }
        });

        // Delete Invitation
        deleteInvitationButton.setOnClickListener(v -> deleteInvitation(invitationId));

        // Get Invitations
        getInvitationsButton.setOnClickListener(v -> getInvitations());

        // Update Invitation
        updateInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(AddInvitationActivity.this, "Please enter a new email for update.", Toast.LENGTH_SHORT).show();
            } else {
                updateInvitation(invitationId, email);
            }
        });
    }

    // Send Invitation using InvitationApi
    private void sendInvitation(int companyId, String email) {
        InvitationApi.sendInvitation(
                this,
                companyId,
                email,
                response -> responseTextView.setText("Invitation sent successfully! Response: " + response.toString()),
                error -> responseTextView.setText("Failed to send invitation: " + error.getMessage())
        );
    }

    // Delete Invitation using InvitationApi
    private void deleteInvitation(int invitationId) {
        InvitationApi.deleteInvitation(
                this,
                invitationId,
                response -> responseTextView.setText("Invitation deleted successfully! Response: " + response.toString()),
                error -> responseTextView.setText("Failed to delete invitation: " + error.getMessage())
        );
    }

    // Get Invitations using InvitationApi
    private void getInvitations() {
        InvitationApi.getInvitations(
                this,
                new Response.Listener<JSONArray>() {  // Expect a JSONArray
                    @Override
                    public void onResponse(JSONArray response) {
                        StringBuilder invitations = new StringBuilder();
                        try {
                            // Loop through the JSON array and extract invitation details
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject invitation = response.getJSONObject(i);
                                int id = invitation.getInt("id");
                                int companyId = invitation.getInt("company_id");
                                String email = invitation.getString("email");

                                // Append invitation details to the string builder
                                invitations.append("ID: ").append(id)
                                        .append(", Company ID: ").append(companyId)
                                        .append(", Email: ").append(email)
                                        .append("\n");
                            }

                            // Display the invitations in the TextView
                            responseTextView.setText("Invitations:\n" + invitations.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            responseTextView.setText("Error parsing invitations");
                        }
                    }
                },
                error -> responseTextView.setText("Failed to retrieve invitations: " + error.getMessage())
        );
    }

    // Update Invitation using InvitationApi
    private void updateInvitation(int invitationId, String newEmail) {
        InvitationApi.updateInvitation(
                this,
                invitationId,
                newEmail,
                response -> responseTextView.setText("Invitation updated successfully! Response: " + response.toString()),
                error -> responseTextView.setText("Failed to update invitation: " + error.getMessage())
        );
    }
}
