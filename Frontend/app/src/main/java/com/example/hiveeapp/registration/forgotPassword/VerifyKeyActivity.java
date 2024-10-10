package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyKeyActivity extends AppCompatActivity {

    private EditText keyField;
    private Button verifyKeyButton;
    private ProgressBar loadingProgressBar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_key);

        // Initialize views
        keyField = findViewById(R.id.keyField);
        verifyKeyButton = findViewById(R.id.verifyKeyButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Get the email from previous activity
        email = getIntent().getStringExtra("email");

        // Verify OTP button click event
        verifyKeyButton.setOnClickListener(v -> {
            String key = keyField.getText().toString().trim();
            if (key.isEmpty()) {
                showToast("Please enter the verification key.");
            } else {
                verifyOtp(email, key);
            }
        });
    }

    /**
     * Verifies the OTP for the given email.
     */
    private void verifyOtp(String email, String key) {
        showLoading(true); // Show loading while verifying OTP

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("otp", key);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            showLoading(false);
            return;
        }

        // URL for verifying OTP
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/otp/validateOtp";

        // Create a new JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    showLoading(false);
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            showSnackbar("OTP verified successfully.");
                            // Navigate to ResetPasswordActivity
                            Intent intent = new Intent(VerifyKeyActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        } else {
                            showToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showSnackbar("Error parsing server response.");
                    }
                },
                this::handleError // Handle error
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Handles server errors.
     */
    private void handleError(VolleyError error) {
        showLoading(false);
        if (error.networkResponse != null) {
            switch (error.networkResponse.statusCode) {
                case 400:
                    showToast("Invalid request. Please check your input.");
                    break;
                case 404:
                    showToast("Server not found. Please try again later.");
                    break;
                case 500:
                    showToast("Internal server error. Please try again later.");
                    break;
                default:
                    showToast("Unexpected error: " + error.networkResponse.statusCode);
                    break;
            }
        } else {
            showToast("Request failed. Please check your network connection.");
        }
    }

    /**
     * Shows or hides the loading spinner.
     */
    private void showLoading(boolean isLoading) {
        loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        verifyKeyButton.setEnabled(!isLoading);
    }

    /**
     * Shows a Toast message.
     */
    private void showToast(String message) {
        Toast.makeText(VerifyKeyActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a Snackbar message.
     */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}