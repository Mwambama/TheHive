package com.example.hiveeapp.company_user.invitations;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddInvitationActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText messageField;
    private Button sendInvitationButton, deleteInvitationButton, getInvitationsButton, updateInvitationButton;
    private TextView responseTextView;
    private ListView invitationsListView;

    private int companyId = 1;
    private int selectedInvitationId = -1;  // To keep track of the selected invitation

    private ArrayList<String> invitationsList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private JSONArray invitationsArray;  // Store the array of invitations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);

        // Initialize views
        emailField = findViewById(R.id.emailField);
        messageField = findViewById(R.id.messageField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);
        deleteInvitationButton = findViewById(R.id.deleteInvitationButton);
        getInvitationsButton = findViewById(R.id.getInvitationsButton);
        updateInvitationButton = findViewById(R.id.updateInvitationButton);
        responseTextView = findViewById(R.id.responseTextView);
        invitationsListView = findViewById(R.id.invitationsListView);

        // Initialize the ListView adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, invitationsList);
        invitationsListView.setAdapter(adapter);
        invitationsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Set item click listener for the ListView
        invitationsListView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                // Get the selected invitation
                JSONObject selectedInvitation = invitationsArray.getJSONObject(position);
                selectedInvitationId = selectedInvitation.getInt("id");
                String email = selectedInvitation.getString("email");
                String message = selectedInvitation.has("message") ? selectedInvitation.getString("message") : "No message available";

                // Display selected invitation details
                emailField.setText(email);
                messageField.setText(message);
                responseTextView.setText("Selected Invitation ID: " + selectedInvitationId);

            } catch (JSONException e) {
                e.printStackTrace();
                responseTextView.setText("Error retrieving invitation details.");
            }
        });

        // Send Invitation
        sendInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String message = messageField.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(AddInvitationActivity.this, "Please enter an email.", Toast.LENGTH_SHORT).show();
            } else {
                sendInvitation(companyId, email, message);
            }
        });

        // Delete Invitation
        deleteInvitationButton.setOnClickListener(v -> {
            if (selectedInvitationId == -1) {
                Toast.makeText(AddInvitationActivity.this, "Please select an invitation to delete.", Toast.LENGTH_SHORT).show();
            } else {
                deleteInvitation(selectedInvitationId);
            }
        });

        // Get Invitations
        getInvitationsButton.setOnClickListener(v -> getInvitations());

        // Update Invitation
        updateInvitationButton.setOnClickListener(v -> {
            String newEmail = emailField.getText().toString().trim();
            String newMessage = messageField.getText().toString().trim();
            if (selectedInvitationId == -1) {
                Toast.makeText(AddInvitationActivity.this, "Please select an invitation to update.", Toast.LENGTH_SHORT).show();
            } else if (newEmail.isEmpty()) {
                Toast.makeText(AddInvitationActivity.this, "Please enter a new email for update.", Toast.LENGTH_SHORT).show();
            } else {
                updateInvitation(selectedInvitationId, newEmail, newMessage);
            }
        });

        // Initially load invitations
        getInvitations();
    }

    // Send Invitation using InvitationApi
    private void sendInvitation(int companyId, String email, String message) {
        InvitationApi.sendInvitation(
                this,
                companyId,
                email,
                message,
                response -> {
                    responseTextView.setText("Invitation sent successfully!");
                    emailField.setText("");
                    messageField.setText("");
                    getInvitations();  // Refresh invitations after sending
                },
                error -> {
                    responseTextView.setText("Failed to send invitation: " + error.getMessage());
                    getInvitations();  // Refresh invitations even if there is an error
                }
        );
    }

    // Delete Invitation using InvitationApi
    private void deleteInvitation(int invitationId) {
        InvitationApi.deleteInvitation(
                this,
                invitationId,
                response -> {
                    responseTextView.setText("Invitation deleted successfully!");
                    // Refresh the list of invitations
                    getInvitations();
                    selectedInvitationId = -1;  // Reset the selected invitation
                    emailField.setText("");
                    messageField.setText("");
                },
                error -> {
                    responseTextView.setText("Failed to delete invitation: " + error.getMessage());
                    getInvitations();  // Refresh invitations even if there is an error
                }
        );
    }

    // Get Invitations using InvitationApi
    private void getInvitations() {
        InvitationApi.getInvitations(
                this,
                response -> {
                    invitationsList.clear();
                    invitationsArray = response;
                    try {
                        // Loop through the JSON array and extract invitation details
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject invitation = response.getJSONObject(i);
                            int id = invitation.getInt("id");
                            String email = invitation.getString("email");

                            // Check if "message" exists, else provide a default value
                            String message = invitation.has("message") ? invitation.getString("message") : "No message available";

                            // Add invitation details to the list
                            invitationsList.add("ID: " + id + ", Email: " + email + ", Message: " + message);
                        }

                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();
                        responseTextView.setText("Invitations loaded successfully.");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        responseTextView.setText("Error parsing invitations: " + e.getMessage());
                    }
                },
                error -> responseTextView.setText("Failed to retrieve invitations: " + error.getMessage())
        );
    }

    // Update Invitation using InvitationApi
    private void updateInvitation(int invitationId, String newEmail, String newMessage) {
        InvitationApi.updateInvitation(
                this,
                invitationId,
                newEmail,
                newMessage,
                response -> {
                    responseTextView.setText("Invitation updated successfully!");
                    emailField.setText("");  // Clear the input field
                    messageField.setText(""); // Clear the message field
                    // Refresh the list of invitations
                    getInvitations();
                    selectedInvitationId = -1;  // Reset the selected invitation
                },
                error -> {
                    responseTextView.setText("Failed to update invitation: " + error.getMessage());
                    getInvitations();  // Refresh invitations even if there is an error
                }
        );
    }
}