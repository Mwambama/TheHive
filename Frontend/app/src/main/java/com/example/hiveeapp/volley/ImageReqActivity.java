package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;

import android.graphics.Bitmap;


public class ImageReqActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

        String url = "http://10.0.2.2:5000/image"; // Replace with server URL

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(imageRequest);
    }
}

