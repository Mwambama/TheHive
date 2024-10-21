package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;

public class CompanyInfoFragment extends Fragment {

    private TextView companyName;
    private TextView companyDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_info, container, false);

        companyName = view.findViewById(R.id.companyName);
        companyDescription = view.findViewById(R.id.companyDescription);

        // Set company information
        companyName.setText("Your Company Name");
        companyDescription.setText("Brief description of your company.");

        return view;
    }
}