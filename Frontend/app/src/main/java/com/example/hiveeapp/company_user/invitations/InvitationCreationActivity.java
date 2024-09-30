package com.example.hiveeapp.company_user.invitations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

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

    private void initViews() {
        emailField = findViewById(R.id.emailField);
        messageField = findViewById(R.id.messageField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendInvitation(String email, String message) {
        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("message", message);
            payload.put("company_id", 1);  // Dummy company ID, adjust if needed
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the invitation using InvitationApi
        InvitationApi.sendInvitation(
                this,
                1,
                email,
                message,
                response -> {
                    // Invitation sent successfully
                    Toast.makeText(this, "Invitation sent successfully!", Toast.LENGTH_SHORT).show();
                    // Go back to InvitationManagementActivity and refresh the list
                    finish(); // This will go back to the previous activity (InvitationManagementActivity)
                },
                error -> {
                    // Handle the error
                    String errorMessage = getErrorMessage(error);
                    Toast.makeText(this, "Failed to send invitation: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

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