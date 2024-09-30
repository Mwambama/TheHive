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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.signup_username_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        emailEditText = findViewById(R.id.signup_email_edt);

        // Signup button click listener
        findViewById(R.id.signup_signup_btn).setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();

            // Check for empty fields
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
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
            String url = "https://<mock_server_id>.mock.pstmn.io/signup"; // Replace with your mock server URL

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
}
