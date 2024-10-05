package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendKeyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.forgotPasswordEmailField);
        sendKeyButton = findViewById(R.id.sendKeyButton);

        sendKeyButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            } else {
                sendVerificationKey(email);
            }
        });
    }

    private void sendVerificationKey(String email) {
        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Placeholder URL for sending verification key
        String url = "https://run.mocky.io/v3/8fc2528f-16f4-4c7f-982e-3ccef695e48a";  // Mock server

        // Create a new JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            Toast.makeText(this, "Verification key sent to your email.", Toast.LENGTH_SHORT).show();
                            // Navigate to VerifyKeyActivity with animation
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  // Animation transition
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error sending verification key: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  // Animation when back button is pressed
    }
}