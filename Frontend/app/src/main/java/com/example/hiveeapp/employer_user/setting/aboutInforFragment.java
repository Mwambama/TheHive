package com.example.hiveeapp.employer_user.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hiveeapp.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;

public class aboutInforFragment extends Fragment {
    private TextView aboutUser;
   // private TextView employerDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_info, container, false);

        aboutUser = view.findViewById(R.id.aboutUser);
      //  employerDescription = view.findViewById(R.id.companyDescription);

        // Set company information
        aboutUser.setText("SETTING");
        //employerDescription.setText("Brief description of my self.");

        return view;
    }
}