package com.example.hiveeapp.employer_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;

public class employerInfoFragment extends Fragment {

    private TextView employerName;
    private TextView employerDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employer_info, container, false);

        employerName = view.findViewById(R.id.companyName);
        employerDescription = view.findViewById(R.id.companyDescription);

        // Set company information
        employerName.setText(" Employer");
        employerDescription.setText("Brief description of my self.");

        return view;
    }
}