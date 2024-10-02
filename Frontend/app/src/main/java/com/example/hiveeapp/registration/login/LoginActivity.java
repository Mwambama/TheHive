package com.example.hiveeapp.registration.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.admin_user.AdminMainActivity;
import com.example.hiveeapp.company_user.handleEmployers.EmployerCreationActivity;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.registration.forgotPassword.ForgotPasswordActivity;
import com.example.hiveeapp.registration.signup.signupActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private MaterialButton loginButton;
    private TextView forgotPasswordButton, registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        registerText = findViewById(R.id.registerText);

        // Login button click event
        loginButton.setOnClickListener(v -> authenticateUser());

        // Forgot password text click event
        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Register text click event
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, signupActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create JSON payload for authentication
        JSONObject loginPayload = new JSONObject();
        try {
            loginPayload.put("email", email);
            loginPayload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Define the login URL
        String loginUrl = "";

        // Create the login request using Volley
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                loginPayload,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            int userType = response.getInt("user_type");
                            navigateToUserActivity(userType);
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    private void navigateToUserActivity(int userType) {
        Intent intent;
        switch (userType) {
            case 1:
                // Navigate to Student Activity
                intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                break;
            case 2:
                // Navigate to Employer Activity
                intent = new Intent(LoginActivity.this, EmployerMainActivity.class);
                break;
            case 3:
                // Navigate to Company Activity
                intent = new Intent(LoginActivity.this, EmployerCreationActivity.class);
                break;
            case 4:
                // Navigate to Admin Activity
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                break;
            default:
                Toast.makeText(LoginActivity.this, "User type not recognized.", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}