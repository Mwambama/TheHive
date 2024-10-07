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
    private ProgressBar passwordStrengthBar, loadingProgressBar;
    private TextView passwordStrengthLabel;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize views
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        passwordStrengthLabel = findViewById(R.id.passwordStrengthLabel);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        passwordStrengthBar = findViewById(R.id.passwordStrengthMeter);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        // Add TextWatcher to the password field to update the strength bar
        newPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

    private void resetPassword(String email, String newPassword) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        resetPasswordButton.setEnabled(false);

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            hideLoading();
            return;
        }

        String url = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/reset-password";

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
                            showSnackbar("Password reset successful!");
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                            finish();
                        } else {
                            showToast(message);
                        }
                    } catch (JSONException e) {
                        handleJSONException(e);
                    }
                },
                this::handleVolleyError
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void handleJSONException(JSONException e) {
        e.printStackTrace();
        Toast.makeText(this, "Error parsing server response. Please try again.", Toast.LENGTH_SHORT).show();
    }

    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
            Toast.makeText(this, "Invalid request. Please check your inputs.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof com.android.volley.TimeoutError) {
            Toast.makeText(this, "Connection timeout. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof com.android.volley.NoConnectionError) {
            Toast.makeText(this, "No internet connection. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An unexpected error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }

    private void updatePasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        passwordStrengthBar.setProgress(strength);

        if (strength < 30) {
            passwordStrengthLabel.setText("Weak");
            passwordStrengthLabel.setTextColor(getResources().getColor(R.color.weak));
        } else if (strength < 60) {
            passwordStrengthLabel.setText("Medium");
            passwordStrengthLabel.setTextColor(getResources().getColor(R.color.medium));
        } else {
            passwordStrengthLabel.setText("Strong");
            passwordStrengthLabel.setTextColor(getResources().getColor(R.color.strong));
        }
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        if (password.length() >= 8) strength += 20;
        if (password.matches(".*[A-Z].*")) strength += 20;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*\\d.*")) strength += 20;
        if (password.matches(".*[!@#$%^&*+=?-].*")) strength += 20;

        return strength;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        resetPasswordButton.setEnabled(true);
    }
}
