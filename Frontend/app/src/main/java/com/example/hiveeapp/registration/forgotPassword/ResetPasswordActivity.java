package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordField, confirmPasswordField;
    private Button resetPasswordButton;
    private ImageView toggleNewPasswordVisibility, toggleConfirmPasswordVisibility;
    private ProgressBar passwordStrengthBar;
    private TextView passwordStrengthLabel;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        toggleNewPasswordVisibility = findViewById(R.id.toggleNewPasswordVisibility);
        toggleConfirmPasswordVisibility = findViewById(R.id.toggleConfirmPasswordVisibility);
        passwordStrengthBar = findViewById(R.id.passwordStrengthBar);
        passwordStrengthLabel = findViewById(R.id.passwordStrengthLabel);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        // Toggle visibility for new password field
        toggleNewPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(newPasswordField, toggleNewPasswordVisibility, isNewPasswordVisible));

        // Toggle visibility for confirm password field
        toggleConfirmPasswordVisibility.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordField, toggleConfirmPasswordVisibility, isConfirmPasswordVisible));

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
                Toast.makeText(ResetPasswordActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ResetPasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            } else {
                resetPassword(email, newPassword);
            }
        });
    }

    // Function to toggle password visibility
    private void togglePasswordVisibility(EditText passwordField, ImageView toggleIcon, boolean isVisible) {
        if (isVisible) {
            // Hide password
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility_off);
            isVisible = false;
        } else {
            // Show password
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility_on);
            isVisible = true;
        }
        // Move cursor to the end of the text
        passwordField.setSelection(passwordField.getText().length());
    }

    // Function to reset the password by making the HTTP request
    private void resetPassword(String email, String newPassword) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("new_password", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/reset-password";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to LoginActivity
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error resetting password: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Function to update the password strength based on the entered password
    private void updatePasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        passwordStrengthBar.setProgress(strength);

        // Update label based on strength
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

    // Function to calculate the password strength
    private int calculatePasswordStrength(String password) {
        int strength = 0;

        if (password.length() >= 8) strength += 20;
        if (password.matches(".*[A-Z].*")) strength += 20;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*\\d.*")) strength += 20;
        if (password.matches(".*[!@#$%^&*+=?-].*")) strength += 20;

        return strength;
    }
}