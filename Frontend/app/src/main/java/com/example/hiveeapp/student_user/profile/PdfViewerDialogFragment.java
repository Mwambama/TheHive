package com.example.hiveeapp.student_user.profile;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.hiveeapp.R;
import java.io.IOException;

public class PdfViewerDialogFragment extends DialogFragment {

    private static final String ARG_PDF_URI = "pdfUri";
    private Uri pdfUri;

    public static PdfViewerDialogFragment newInstance(Uri pdfUri) {
        PdfViewerDialogFragment fragment = new PdfViewerDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PDF_URI, pdfUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pdfUri = getArguments().getParcelable(ARG_PDF_URI);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pdf_viewer, container, false);
        ImageView pdfImageView = view.findViewById(R.id.fullScreenPdfImage);

        // Load and show the PDF
        showPdf(pdfImageView, pdfUri);

        return view;
    }

    private void showPdf(ImageView pdfImageView, Uri uri) {
        try {
            ParcelFileDescriptor fileDescriptor = requireContext().getContentResolver().openFileDescriptor(uri, "r");
            if (fileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);

                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                pdfImageView.setImageBitmap(bitmap);

                page.close();
                pdfRenderer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
