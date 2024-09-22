package com.example.hiveeapp.company_user.invitations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import org.json.JSONObject;

public class AddInvitationActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendInvitationButton;
    private int companyId = 1; // It is going to be dynamic in the future.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);

        // Initialize views
        emailField = findViewById(R.id.emailField);
        sendInvitationButton = findViewById(R.id.sendInvitationButton);

        // Set click listener for the button
        sendInvitationButton.setOnClickListener(v -> {
            String email = emailField.getText().toString(); // Get the email from the input field

            if (email.isEmpty()) {
                // Show a toast message if the email field is empty
                Toast.makeText(AddInvitationActivity.this, "Please enter an email.", Toast.LENGTH_SHORT).show();
            } else {
                // Call the method to send the invitation
                sendInvitation(companyId, email);
            }
        });
    }

    // Method to send the invitation using InvitationApi
    private void sendInvitation(int companyId, String email) {
        InvitationApi.sendInvitation(
                this,
                companyId,
                email,
                new Response.Listener<JSONObject>() {  // Success listener
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        Toast.makeText(AddInvitationActivity.this, "Invitation sent successfully!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {  // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        if (error.networkResponse != null) {
                            Toast.makeText(AddInvitationActivity.this, "Error: " + new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddInvitationActivity.this, "Failed to send invitation.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}