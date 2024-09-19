package com.example.hiveeapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.hiveeapp.volley.VolleySingleton;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ImageView from the layout
        imageView = findViewById(R.id.imageView);

        // URL of the image you want to fetch
        String imageUrl = "https://media.istockphoto.com/id/1458782106/photo/scenic-aerial-view-of-the-mountain-landscape-with-a-forest-and-the-crystal-blue-river-in.jpg?s=1024x1024&w=is&k=20&c=iPdhO2H3jeYh4UWmNlrXNmiiK04iaLykIMiD9u1yDq4="; //test

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
    }
}
