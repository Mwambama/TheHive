










package com.example.hiveeapp.employer_user.applications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hiveeapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class studentDetailsFragment extends DialogFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_application_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the passed arguments
        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name", "N/A");
            String email = args.getString("email", "N/A");
            String phone = args.getString("phone", "N/A");
            String university = args.getString("university", "N/A");
            String graduationDate = args.getString("graduationDate", "N/A");
            String gpa = args.getString("gpa", "N/A");
            String resumePath = args.getString("resumePath", "N/A");

            // Populate the TextViews with data
            ((TextInputEditText) view.findViewById(R.id.nameView)).setText(name);
            ((TextInputEditText) view.findViewById(R.id.emailView)).setText(email);
            ((TextInputEditText) view.findViewById(R.id.phoneView)).setText(phone);
            ((TextInputEditText) view.findViewById(R.id.universityView)).setText(university);
            ((TextInputEditText) view.findViewById(R.id.graduationDateView)).setText(graduationDate);
            ((TextInputEditText) view.findViewById(R.id.gpaView)).setText(gpa);
            ((TextInputEditText) view.findViewById(R.id.resumePathView)).setText(resumePath);
        }
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














//package com.example.hiveeapp.employer_user.applications;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.DialogFragment;
//
//import com.example.hiveeapp.R;
//import com.google.android.material.textfield.TextInputEditText;
//
//import org.json.JSONObject;
//
//public class studentDetailsFragment extends DialogFragment {
//
//    private static final String ARG_APPLICATION = "application";
//
//    // Use this factory method to create a new instance of this fragment using the provided parameters.
//    public static studentDetailsFragment newInstance(JSONObject application) {
//        studentDetailsFragment fragment = new studentDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_APPLICATION, application.toString());
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.popup_application_details, container, false);
//
//        // Retrieve application data from arguments
//        if (getArguments() != null) {
//            String applicationString = getArguments().getString(ARG_APPLICATION);
//            try {
//                JSONObject application = new JSONObject(applicationString);
//                populateApplicationDetails(view, application);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return view;
//    }
//
//    private void populateApplicationDetails(View view, JSONObject application) {
//        // Populate the TextViews with data from the application JSON object
//        ((TextInputEditText) view.findViewById(R.id.nameView)).setText(application.optString("name", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.emailView)).setText(application.optString("email", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.phoneView)).setText(application.optString("phone", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.universityView)).setText(application.optString("university", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.graduationDateView)).setText(application.optString("graduationDate", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.gpaView)).setText(application.optString("gpa", "N/A"));
//        ((TextInputEditText) view.findViewById(R.id.resumePathView)).setText(application.optString("resumePath", "N/A"));
//    }
//}









//package com.example.hiveeapp.employer_user.applications;
//
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.DialogFragment;
//
//import com.example.hiveeapp.R;
//import com.google.android.material.textfield.TextInputEditText;
//
//public class studentDetailsFragment extends DialogFragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.popup_application_details, container, false);
//
//        // Retrieve the passed arguments
//        Bundle args = getArguments();
//        if (args != null) {
//            String name = args.getString("name", "N/A");
//            String email = args.getString("email", "N/A");
//            String phone = args.getString("phone", "N/A");
//            String university = args.getString("university", "N/A");
//            String graduationDate = args.getString("graduationDate", "N/A");
//            String gpa = args.getString("gpa", "N/A");
//            String resumePath = args.getString("resumePath", "N/A");
//
//            // Populate the TextViews with data
//            ((TextInputEditText) view.findViewById(R.id.nameView)).setText(name);
//            ((TextInputEditText) view.findViewById(R.id.emailView)).setText(email);
//            ((TextInputEditText) view.findViewById(R.id.phoneView)).setText(phone);
//            ((TextInputEditText) view.findViewById(R.id.universityView)).setText(university);
//            ((TextInputEditText) view.findViewById(R.id.graduationDateView)).setText(graduationDate);
//            ((TextInputEditText) view.findViewById(R.id.gpaView)).setText(gpa);
//            ((TextInputEditText) view.findViewById(R.id.resumePathView)).setText(resumePath);
//        }
//
//        return view;
//    }
//}
