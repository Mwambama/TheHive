package com.example.hiveeapp.employer_user.applications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class studentDetailsFragment extends DialogFragment {

    private static final String TAG = "studentDetailsFragment";
    private static final String ARG_STUDENT_DETAILS = "studentDetails";
    private JSONObject studentDetails;

    // TextViews to display student details
    private TextView studentNameTextView, studentEmailTextView, studentPhoneTextView,
            studentUniversityTextView, studentGraduationDateTextView, studentGpaTextView, studentResumePathTextView;

    // Factory method to create a new instance of the fragment with the student details
    public static studentDetailsFragment newInstance(JSONObject studentDetails) {
        studentDetailsFragment fragment = new studentDetailsFragment();
        Bundle args = new Bundle();

        // Pass the JSON string if studentDetails is not null
        args.putString(ARG_STUDENT_DETAILS, studentDetails.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String studentDetailsString = getArguments().getString(ARG_STUDENT_DETAILS);

            try {
                // Check if studentDetailsString is null or empty before creating JSONObject
                if (studentDetailsString != null && !studentDetailsString.isEmpty()) {
                    studentDetails = new JSONObject(studentDetailsString);
                    Log.d(TAG, "Student Details JSON: " + studentDetails.toString());
                } else {
                    Log.d(TAG, "Student details string is null or empty, initializing empty JSONObject.");
                    studentDetails = new JSONObject(); // Initialize an empty JSON object
                }
            } catch (JSONException e) {
                Log.e(TAG, "Invalid JSON format for student details", e);
                studentDetails = new JSONObject(); // Fallback to an empty JSON object
            }
        } else {
            Log.d(TAG, "No arguments found, initializing empty JSONObject.");
            studentDetails = new JSONObject(); // Initialize an empty JSON object if no arguments
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_application_details, container, false);

        // Initialize TextViews by referencing the correct IDs
        studentNameTextView = view.findViewById(R.id.nameView);
        studentEmailTextView = view.findViewById(R.id.emailView);
        studentPhoneTextView = view.findViewById(R.id.phoneView);
        studentUniversityTextView = view.findViewById(R.id.universityView);
        studentGraduationDateTextView = view.findViewById(R.id.graduationDateView);
        studentGpaTextView = view.findViewById(R.id.gpaView);
        studentResumePathTextView = view.findViewById(R.id.resumePathView);

        // Retrieve student details and display them
        Bundle args = getArguments();
        if (args != null) {
            displayStudentDetails(args);
        } else {
            Log.e("studentDetailsFragment", "No arguments passed to fragment");
        }
        return view;
    }


    private void displayStudentDetails(Bundle studentDetails) {
        Log.d("studentDetailsFragment", "Displaying Student Details: " + studentDetails.toString());

        studentNameTextView.setText(studentDetails.getString("name", "N/A"));
        studentEmailTextView.setText(studentDetails.getString("email", "N/A"));
        studentPhoneTextView.setText(studentDetails.getString("phone", "N/A"));
        studentUniversityTextView.setText(studentDetails.getString("university", "N/A"));
        studentGpaTextView.setText(studentDetails.getString("gpa", "N/A"));
        studentResumePathTextView.setText(studentDetails.getString("resumePath", "N/A"));

        // Handle graduation date with date formatting
        String graduationDate = studentDetails.getString("graduationDate", "N/A");
        if (!"N/A".equals(graduationDate)) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(graduationDate);
                graduationDate = outputFormat.format(date);
            } catch (ParseException e) {
                Log.e("studentDetailsFragment", "Error parsing graduation date", e);
            }
        }
        studentGraduationDateTextView.setText(graduationDate);
    }



    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Set the dialog to fill the screen
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }
}
