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
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;

import org.json.JSONArray;


public class JsonArrReqActivity extends AppCompatActivity {
    TextView textView;

    private Button mainSignupBtn;  // declaring the the signup button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Replace with actual layout

        textView = findViewById(R.id.textView);

        String url = "http://10.0.2.2:5000/jsonarray"; // Replace with your server URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        textView.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Error: " + error.toString());
            }
        });


    }

}

