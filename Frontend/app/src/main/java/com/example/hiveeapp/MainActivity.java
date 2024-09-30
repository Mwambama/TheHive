package com.example.hiveeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hiveeapp.signup.SignupActivity;
import com.example.hiveeapp.volley.VolleySingleton;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button mainLoginBtn, mainSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views from the layout
        imageView = findViewById(R.id.imageView);
        mainLoginBtn = findViewById(R.id.main_login_btn);
        mainSignupBtn = findViewById(R.id.main_signup_btn);

        // URL of the image you want to fetch
        String imageUrl = "";

        // Create an ImageRequest using Volley
        ImageRequest imageRequest = new ImageRequest(
                imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Set the downloaded image to the ImageView
                        imageView.setImageBitmap(response);
                    }
                },
                0, // max width (0 for full-size)
                0, // max height (0 for full-size)
                ImageView.ScaleType.CENTER_CROP, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error case
                        error.printStackTrace();
                    }
                }
        );
        // Add the request to the Volley queue
        VolleySingleton.getInstance(this).addToRequestQueue(imageRequest);

        // Set OnClickListener for Signup Button
        mainSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SignupActivity
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
