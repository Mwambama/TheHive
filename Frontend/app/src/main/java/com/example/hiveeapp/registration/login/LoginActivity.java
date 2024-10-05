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
import com.google.android.material.snackbar.Snackbar;
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

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, R.string.enter_email_password, Toast.LENGTH_SHORT).show();
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
                            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();  // Snackbar for error feedback
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar.make(findViewById(android.R.id.content), R.string.error_parsing_response, Snackbar.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.login_failed) + error.getMessage(), Snackbar.LENGTH_LONG).show();
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
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

    // Add animation for back button press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right); // Animation when back button is pressed
    }
}