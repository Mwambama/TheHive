package com.example.hiveeapp.student_user.profile;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.StudentApi;
import com.google.android.material.button.MaterialButton;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ResumeManagementActivity extends AppCompatActivity {

    private static final int PICK_PDF_REQUEST = 1;
    private Uri pdfUri;
    private ImageView pdfPreview;
    private MaterialButton uploadResumeButton;
    private int userId;

    private static final String TAG = "ResumeManagement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_management);

        // Get userId from intent
        userId = getIntent().getIntExtra("USER_ID", -1);

        // Initialize views
        pdfPreview = findViewById(R.id.pdfPreview);
        uploadResumeButton = findViewById(R.id.manageResumeButton);

        // Handle PDF upload
        uploadResumeButton.setOnClickListener(v -> selectPdf());

        // If the user has already uploaded a PDF, load the preview
        loadStoredPdfPreview();
    }

    private void selectPdf() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();

            // Take persistent permissions
            getContentResolver().takePersistableUriPermission(
                    pdfUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );

            uploadPdf();
        }
    }

    private void uploadPdf() {
        if (pdfUri == null) {
            Toast.makeText(this, "No PDF selected", Toast.LENGTH_SHORT).show();
            return;
        }

        StudentApi.uploadPdfToServer(this, userId, pdfUri,
                response -> {
                    Toast.makeText(this, "Resume uploaded successfully", Toast.LENGTH_SHORT).show();
                    showPdfThumbnail(pdfUri); // Display thumbnail
                },
                error -> Toast.makeText(this, "Failed to upload resume", Toast.LENGTH_SHORT).show()
        );
    }

    private void showPdfThumbnail(Uri uri) {
        try {
            ParcelFileDescriptor fileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);

                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                pdfPreview.setImageBitmap(bitmap);
                pdfPreview.setVisibility(View.VISIBLE);

                page.close();
                pdfRenderer.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + uri.toString(), e);
            Toast.makeText(this, "Error: File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e(TAG, "Error rendering PDF", e);
            Toast.makeText(this, "Error loading PDF preview", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStoredPdfPreview() {
        // Check if the PDF URI is stored locally (shared preferences or database)
        String storedUri = getSharedPreferences("app_prefs", MODE_PRIVATE).getString("pdf_uri", null);

        if (storedUri != null) {
            pdfUri = Uri.parse(storedUri);
            showPdfThumbnail(pdfUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Store the selected PDF URI for future use
        if (pdfUri != null) {
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit()
                    .putString("pdf_uri", pdfUri.toString())
                    .apply();
        }
    }
}
