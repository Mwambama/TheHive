package com.example.hiveeapp.registration.forgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
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
            e.printStackTrace();
            showSnackbar("Failed to create request.");
            hideLoading();
            return;
        }

        String url = "";  // Define the server URL

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
                        e.printStackTrace();
                        showSnackbar("Error parsing server response.");
                    }
                },
                error -> {
                    hideLoading();
                    showToast("Error verifying key: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void hideLoading() {
        loadingProgressBar.setVisibility(View.GONE);
        verifyKeyButton.setEnabled(true);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);  // Apply back navigation animation
    }
}