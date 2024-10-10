package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonArrRequestActivity2 extends AppCompatActivity {
    TextView textView;
    private Button mainSignupBtn;
    private EditText nameEditText, passwordEditText, companyIdEditText, emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_arr_request2);  // Set the content view to the layout

        textView = findViewById(R.id.textView);  // Initialize the TextView
        mainSignupBtn = findViewById(R.id.main_signup_btn);  // Initialize the signup button

        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        companyIdEditText = findViewById(R.id.signup_company_id_edt);
        emailEditText = findViewById(R.id.signup_email_edt);

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/employer"; // Replace with your server URL

        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String companyId = companyIdEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Check for empty fields
                if (name.isEmpty() || password.isEmpty() || companyId.isEmpty() || email.isEmpty()) {
                    Toast.makeText(JsonArrRequestActivity2.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mock signup data
                JSONObject signupData = new JSONObject();
                try {
                    signupData.put("name", name);
                    signupData.put("password", password);
                    signupData.put("companyId", companyId);
                    signupData.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, url, signupData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response from the server
                                StringBuilder responseData = new StringBuilder();
                                try {
                                    String status = response.getString("status");
                                    String message = response.getString("message");
                                    responseData.append("Status: ").append(status).append("\n");
                                    responseData.append("Message: ").append(message).append("\n\n");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                textView.setText(responseData.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                textView.setText("Error: " + error.toString());
                            }
                        }
                );

                VolleySingleton.getInstance(JsonArrRequestActivity2.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}

