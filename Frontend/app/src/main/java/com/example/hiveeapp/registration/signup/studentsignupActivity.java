package com.example.hiveeapp.registration.signup;

import android.content.Intent;
import android.text.InputType;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
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
import android.view.LayoutInflater;
import android.view.View;
import com.example.hiveeapp.registration.login.LoginActivity;

public class studentsignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText universityEditText;
    private ImageButton togglePasswordVisibilityButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);  // Update to the correct XML layout

        // Initialize EditText fields
        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        universityEditText = findViewById(R.id.signup_university_edt);

        // Inflate the additional layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View passwordLayout = inflater.inflate(R.layout.activity_show_passwords, null);
        togglePasswordVisibilityButton = passwordLayout.findViewById(R.id.toggle_password_visibility_btn);

        // Set click listener for the toggle button
        togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility());

        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String university = universityEditText.getText().toString();

            // Check for empty fields
            if (name.isEmpty() || password.isEmpty() || email.isEmpty() || university.isEmpty()) {
                Toast.makeText(studentsignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if password is valid
            if (!isPasswordValid(password)) {
                Toast.makeText(studentsignupActivity.this, "Password must be at least 7 characters long, contain an uppercase letter, and a number", Toast.LENGTH_LONG).show();
                return;
            }

            // Create JSON object with signup data
            JSONObject signupData = new JSONObject();
            try {
                signupData.put("name", name);
                signupData.put("password", password);
                signupData.put("email", email);
                signupData.put("university", university);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // URL for the Postman mock server
            String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/student"; // Updated URL

            // Create JSON request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    signupData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle successful signup
                            Toast.makeText(studentsignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                            // Navigate to LoginActivity
                            Intent intent = new Intent(studentsignupActivity.this, LoginActivity.class);
                            // Ensure the back stack is cleared
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle signup error
                            Toast.makeText(studentsignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

              // Set a retry policy with a timeout of 10 seconds (10000 ms)
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000, // Timeout in milliseconds (10 seconds)
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        } else {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = true;
        }
        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
