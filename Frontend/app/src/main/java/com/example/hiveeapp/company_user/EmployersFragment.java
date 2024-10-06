package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerAdapter;
import com.example.hiveeapp.company_user.handleEmployers.EmployerApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmployersFragment extends Fragment {

    private RecyclerView employerRecyclerView;
    private EmployerAdapter employerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_employers, container, false);

        employerRecyclerView = view.findViewById(R.id.employerRecyclerView);
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter without edit/delete buttons
        employerAdapter = new EmployerAdapter(getContext(), false); // false indicates read-only
        employerRecyclerView.setAdapter(employerAdapter);

        getEmployers();

        return view;
    }

    private void getEmployers() {
        EmployerApi.getEmployers(
                getContext(),
                response -> {
                    employerAdapter.setEmployers(response);
                },
                error -> {
                    // Handle error
                }
        );
    }
}
