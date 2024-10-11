package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonArrRequestActivity extends AppCompatActivity {
    private TextView textView;
    private Button mainSignupBtn;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText universityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_arr_request);  // Set the content view to the layout

        textView = findViewById(R.id.textView);  // Initialize the TextView
        mainSignupBtn = findViewById(R.id.signup_signup_btn);  // Initialize the signup button
        nameEditText = findViewById(R.id.signup_name_edt);  // Initialize the Name EditText
        passwordEditText = findViewById(R.id.signup_password_edt);  // Initialize the Password EditText
        emailEditText = findViewById(R.id.signup_email_edt);  // Initialize the Email EditText
        universityEditText = findViewById(R.id.signup_university_edt);  // Initialize the University EditText

        String url = "http://coms-3090-063.class.las.iastate.edu:8080/account/signup/student"; // Replace with your server URL


        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mock signup data
                JSONObject signupData = new JSONObject();
                try {
                    signupData.put("name", nameEditText.getText().toString());
                    signupData.put("password", passwordEditText.getText().toString());
                    signupData.put("email", emailEditText.getText().toString());
                    signupData.put("university", universityEditText.getText().toString());  // Include university field
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

                VolleySingleton.getInstance(JsonArrRequestActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
