package com.example.hiveeapp.registration.signup;

import android.content.Intent;
import android.text.InputType;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.example.hiveeapp.R;
import android.os.Bundle;
import android.util.Log;
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
import android.view.View;
import com.example.hiveeapp.registration.login.LoginActivity;

public class studentsignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText verifyPasswordEditText;
    private EditText emailEditText;
    private EditText universityEditText;
    private ImageButton togglePasswordVisibilityButton;
    private ImageButton toggleVerifyPasswordVisibilityButton;
    private boolean isPasswordVisible = false;
    private boolean isVerifyPasswordVisible = false;
    private final String URL = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        verifyPasswordEditText = findViewById(R.id.signup_verify_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        universityEditText = findViewById(R.id.signup_university_edt);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility_btn);
        toggleVerifyPasswordVisibilityButton = findViewById(R.id.toggle_verify_password_visibility_btn);

        // Toggle Password Visibility
        togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility(passwordEditText, togglePasswordVisibilityButton));
        toggleVerifyPasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility(verifyPasswordEditText, toggleVerifyPasswordVisibilityButton));

        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            if (nameEditText == null || passwordEditText == null || emailEditText == null || universityEditText == null || verifyPasswordEditText == null) {
                Toast.makeText(studentsignupActivity.this, "Error initializing fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String verifyPassword = verifyPasswordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String university = universityEditText.getText().toString();

            // Check for empty fields
            if (name.isEmpty() || password.isEmpty() || email.isEmpty() || university.isEmpty() || verifyPassword.isEmpty()) {
                Toast.makeText(studentsignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(verifyPassword)) {
                Toast.makeText(studentsignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
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

            // Create JSON request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    URL,
                    signupData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle successful signup
                            Toast.makeText(studentsignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                            // Navigate to LoginActivity
                            Log.d("Navigation", "Navigating to LoginActivity");
                            Intent intent = new Intent(studentsignupActivity.this, LoginActivity.class);
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

    private void togglePasswordVisibility(EditText editText, ImageButton button) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            button.setImageResource(R.drawable.ic_visibility_on);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            button.setImageResource(R.drawable.ic_visibility_off);
        }
        editText.setSelection(editText.getText().length());
    }
}
