package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
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

        // Get the email from previous activity
        email = getIntent().getStringExtra("email");

        // Reset password button click event
        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in all fields.");
            } else if (!newPassword.equals(confirmPassword)) {
                showToast("Passwords do not match.");
            } else {
                resetPassword(email, newPassword);
            }
        });
    }

    /**
     * Sends the reset password request to the server.
     */
    private void resetPassword(String email, String newPassword) {
        showLoading(true); // Show loading while resetting password

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("newPassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            showLoading(false);
            return;
        }

        // URL for resetting the password
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/resetPassword";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    showLoading(false);
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            showSnackbar("Password reset successfully.");
                            // Navigate back to LoginActivity
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
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
        resetPasswordButton.setEnabled(!isLoading);
    }

    /**
     * Shows a Toast message.
     */
    private void showToast(String message) {
        Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a Snackbar message.
     */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}