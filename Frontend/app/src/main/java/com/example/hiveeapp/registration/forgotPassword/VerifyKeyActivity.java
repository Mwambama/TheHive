package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyKeyActivity extends AppCompatActivity {

    private EditText keyField;
    private Button verifyKeyButton;
    private ProgressBar loadingProgressBar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_key);

        keyField = findViewById(R.id.keyField);
        verifyKeyButton = findViewById(R.id.verifyKeyButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        verifyKeyButton.setOnClickListener(v -> {
            String key = keyField.getText().toString().trim();
            if (key.isEmpty()) {
                showToast("Please enter the key.");
            } else {
                verifyKey(email, key);
            }
        });
    }

    private void verifyKey(String email, String key) {
        // Show loading indicator
        loadingProgressBar.setVisibility(View.VISIBLE);
        verifyKeyButton.setEnabled(false);

        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("verification_key", key);
        } catch (JSONException e) {
            handleJSONException(e);
            return;
        }

        String url = "https://0426e89a-dc0e-4f75-8adb-c324dd58c2a8.mock.pstmn.io/verify-key";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    hideLoading();
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            showSnackbar("Key verified successfully.");
                            // Navigate to ResetPasswordActivity with animation
                            Intent intent = new Intent(VerifyKeyActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                            finish();
                        } else {
                            showToast(message);
                        }
                    } catch (JSONException e) {
                        handleJSONException(e);
                    }
                },
                this::handleVolleyError
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    // Function to handle JSON parsing errors
    private void handleJSONException(JSONException e) {
        e.printStackTrace();
        Toast.makeText(this, "Error parsing server response. Please try again.", Toast.LENGTH_SHORT).show();
    }

    // Function to handle different types of Volley errors
    private void handleVolleyError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
            Toast.makeText(this, "Invalid request. Please check your inputs.", Toast.LENGTH_SHORT).show();
        } else if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof com.android.volley.TimeoutError) {
            Toast.makeText(this, "Connection timeout. Please try again.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof com.android.volley.NoConnectionError) {
            Toast.makeText(this, "No internet connection. Please check your connection and try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An unexpected error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }
}