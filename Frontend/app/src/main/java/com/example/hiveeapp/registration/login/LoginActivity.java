package com.example.hiveeapp.registration.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.admin_user.AdminMainActivity;
import com.example.hiveeapp.company_user.CompanyMainActivity;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.registration.forgotPassword.ForgotPasswordActivity;
import com.example.hiveeapp.registration.signup.signupActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        });

        // Register text click event
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, signupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        });
    }

    private void authenticateUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        // Validate email format and non-empty fields
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address.");
            return;
        }
        if (password.isEmpty()) {
            showToast("Please enter your password.");
            return;
        }

        // Show the loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false); // Disable the login button during network request

        // Construct the login URL
        String loginUrl = "http://coms-3090-063.class.las.iastate.edu:8080/account/login";

        // Create the authentication header with email and password
        String credentials = email + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        // Send login request to server
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                null,  // No body for this POST request
                response -> {
                    hideLoading(); // Hide the loading indicator and enable login button
                    handleLoginSuccess(response);
                },
                error -> {
                    hideLoading(); // Hide the loading indicator and enable login button
                    handleError(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                // Add the authentication header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley queue
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    private void handleLoginSuccess(JSONObject response) {
        try {
            // Parse the server response
            int userId = response.getInt("userId");
            String name = response.getString("name");
            String role = response.getString("role");

            // Navigate to the appropriate activity based on the user role
            navigateToUserActivity(role);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar(getString(R.string.error_parsing_response));
        }
    }

    private void navigateToUserActivity(String role) {
        Intent intent;
        switch (role) {
            case "STUDENT":
                intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                break;
            case "EMPLOYER":
                intent = new Intent(LoginActivity.this, EmployerMainActivity.class);
                break;
            case "COMPANY":
                intent = new Intent(LoginActivity.this, CompanyMainActivity.class);
                break;
            case "ADMIN":
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                break;
            default:
                showSnackbar(getString(R.string.user_type_not_recognized));
                return;
        }
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        finish();
    }

    private void handleError(VolleyError error) {
        hideLoading(); // Ensure loading is hidden on error

        if (error instanceof TimeoutError) {
            showToast("The request timed out. Please try again.");
        } else if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            switch (statusCode) {
                case 400:
                    showToast("Invalid request. Please check your input.");
                    break;
                case 401:
                    showToast("Unauthorized. Please check your credentials.");
                    break;
                case 404:
                    showToast("Server not found. Please try again later.");
                    break;
                case 500:
                    showToast("Internal server error. Please try again later.");
                    break;
                default:
                    showToast("Unexpected error: " + statusCode);
                    break;
            }
        } else {
            showToast("Login failed. Please check your network connection.");
        }
    }

    // Method to show a Snackbar message
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    // Method to hide the loading spinner and re-enable the button
    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
    }

    // Method to show a Toast message
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}