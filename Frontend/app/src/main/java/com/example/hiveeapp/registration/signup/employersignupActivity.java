package com.example.hiveeapp.registration.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class employersignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText verifyPasswordEditText;
    private EditText emailEditText;
    private EditText companyIdEditText;
    private ImageButton togglePasswordVisibilityButton;
    private ImageButton toggleVerifyPasswordVisibilityButton;
    private boolean isPasswordVisible = false;
    private boolean isVerifyPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_signup);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        verifyPasswordEditText = findViewById(R.id.signup_verify_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        companyIdEditText = findViewById(R.id.signup_company_id_edt);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility_btn);
        toggleVerifyPasswordVisibilityButton = findViewById(R.id.toggle_verify_password_visibility_btn);

        // Toggle password visibility
        togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility(passwordEditText, togglePasswordVisibilityButton));
        toggleVerifyPasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility(verifyPasswordEditText, toggleVerifyPasswordVisibilityButton));

        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String verifyPassword = verifyPasswordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String companyId = companyIdEditText.getText().toString();

            // Check for empty fields
            if (name.isEmpty() || password.isEmpty() || verifyPassword.isEmpty() || email.isEmpty() || companyId.isEmpty()) {
                Toast.makeText(employersignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(verifyPassword)) {
                Toast.makeText(employersignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if password is valid
            if (!isPasswordValid(password)) {
                Toast.makeText(employersignupActivity.this, "Password must be at least 7 characters long, contain an uppercase letter, and a number", Toast.LENGTH_LONG).show();
                return;
            }

            // Create JSON object with signup data
            JSONObject signupData = new JSONObject();
            try {
                signupData.put("name", name);
                signupData.put("password", password);
                signupData.put("email", email);
                signupData.put("companyId", companyId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/employer";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, signupData,
                    response -> {
                        Toast.makeText(employersignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(employersignupActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    },
                    error -> Toast.makeText(employersignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        });
    }

    // Password validation method
    private boolean isPasswordValid(String password) {
        return password.length() >= 7 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

    // Toggle password visibility method
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
