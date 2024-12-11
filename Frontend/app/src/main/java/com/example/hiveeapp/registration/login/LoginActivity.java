package com.example.hiveeapp.registration.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.hiveeapp.R;
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

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFERENCES_NAME = "UserPreferences";
    private static final String STUDENT_ID_KEY = "userId";

    private EditText emailField, passwordField;
    private MaterialButton loginButton;
    private TextView forgotPasswordButton, registerText;
    private ProgressBar loadingProgressBar;
    private TextView roleMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        initializeViews();

        // Set event listeners for buttons
        setListeners();

        //Test purpose
        emailField.setText("test643@example.com");
        passwordField.setText("Test$1234");

        //Test purpose
//        emailField.setText("employerTest@aols.com");
//        passwordField.setText("Test12345@");
    }

    private void initializeViews() {
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        registerText = findViewById(R.id.registerText);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        roleMessageTextView = findViewById(R.id.roleMessageTextView);
    }

    private void setListeners() {
        loginButton.setOnClickListener(v -> authenticateUser());

        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        });

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, signupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
        });
    }

    private void authenticateUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        // Debug: Log email and password inputs
        Log.d(TAG, "authenticateUser: email=" + email + ", password=" + password);

        if (!validateInputs(email, password)) return;

        String loginUrl = "http://coms-3090-063.class.las.iastate.edu:8080/account/login";

        String credentials = email + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                null,
                response -> handleLoginSuccess(response, email, password),
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Log.d(TAG, "Sending login request to URL: " + loginUrl);
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
    }

    private boolean validateInputs(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address.");
            return false;
        }
        if (password.isEmpty()) {
            showToast("Please enter your password.");
            return false;
        }
        return true;
    }

    private void handleLoginSuccess(JSONObject response, String email, String password) {
        try {
            int userId = response.getInt("userId");
            String role = extractUserRole(response);

            // Debug: Log retrieved userId and role
            Log.d(TAG, "handleLoginSuccess: userId=" + userId + ", role=" + role);

            SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(STUDENT_ID_KEY, userId);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.commit();

            // Debug: Log all preferences
            Map<String, ?> allEntries = preferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d(TAG, "SharedPreferences key: " + entry.getKey() + ", value: " + entry.getValue());
            }

            navigateToUserActivity(role);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Error parsing server response.");
        } finally {
            showLoading(false);
        }
    }

    private String extractUserRole(JSONObject response) throws JSONException {
        if (response.has("role")) {
            return response.getString("role").trim().toUpperCase();
        } else if (response.has("user")) {
            JSONObject userObject = response.getJSONObject("user");
            if (userObject.has("role")) {
                return userObject.getString("role").trim().toUpperCase();
            }
        }
        return "UNKNOWN";
    }

    private void navigateToUserActivity(String role) {
        Log.d(TAG, "Navigating to activity based on role: " + role);

        switch (role) {
            case "COMPANY":
            case "EMPLOYER":
                Intent employerIntent = new Intent(LoginActivity.this, EmployerMainActivity.class);
                startActivity(employerIntent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                finish();
                break;

            case "STUDENT":
                Intent studentIntent = new Intent(LoginActivity.this, StudentMainActivity.class);
                startActivity(studentIntent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                finish();
                break;

            default:
                roleMessageTextView.setVisibility(View.VISIBLE);
                roleMessageTextView.setText("We are still building your page, hold tight!");
                Log.d(TAG, "User role not implemented: " + role);
                break;
        }
    }

    private void handleError(VolleyError error) {
        showLoading(false);

        if (error instanceof TimeoutError) {
            showToast("The request timed out. Please try again.");
        } else if (error instanceof com.android.volley.NoConnectionError ||
                error.getCause() instanceof UnknownHostException) {
            showToast("No internet connection. Please check your network.");
        } else if (error.networkResponse != null) {
            handleServerError(error.networkResponse.statusCode);
        } else {
            showToast("Login failed. Please check your network connection.");
        }

        if (error.getMessage() != null) {
            Log.e(TAG, "Error message: " + error.getMessage());
        }
        Log.e(TAG, "Login error details: ", error);
    }

    private void handleServerError(int statusCode) {
        Log.d(TAG, "Server responded with error code: " + statusCode);
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
    }

    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}