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
    //private Button mainLoginBtn,
    private Button mainSignupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the views from the layout
       // imageView = findViewById(R.id.imageView);
        //mainLoginBtn = findViewById(R.id.main_login_btn);
        mainSignupBtn = findViewById(R.id.main_signup_btn);

        // Set OnClickListener for Signup Button
        // navigate to sign up
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
