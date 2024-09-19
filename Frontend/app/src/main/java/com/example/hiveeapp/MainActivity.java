package com.example.hiveeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hiveeapp.company_user.companyActivity;
import com.example.hiveeapp.company_user.usersActivity;
import com.example.hiveeapp.volley.VolleySingleton;

public class MainActivity extends AppCompatActivity {

    private Button goToCompanyActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Your main layout

        // Find the button in the layout
        goToCompanyActivityButton = findViewById(R.id.goToCompanyActivityButton);

        // Set a click listener to navigate to CompanyActivity
        goToCompanyActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, companyActivity.class);
            startActivity(intent);
        });
    }
}
