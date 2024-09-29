package com.example.hiveeapp.company_user.invitations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

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
        emailField = findViewById(R.id.emailField);
        messageField = findViewById(R.id.messageField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> {
            finish(); // Go back to the previous activity
        });

        // Send Invitation
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

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendInvitation(String email, String message) {
        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("message", message);
            payload.put("company_id", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send request using InvitationApi
        InvitationApi.sendInvitation(
                this,
                1,
                email,
                message,
                response -> {
                    Toast.makeText(this, "Invitation sent successfully!", Toast.LENGTH_SHORT).show();
                    // Clear the fields
                    emailField.setText("");
                    messageField.setText("");
                },
                error -> {
                    Toast.makeText(this, "Failed to send invitation: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
    }
}