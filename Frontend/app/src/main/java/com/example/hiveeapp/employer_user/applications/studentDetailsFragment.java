package com.example.hiveeapp.employer_user.applications;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.hiveeapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class studentDetailsFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_application_details, container, false);

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

        return view;
    }
}
