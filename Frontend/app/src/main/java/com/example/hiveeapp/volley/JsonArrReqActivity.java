package com.example.hiveeapp.volley;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonArrReqActivity extends AppCompatActivity {
    TextView textView;
    private Button mainSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Replace with actual layout

     //   textView = findViewById(R.id.textView);
        mainSignupBtn = findViewById(R.id.main_signup_btn);  // Initialize the signup button

        String url = "https://8c5d8b24-4a9a-4ce2-bf22-1aa5316f76a2.mock.pstmn.io/volley/post"; // Replace with your mock server URL

        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mock signup data
                JSONObject signupData = new JSONObject();
                try {
                    signupData.put("username", "testuser");
                    signupData.put("password", "password123");
                    signupData.put("email", "testuser@example.com");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, url, signupData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response from the mock server
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

                VolleySingleton.getInstance(JsonArrReqActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}




