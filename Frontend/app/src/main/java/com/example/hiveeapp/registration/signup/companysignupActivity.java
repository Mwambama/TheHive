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

public class companysignupActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private ImageButton togglePasswordVisibilityButton;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup);

        // Initialize EditText fields
        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        togglePasswordVisibilityButton = findViewById(R.id.toggle_password_visibility_btn);

        // Set click listener for the toggle button
        togglePasswordVisibilityButton.setOnClickListener(view -> togglePasswordVisibility());

        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();

            if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(companysignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isPasswordValid(password)) {
                Toast.makeText(companysignupActivity.this, "Password must be at least 7 characters long, contain an uppercase letter, and a number", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject signupData = new JSONObject();
            try {
                signupData.put("name", name);
                signupData.put("password", password);
                signupData.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/company";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, signupData,
                    response -> {
                        Toast.makeText(companysignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(companysignupActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    },
                    error -> Toast.makeText(companysignupActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show());

            request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 7 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*");
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            togglePasswordVisibilityButton.setImageResource(R.drawable.ic_visibility_on);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
