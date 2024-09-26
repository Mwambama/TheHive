package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
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
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

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

        String url = "https://run.mocky.io/v3/f5fb1677-3aae-40ff-803d-cc9fe1a630c7";  // Mock server

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
}