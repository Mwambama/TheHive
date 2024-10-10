package com.example.hiveeapp.company_user.invitations;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * InvitationCreationActivity manages the creation and editing of employer invitations.
 * It allows the user to either create a new invitation or update an existing one.
 */
public class InvitationCreationActivity extends AppCompatActivity {

    private EditText emailField, messageField;
    private Button sendInvitationButton;
    private ImageButton backArrowIcon;
    private TextView invitationCreationTitle;

    private boolean isEditing = false;  // Flag to determine if the activity is in editing mode
    private int invitationId = -1;      // ID of the invitation being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_creation);

        // Initialize the views
        initViews();

        // Set up the back button functionality
        backArrowIcon.setOnClickListener(v -> finish());

        // Check if the activity is in editing mode and populate fields if necessary
        if (getIntent().hasExtra("invitationId")) {
            isEditing = true;
            invitationId = getIntent().getIntExtra("invitationId", -1);
            String email = getIntent().getStringExtra("email");
            String message = getIntent().getStringExtra("message");

            // Populate the input fields with existing invitation data
            emailField.setText(email);
            messageField.setText(message);

            // Change UI to reflect editing mode
            sendInvitationButton.setText("Update Invitation");
            invitationCreationTitle.setText("Edit Invitation");
        } else {
            // Default UI for creating a new invitation
            sendInvitationButton.setText("Send Invitation");
            invitationCreationTitle.setText("Create Invitation");
        }

        // Set up the send/update button logic
        sendInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String message = messageField.getText().toString().trim();

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            } else if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
            } else {
                if (isEditing) {
                    // Update the existing invitation
                    updateInvitation(invitationId, email, message);
                } else {
                    // Create a new invitation
                    sendInvitation(email, message);
                }
            }
        });
    }

    /**
     * Initializes the views in the activity.
     */
    private void initViews() {
        emailField = findViewById(R.id.emailField);
        messageField = findViewById(R.id.messageField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        invitationCreationTitle = findViewById(R.id.invitationCreationTitle);
    }

    /**
     * Validates the email format.
     *
     * @param email The email string to validate.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Sends a new invitation using the InvitationApi.
     *
     * @param email   The recipient email address.
     * @param message The message to include with the invitation.
     */
    private void sendInvitation(String email, String message) {
        InvitationApi.sendInvitation(
                this,
                email,
                message,
                response -> {
                    // Handle successful invitation creation
                    Toast.makeText(this, "Invitation sent successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to the previous screen
                },
                error -> {
                    // Handle any errors during the invitation creation process
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(this, "Failed to send invitation: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * Updates an existing invitation using the InvitationApi.
     *
     * @param invitationId The ID of the invitation to update.
     * @param email        The updated email address.
     * @param message      The updated message content.
     */
    private void updateInvitation(int invitationId, String email, String message) {
        InvitationApi.updateInvitation(
                this,
                invitationId,
                email,
                message,
                response -> {
                    // Handle successful invitation update
                    Toast.makeText(this, "Invitation updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Return to the previous screen
                },
                error -> {
                    // Handle any errors during the invitation update process
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(this, "Failed to update invitation: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * Extracts and returns a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object containing the error details.
     * @return A user-friendly error message.
     */
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");

                // Attempt to parse errorData as JSON
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    // If parsing fails, use the raw errorData
                    errorMsg = errorData;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}
