package com.example.hiveeapp.signup;

import com.example.hiveeapp.R;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password_edt);
        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String email = emailEditText.getText().toString();

            // Check for empty fields
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
                // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignupActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if password is valid
        if (!isPasswordValid(password)) {
            Toast.makeText(SignupActivity.this, "Password must be at least 7 characters long, contain an uppercase letter, and a number", Toast.LENGTH_LONG).show();
            return;
        }

            // Create JSON object with signup data
            JSONObject signupData = new JSONObject();
            try {
                signupData.put("username", username);
                signupData.put("password", password);
                signupData.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // URL for the Postman mock server
            String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/signup/post"; // Replace with your mock server URL

            // Create JSON request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    signupData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle successful signup
                            Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle signup error
                            Toast.makeText(SignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            // Add request to the Volley request queue
            VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        });
    }
    private boolean isPasswordValid(String password) {
        if (password.length() < 7) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasDigit = password.matches(".*\\d.*");
        return hasUppercase && hasDigit;
    }
}


/**
 * package com.example.hiveeapp.registration;
 *
 * import android.content.Intent;
 * import android.os.Bundle;
 * import android.text.InputType;
 * import android.view.View;
 * import android.widget.*;
 * import androidx.appcompat.app.AppCompatActivity;
 *
 * import com.android.volley.Request;
 * import com.android.volley.toolbox.JsonObjectRequest;
 * import com.example.hiveeapp.R;
 * import com.example.hiveeapp.admin_user.AdminMainActivity;
 * import com.example.hiveeapp.company_user.handleEmployers.CompanyActivityApi;
 * import com.example.hiveeapp.employer_user.EmployerMainActivity;
 * import com.example.hiveeapp.volley.VolleySingleton;
 *
 * import org.json.JSONException;
 * import org.json.JSONObject;
 *
 * import student_user.StudentMainActivity;
 *
 * public class LoginActivity extends AppCompatActivity {
 *
 *     private EditText emailField, passwordField;
 *     private Button loginButton, forgotPasswordButton;
 *     private ImageView togglePasswordVisibility;
 *     private boolean isPasswordVisible = false;
 *
 *     @Override
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_login);
 *
 *         // Initialize views
 *         emailField = findViewById(R.id.emailField);
 *         passwordField = findViewById(R.id.passwordField);
 *         loginButton = findViewById(R.id.loginButton);
 *         forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
 *         togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);
 *
 *         loginButton.setOnClickListener(v -> authenticateUser());
 *
 *         togglePasswordVisibility.setOnClickListener(v -> togglePasswordVisibility());
 *
 *         forgotPasswordButton.setOnClickListener(v -> {
 *             // Handle forgot password logic here
 *             Toast.makeText(LoginActivity.this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
 *         });
 *     }
 *
 *     private void authenticateUser() {
 *         String email = emailField.getText().toString().trim();
 *         String password = passwordField.getText().toString();
 *
 *         if (email.isEmpty() || password.isEmpty()) {
 *             Toast.makeText(LoginActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
 *             return;
 *         }
 *
 *         // Create JSON payload
 *         JSONObject loginPayload = new JSONObject();
 *         try {
 *             loginPayload.put("email", email);
 *             loginPayload.put("password", password);
 *         } catch (JSONException e) {
 *             e.printStackTrace();
 *             return;
 *         }
 *
 *         // Send login request to server
 *         String loginUrl = "";
 *
 *         JsonObjectRequest loginRequest = new JsonObjectRequest(
 *                 Request.Method.POST,
 *                 loginUrl,
 *                 loginPayload,
 *                 response -> {
 *                     // Handle successful response
 *                     try {
 *                         boolean success = response.getBoolean("success");
 *                         if (success) {
 *                             int userType = response.getInt("user_type");
 *                             navigateToUserActivity(userType);
 *                         } else {
 *                             String message = response.getString("message");
 *                             Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
 *                         }
 *                     } catch (JSONException e) {
 *                         e.printStackTrace();
 *                         Toast.makeText(LoginActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
 *                     }
 *                 },
 *                 error -> {
 *                     // Handle error
 *                     Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
 *                 }
 *         );
 *
 *         // Add the request to the Volley request queue
 *         VolleySingleton.getInstance(this).addToRequestQueue(loginRequest);
 *     }
 *
 *     private void navigateToUserActivity(int userType) {
 *         Intent intent;
 *         switch (userType) {
 *             case 1:
 *                 // Navigate to Student Activity
 *                 intent = new Intent(LoginActivity.this, StudentMainActivity.class);
 *                 break;
 *             case 2:
 *                 // Navigate to Employer Activity
 *                 intent = new Intent(LoginActivity.this, EmployerMainActivity.class);
 *                 break;
 *             case 3:
 *                 // Navigate to Company Activity
 *                 intent = new Intent(LoginActivity.this, CompanyActivityApi.class);
 *                 break;
 *             case 4:
 *                 // Navigate to Admin Activity
 *                 intent = new Intent(LoginActivity.this, AdminMainActivity.class);
 *                 break;
 *             default:
 *                 Toast.makeText(LoginActivity.this, "User type not recognized.", Toast.LENGTH_SHORT).show();
 *                 return;
 *         }
 *         startActivity(intent);
 *         finish();
 *     }
 *
 *     private void togglePasswordVisibility() {
 *         if (isPasswordVisible) {
 *             // Hide password
 *             passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
 *             togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
 *             isPasswordVisible = false;
 *         } else {
 *             // Show password
 *             passwordField.setInputType(InputType.TYPE_CLASS_TEXT);
 *             togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_on);
 *             isPasswordVisible = true;
 *         }
 *         // Move cursor to the end of the text
 *         passwordField.setSelection(passwordField.getText().length());
 *     }
 * }
 */