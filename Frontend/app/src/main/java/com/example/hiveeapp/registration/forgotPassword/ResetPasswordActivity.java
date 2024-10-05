package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
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
    private String email;
    private ProgressBar passwordStrengthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        passwordStrengthBar = findViewById(R.id.passwordStrengthBar);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        // Add TextWatcher to update password strength as the user types
        newPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int strength = calculatePasswordStrength(s.toString());
                passwordStrengthBar.setProgress(strength);
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

    // Function to calculate password strength
    private int calculatePasswordStrength(String password) {
        int score = 0;

        if (password.length() >= 8) score += 25; // Password length >= 8
        if (password.matches(".*[a-z].*")) score += 25; // Contains lowercase letters
        if (password.matches(".*[A-Z].*")) score += 25; // Contains uppercase letters
        if (password.matches(".*\\d.*")) score += 25; // Contains digits

        return score;
    }

    private void resetPassword(String email, String newPassword) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            return;
        }

        String url = "";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            showSnackbar("Password reset successful!");
                            // Navigate to LoginActivity with animation
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);  // Apply animation
                            finish();
                        } else {
                            showToast(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showSnackbar("Error parsing server response.");
                    }
                },
                error -> {
                    showToast("Error resetting password: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);  // Apply back navigation animation
    }
}