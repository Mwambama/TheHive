package com.example.hiveeapp.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyKeyActivity extends AppCompatActivity {

    private EditText keyField;
    private Button verifyKeyButton;
    private String email; // Holds the user's email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_key);

        keyField = findViewById(R.id.keyField);
        verifyKeyButton = findViewById(R.id.verifyKeyButton);

        // Retrieve the email from the previous activity
        email = getIntent().getStringExtra("email");

        verifyKeyButton.setOnClickListener(v -> {
            String key = keyField.getText().toString().trim();
            if (key.isEmpty()) {
                Toast.makeText(VerifyKeyActivity.this, "Please enter the key.", Toast.LENGTH_SHORT).show();
            } else {
                verifyKey(email, key);
            }
        });
    }

    private void verifyKey(String email, String key) {
        // Create JSON payload
        JSONObject payload = new JSONObject();
        try {
            payload.put("email", email);
            payload.put("verification_key", key);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create request.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Placeholder URL for verifying the key
        String url = "https://run.mocky.io/v3/a328228e-7831-49b4-8185-24ebbe845875";

        // Create a new JsonObjectRequest
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                payload,
                response -> {
                    // Handle successful response
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        if (success) {
                            Toast.makeText(this, "Key verified successfully.", Toast.LENGTH_SHORT).show();
                            // Navigate to ResetPasswordActivity
                            Intent intent = new Intent(VerifyKeyActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error verifying key: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}