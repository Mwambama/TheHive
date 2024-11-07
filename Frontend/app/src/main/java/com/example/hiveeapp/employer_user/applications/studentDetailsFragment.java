

package com.example.hiveeapp.employer_user.applications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class studentDetailsFragment extends DialogFragment {

    private static final String ARG_STUDENT_DETAILS = "studentDetails";
    private JSONObject studentDetails;

    // Initialize TextViews to display student details
    private TextView studentNameTextView, studentEmailTextView, studentPhoneTextView,
            studentUniversityTextView, studentGraduationDateTextView, studentGpaTextView, studentResumePathTextView;

    // Factory method to create a new instance of the fragment with the student details
    public static studentDetailsFragment newInstance(JSONObject studentDetails) {
        studentDetailsFragment fragment = new studentDetailsFragment();
        Bundle args = new Bundle();

        // Check if studentDetails is not null before passing it
        if (studentDetails != null) {
            args.putString(ARG_STUDENT_DETAILS, studentDetails.toString());
        } else {
            // Pass an empty object if studentDetails is null
            args.putString(ARG_STUDENT_DETAILS, "{}");
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                // Retrieve student details from the arguments passed
                String studentDetailsString = getArguments().getString(ARG_STUDENT_DETAILS);

                // Initialize studentDetails only if the string is not empty
                if (studentDetailsString != null && !studentDetailsString.isEmpty()) {
                    studentDetails = new JSONObject(studentDetailsString);
                } else {
                    studentDetails = new JSONObject(); // Initialize empty if data is not found
                }
            } catch (JSONException e) {
                e.printStackTrace();
                studentDetails = new JSONObject(); // Initialize empty if JSON is invalid
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_application_details, container, false);

        // Initialize TextViews
        studentNameTextView = view.findViewById(R.id.nameView);
        studentEmailTextView = view.findViewById(R.id.emailView);
        studentPhoneTextView = view.findViewById(R.id.phoneView);
        studentUniversityTextView = view.findViewById(R.id.universityView);
        studentGraduationDateTextView = view.findViewById(R.id.graduationDateView);
        studentGpaTextView = view.findViewById(R.id.gpaView);
        studentResumePathTextView = view.findViewById(R.id.resumePathView);

        // If studentDetails is available, display the details
        if (studentDetails != null) {
            displayStudentDetails();
        }

        return view;
    }

    private void displayStudentDetails() {
        // Log the student data for debugging purposes
        studentNameTextView.setText(studentDetails.optString("name", "N/A"));
        studentEmailTextView.setText(studentDetails.optString("email", "N/A"));
        studentPhoneTextView.setText(studentDetails.optString("phone", "N/A"));
        studentUniversityTextView.setText(studentDetails.optString("university", "N/A"));
        studentGraduationDateTextView.setText(studentDetails.optString("graduationDate", "N/A"));
        studentGpaTextView.setText(studentDetails.optString("gpa", "N/A"));
        studentResumePathTextView.setText(studentDetails.optString("resumePath", "N/A"));
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











//
//
//package com.example.hiveeapp.employer_user.applications;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.hiveeapp.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class studentDetailsFragment extends DialogFragment {
//
//    private static final String ARG_STUDENT_DETAILS = "studentDetails";
//    private JSONObject studentDetails;
//
//    // Factory method to create a new instance of the fragment with the student details
//    public static studentDetailsFragment newInstance(JSONObject studentDetails) {
//        studentDetailsFragment fragment = new studentDetailsFragment();
//        Bundle args = new Bundle();
//
//        // Check if studentDetails is not null before passing it
//        if (studentDetails != null) {
//            args.putString(ARG_STUDENT_DETAILS, studentDetails.toString());
//        } else {
//            // Pass an empty object if studentDetails is null
//            args.putString(ARG_STUDENT_DETAILS, "{}");
//        }
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            try {
//                // Retrieve student details and handle any JSON issues
//                String studentDetailsString = getArguments().getString(ARG_STUDENT_DETAILS);
//
//                // Only catch JSONException when parsing the JSON string
//                if (studentDetailsString != null && !studentDetailsString.isEmpty()) {
//                    studentDetails = new JSONObject(studentDetailsString);
//                } else {
//                    studentDetails = new JSONObject(); // Initialize an empty JSON object
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                studentDetails = new JSONObject(); // In case of JSON error, initialize empty
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.popup_application_details, container, false);
//
//        // Initialize views
//        TextView studentNameTextView = view.findViewById(R.id.nameView);
//        TextView studentEmailTextView = view.findViewById(R.id.emailView);
//        TextView studentPhoneTextView = view.findViewById(R.id.phoneView);
//        TextView studentUniversityTextView = view.findViewById(R.id.universityView);
//        TextView studentGraduationDateTextView = view.findViewById(R.id.graduationDateView);
//        TextView studentGpaTextView = view.findViewById(R.id.gpaView);
//        TextView studentResumePathTextView = view.findViewById(R.id.resumePathView);
//
//        // Set student details if available
//        if (studentDetails != null) {
//            studentNameTextView.setText(studentDetails.optString("name", "N/A"));
//            studentEmailTextView.setText(studentDetails.optString("email", "N/A"));
//            studentPhoneTextView.setText(studentDetails.optString("phone", "N/A"));
//            studentUniversityTextView.setText(studentDetails.optString("university", "N/A"));
//            studentGraduationDateTextView.setText(studentDetails.optString("graduationDate", "N/A"));
//            studentGpaTextView.setText(studentDetails.optString("gpa", "N/A"));
//            studentResumePathTextView.setText(studentDetails.optString("resumePath", "N/A"));
//        }
//
//        return view;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            // Set the dialog to fill the screen
//            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        }
//    }
//}






//package com.example.hiveeapp.employer_user.applications;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//
//import com.example.hiveeapp.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class studentDetailsFragment extends DialogFragment {
//
//    private static final String ARG_STUDENT_DETAILS = "studentDetails";
//    private JSONObject studentDetails;
//
//    // Factory method to create a new instance of the fragment with the student details
//    public static studentDetailsFragment newInstance(JSONObject studentDetails) {
//        studentDetailsFragment fragment = new studentDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_STUDENT_DETAILS, studentDetails.toString());
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            try {
//                studentDetails = new JSONObject(getArguments().getString(ARG_STUDENT_DETAILS));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.popup_application_details, container, false);
//
//        // Initialize views
//        TextView studentNameTextView = view.findViewById(R.id.nameView);
//        TextView studentEmailTextView = view.findViewById(R.id.emailView);
//        TextView studentPhoneTextView = view.findViewById(R.id.phoneView);
//        TextView studentUniversityTextView = view.findViewById(R.id.universityView);
//        TextView studentGraduationDateTextView = view.findViewById(R.id.graduationDateView);
//        TextView studentGpaTextView = view.findViewById(R.id.gpaView);
//        TextView studentResumePathTextView = view.findViewById(R.id.resumePathView);
//
//        // Set student details if available
//        if (studentDetails != null) {
//            try {
//                studentNameTextView.setText(studentDetails.optString("name", "N/A"));
//                studentEmailTextView.setText(studentDetails.optString("email", "N/A"));
//                studentPhoneTextView.setText(studentDetails.optString("phone", "N/A"));
//                studentUniversityTextView.setText(studentDetails.optString("university", "N/A"));
//                studentGraduationDateTextView.setText(studentDetails.optString("graduationDate", "N/A"));
//                studentGpaTextView.setText(studentDetails.optString("gpa", "N/A"));
//                studentResumePathTextView.setText(studentDetails.optString("resumePath", "N/A"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return view;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            // Set the dialog to fill the screen
//            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        }
//    }
//}
