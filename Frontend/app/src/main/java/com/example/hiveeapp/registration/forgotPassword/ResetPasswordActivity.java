package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";

    private EditText newPasswordField, confirmPasswordField;
    private Button resetPasswordButton;
    private ProgressBar loadingProgressBar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize views
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Get the email from the previous activity
        email = getIntent().getStringExtra("email");
        Log.d(TAG, "Email retrieved from intent: " + email);

        // Reset password button click event
        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            Log.d(TAG, "New Password: " + newPassword);
            Log.d(TAG, "Confirm Password: " + confirmPassword);

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in all fields.");
            } else if (!newPassword.equals(confirmPassword)) {
                showToast("Passwords do not match! Try again!");
            } else {
                resetPassword(email, newPassword, confirmPassword);
            }
        });
    }

    /**
     * Sends the reset password request to the server.
     */
    private void resetPassword(String email, String newPassword, String confirmPassword) {
        showLoading(true); // Show loading while resetting password

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("password", newPassword);
            payload.put("confirmPassword", confirmPassword);

            Log.d(TAG, "Payload: " + payload.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            showSnackbar("Failed to create request.");
            showLoading(false);
            return;
        }

        // URL for resetting the password
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/change-password";
        Log.d(TAG, "URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    showLoading(false);
                    Log.d(TAG, "Response received: " + response.toString());
                    try {
                        String responseMessage = response.getString("message");
                        if (responseMessage.equals("Password changed successfully")) {
                            showSnackbar(responseMessage);
                            // Navigate back to LoginActivity
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast(responseMessage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON Exception in response: " + e.getMessage());
                        showSnackbar("Error parsing server response.");
                    }
                },
                this::handleError // Handle error
        );

        // Increase the timeout in case of slow network
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // Timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Log.d(TAG, "Adding request to queue");
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Handles server errors.
     */
    private void handleError(VolleyError error) {
        showLoading(false);
        if (error.networkResponse != null) {
            // Safely retrieve the response body if available
            String responseBody = error.networkResponse.data != null ? new String(error.networkResponse.data) : "No response body";
            Log.e(TAG, "Error: " + responseBody);
            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
            switch (error.networkResponse.statusCode) {
                case 401:
                    showToast("Unauthorized. Your session has expired. Please log in again.");
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
            if (error.getCause() != null) {
                Log.e(TAG, "VolleyError Cause: " + error.getCause().getMessage());
            } else {
                Log.e(TAG, "Unknown network error", error);
            }
        }
    }

    /**
     * Shows or hides the loading spinner.
     */
    private void showLoading(boolean isLoading) {
        loadingProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        resetPasswordButton.setEnabled(!isLoading);
        Log.d(TAG, "Loading indicator set to " + isLoading);
    }

    /**
     * Shows a Toast message.
     */
    private void showToast(String message) {
        Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Toast displayed: " + message);
    }

    /**
     * Shows a Snackbar message.
     */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
        Log.d(TAG, "Snackbar displayed: " + message);
    }
}