package com.example.androidexample;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUploadActivity extends AppCompatActivity {

    Button selectBtn, uploadBtn;
    ImageView mImageView;
    Uri selectiedUri;
    ProgressDialog progressDialog; // Add progress dialog

    private static String UPLOAD_URL = "http://10.0.2.2:8080/images";
    private ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        mImageView = findViewById(R.id.imageSelView);
        selectBtn = findViewById(R.id.selectBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectiedUri = uri;
                mImageView.setImageURI(uri);
            }
        });

        selectBtn.setOnClickListener(v -> mGetContent.launch("image/*"));
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(v -> uploadImage());
    }

    private void uploadImage() {
        byte[] imageData = convertImageUriToBytes(selectiedUri);
        if (imageData == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show(); // Show progress dialog

        MultipartRequest multipartRequest = new MultipartRequest(
                Request.Method.POST, UPLOAD_URL, imageData,
                response -> {
                    progressDialog.dismiss(); // Dismiss progress dialog
                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
                    Log.d("Upload", "Response: " + response);

                    // Use Glide to load the uploaded image from the server response URL
                    Glide.with(this)
                            .load(response)  // Load the image from the response URL
                            .into(mImageView);

                    // Optionally reset the selected image URI after loading the image
                    selectiedUri = null;
                },
                error -> {
                    progressDialog.dismiss(); // Dismiss progress dialog
                    Toast.makeText(getApplicationContext(), "Error Uploading Image", Toast.LENGTH_LONG).show();
                    Log.e("Upload", "Error: " + error.getMessage());
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}