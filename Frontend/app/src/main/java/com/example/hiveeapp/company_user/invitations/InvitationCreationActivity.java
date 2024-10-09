package com.example.hiveeapp.company_user.invitations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * InvitationCreationActivity allows the company user to create and send employer invitations.
 */
public class InvitationCreationActivity extends AppCompatActivity {

    private EditText emailField, messageField;
    private Button sendInvitationButton;
    private ImageButton backArrowIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_creation);

        // Initialize views
        initViews();

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> finish());

        // Send Invitation button logic
        sendInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String message = messageField.getText().toString().trim();

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
            } else if (message.isEmpty()) {
                Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
            } else {
                sendInvitation(email, message);
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
     * Sends the invitation using the InvitationApi.
     *
     * @param email   The email address to send the invitation to.
     * @param message The message to include with the invitation.
     */
    private void sendInvitation(String email, String message) {
        // Send the invitation using InvitationApi
        InvitationApi.sendInvitation(
                this,
                email,
                message,
                response -> {
                    // Invitation sent successfully
                    Toast.makeText(this, "Invitation sent successfully!", Toast.LENGTH_SHORT).show();
                    // Finish the activity and return to the previous screen
                    finish();
                },
                error -> {
                    // Handle the error correctly as VolleyError
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(InvitationCreationActivity.this, "Failed to send invitation: " + errorMessage, Toast.LENGTH_SHORT).show();
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
                JSONObject jsonError = new JSONObject(errorData);
                errorMsg = jsonError.optString("message", errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}