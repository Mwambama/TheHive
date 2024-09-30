package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendKeyButton;
    private static final String TAG = "ForgotPasswordActivity";

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
            } else if (!isValidEmail(email)) {
                // Show error message if the email is not valid
                Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            } else {
                sendVerificationKey(email);

                // Immediately navigate to VerifyKeyActivity after sending the request
                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                intent.putExtra("email", email); // Pass the email to the next activity
                startActivity(intent);
            }
        });
    }

    // Method to check if the email is valid
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

        // Placeholder URL for sending verification key (mock server)
        String url = "https://run.mocky.io/v3/8fc2528f-16f4-4c7f-982e-3ccef695e48a";

        // Create a new JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    // Log response for debugging
                    Log.d(TAG, "Server Response: " + response.toString());
                    Toast.makeText(ForgotPasswordActivity.this, "Server Response: " + response.toString(), Toast.LENGTH_LONG).show();
                    // Navigate to VerifyKeyActivity
                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                },
                error -> {
                    // Log the error
                    if (error.networkResponse != null) {
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        Log.e(TAG, "Error Status Code: " + statusCode);
                        Log.e(TAG, "Error Data: " + new String(error.networkResponse.data));
                    } else {
                        Log.e(TAG, "Error sending key request: " + error.getMessage());
                    }
                    error.printStackTrace();
                    Toast.makeText(ForgotPasswordActivity.this, "Error sending key request: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Set the retry policy
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

}
