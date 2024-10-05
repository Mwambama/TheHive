package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendKeyButton;
    private ImageButton backArrowIcon;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.forgotPasswordEmailField);
        sendKeyButton = findViewById(R.id.sendKeyButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Back arrow click event
        backArrowIcon.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
        });

        sendKeyButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            } else {
                sendVerificationKey(email);
            }
        });
    }

    private void sendVerificationKey(String email) {
        // Show loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);
        sendKeyButton.setEnabled(false);  // Disable button during the request

        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            hideLoading();
            return;
        }

        // URL for sending verification key
        String url = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/send-key";

        // Create a new JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    hideLoading();
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            showSnackbar("Verification key sent to your email.");
                            // Navigate to VerifyKeyActivity with animation
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        } else {
                            showToast(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showSnackbar("Error parsing server response.");
                    }
                },
                error -> {
                    // Handle error
                    handleError(error);
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Error handling function
    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(ForgotPasswordActivity.this, "The request timed out. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            switch (statusCode) {
                case 400:
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid request. Please check your input.", Toast.LENGTH_SHORT).show();
                    break;
                case 401:
                    Toast.makeText(ForgotPasswordActivity.this, "Unauthorized. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(ForgotPasswordActivity.this, "Server not found. Please try again later.", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    Toast.makeText(ForgotPasswordActivity.this, "Internal server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ForgotPasswordActivity.this, "Unexpected error: " + statusCode, Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Request failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }
}