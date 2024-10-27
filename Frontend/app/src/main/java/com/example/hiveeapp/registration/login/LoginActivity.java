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
import com.example.hiveeapp.registration.forgotPassword.ForgotPasswordActivity;
import com.example.hiveeapp.registration.signup.signupActivity;
import com.example.hiveeapp.registration.signup.studentsignupActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * LoginActivity handles the user login functionality, sending user credentials to the server for authentication.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

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

        // test purpose
        emailField.setText("teststudent@example.com");
        passwordField.setText("TestStudent1234@");
    }

    /**
     * Initialize all the views in the activity.
     */
    private void initializeViews() {
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        registerText = findViewById(R.id.registerText);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        roleMessageTextView = findViewById(R.id.roleMessageTextView);
    }

    /**
     * Set up click listeners for login, forgot password, and register actions.
     */
    private void setListeners() {
        // Login button click event
        loginButton.setOnClickListener(v -> authenticateUser());

        // Forgot password click event
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

    /**
     * Authenticate user by sending email and password to the server.
     */
    private void authenticateUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        // Validate email and password fields
        if (!validateInputs(email, password)) return;

        // Show the loading indicator
        showLoading(true);

        // Construct the login URL
        String loginUrl = "http://coms-3090-063.class.las.iastate.edu:8080/account/login";

        // Create the authentication header
        String credentials = email + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        // Send login request to the server
        JsonObjectRequest loginRequest = new JsonObjectRequest(
                Request.Method.POST,
                loginUrl,
                null,
                this::handleLoginSuccess,
                this::handleError
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

    /**
     * Validates the email and password fields.
     *
     * @param email    The entered email address.
     * @param password The entered password.
     * @return true if inputs are valid, false otherwise.
     */
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

    /**
     * Handle login success by navigating to the respective user activity based on their role.
     *
     * @param response The response received from the server.
     */
// Inside the handleLoginSuccess method, after extracting userId from response
    private void handleLoginSuccess(JSONObject response) {
        try {
            int userId = response.getInt("userId"); // Assuming userId is directly in the response as an integer
            String role = extractUserRole(response);

            // Save userId to SharedPreferences as an integer
            SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("userId", userId); // Save as an int
            editor.apply();

            navigateToUserActivity(role);
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Error parsing server response.");
        } finally {
            showLoading(false);
        }
    }


    /**
     * Extracts the user role from the server response.
     *
     * @param response The JSON response from the server.
     * @return The role of the user.
     * @throws JSONException If parsing fails.
     */
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

    /**
     * Navigate to the appropriate activity based on the user's role.
     *
     * @param role The role of the user (e.g., STUDENT, EMPLOYER, COMPANY, ADMIN).
     */
    private void navigateToUserActivity(String role) {
        switch (role) {
            case "COMPANY":
                // Proceed as usual for COMPANY users
                Intent companyIntent = new Intent(LoginActivity.this, CompanyMainActivity.class);
                startActivity(companyIntent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                finish();
                break;
            default:
                // Show the message on the screen for other user roles
                roleMessageTextView.setVisibility(View.VISIBLE);  // Display the message
                roleMessageTextView.setText("We are still building your page, hold tight!");
                Log.d(TAG, "User role not implemented: " + role);
                break;
        }
    }

    /**
     * Handle errors from the server response.
     *
     * @param error The error received from the server.
     */
    private void handleError(VolleyError error) {
        showLoading(false);

        if (error instanceof TimeoutError) {
            showToast("The request timed out. Please try again.");
        } else if (error.networkResponse != null) {
            handleServerError(error.networkResponse.statusCode);
        } else {
            showToast("Login failed. Please check your network connection.");
        }
    }

    /**
     * Handle server-side error codes and display appropriate messages.
     *
     * @param statusCode The HTTP status code received from the server.
     */
    private void handleServerError(int statusCode) {
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

    /**
     * Shows or hides the loading spinner and disables/enables the login button.
     *
     * @param show true to show loading, false to hide it.
     */
    private void showLoading(boolean show) {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
    }

    /**
     * Displays a Toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Snackbar message.
     *
     * @param message The message to display.
     */
    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}