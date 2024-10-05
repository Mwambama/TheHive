package com.example.hiveeapp.registration.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
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
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private MaterialButton loginButton;
    private TextView forgotPasswordButton, registerText;
    private ProgressBar loadingProgressBar;

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
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Login button click event
        loginButton.setOnClickListener(v -> authenticateUser());

        // Forgot password text click event
        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);  // Apply animation
        });

        registerText.setOnClickListener(v -> {
            // Navigate to the signup activity
            Intent intent = new Intent(LoginActivity.this, signupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);  // Apply animation
        });
    }

    private void authenticateUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        // Validate email format and non-empty fields
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false); // Disable the login button during network request

        // Create JSON payload for authentication
        JSONObject loginPayload = new JSONObject();
        try {
            loginPayload.put("email", email);
            loginPayload.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "Failed to create login request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send login request to server
        String loginUrl = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/login";

        // Create the login request using Volley
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                loginPayload,
                response -> {
                    loadingProgressBar.setVisibility(View.GONE); // Hide the loading indicator
                    loginButton.setEnabled(true); // Re-enable login button

                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            int userType = response.getInt("user_type");
                            navigateToUserActivity(userType);
                        } else {
                            String message = response.getString("message");
                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();  // Snackbar for error feedback
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_parsing_response, Snackbar.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // Handle error
                    handleError(error);
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    // Error handling function
    private void handleError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(LoginActivity.this, "The request timed out. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            switch (statusCode) {
                case 400:
                    Toast.makeText(LoginActivity.this, "Invalid request. Please check your input.", Toast.LENGTH_SHORT).show();
                    break;
                case 401:
                    Toast.makeText(LoginActivity.this, "Unauthorized. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    break;
                case 404:
                    Toast.makeText(LoginActivity.this, "Server not found. Please try again later.", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    Toast.makeText(LoginActivity.this, "Internal server error. Please try again later.", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Unexpected error: " + statusCode, Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(LoginActivity.this, "Login failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToUserActivity(int userType) {
        Intent intent;
        switch (userType) {
            case 1:
                intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                break;
            case 2:
                intent = new Intent(LoginActivity.this, EmployerMainActivity.class);
                break;
            case 3:
                intent = new Intent(LoginActivity.this, EmployerCreationActivity.class);
                break;
            case 4:
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                break;
            default:
                Snackbar.make(findViewById(android.R.id.content), R.string.user_type_not_recognized, Snackbar.LENGTH_LONG).show();
                return;
        }
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);  // Apply animation
        finish();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        } else {
            passwordField.setInputType(InputType.TYPE_CLASS_TEXT);
            togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_on);
            isPasswordVisible = true;
        }
        passwordField.setSelection(passwordField.getText().length());
    }
}