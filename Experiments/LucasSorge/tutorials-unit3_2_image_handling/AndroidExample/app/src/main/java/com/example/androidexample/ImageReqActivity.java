package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class ImageReqActivity extends AppCompatActivity {

    private Button btnImageReq;
    private ImageView imageView;
    private ProgressDialog progressDialog; // Add progress dialog

    public static final String URL_IMAGE = "http://10.0.2.2:8080/images/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_req);

        btnImageReq = findViewById(R.id.btnImageReq);
        imageView = findViewById(R.id.imgView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading image...");

        btnImageReq.setOnClickListener(v -> makeImageRequest());
    }

    /**
     * Making image request with progress dialog
     */
    private void makeImageRequest() {
        progressDialog.show(); // Show progress dialog when request starts

        ImageRequest imageRequest = new ImageRequest(
                URL_IMAGE,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        progressDialog.dismiss(); // Dismiss progress dialog
                        imageView.setImageBitmap(response);
                        Toast.makeText(getApplicationContext(), "Image Loaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss(); // Dismiss progress dialog
                        Toast.makeText(getApplicationContext(), "Error Loading Image", Toast.LENGTH_LONG).show();
                        Log.e("Volley Error", error.toString());
                    }
                }
        );

        // Add request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }
}