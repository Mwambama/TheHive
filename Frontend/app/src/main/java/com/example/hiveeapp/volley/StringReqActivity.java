package com.example.hiveeapp.volley;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hiveeapp.R;

import android.widget.TextView;


public class StringReqActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Replace with actual layout

        textView = findViewById(R.id.textView);

        String url = "http://10.0.2.2:5000/string"; // Replace with your server URL

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Error: " + error.toString());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}

