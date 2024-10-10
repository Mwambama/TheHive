package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button sendKeyButton;
    private ImageButton backArrowIcon;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        emailField = findViewById(R.id.forgotPasswordEmailField);
        sendKeyButton = findViewById(R.id.sendKeyButton);
        backArrowIcon = findViewById(R.id.backArrowIcon);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Back arrow click event
        backArrowIcon.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
        });

        // Send OTP button click event
        sendKeyButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Please enter a valid email address.");
            } else {
                sendOtpToEmail(email);
            }
        });
    }

    /**
     * Sends OTP to the given email address.
     */
    private void sendOtpToEmail(String email) {
        showLoading(true); // Show loading while sending OTP

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            showLoading(false);
            return;
        }

        // URL for sending OTP
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/otp/sendOtp";

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
                            // Directly navigate to VerifyKeyActivity
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyKeyActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        } else {
                            // Handle failure case if necessary
                            showSnackbar("Failed to send OTP.");
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
        sendKeyButton.setEnabled(!isLoading);
    }

    /**
     * Shows a Toast message.
     */
    private void showToast(String message) {
        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a Snackbar message.
     */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}