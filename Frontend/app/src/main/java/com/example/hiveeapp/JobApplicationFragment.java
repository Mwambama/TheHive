package com.example.hiveeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobApplicationFragment extends Fragment {

    private ListView appliedJobsListView;
    private List<String> appliedJobsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_applications, container, false);

        appliedJobsListView = view.findViewById(R.id.appliedJobsListView);
        loadAppliedJobs();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, appliedJobsList);
        appliedJobsListView.setAdapter(adapter);

        return view;
    }

    private void loadAppliedJobs() {
        SharedPreferences preferences = requireContext().getSharedPreferences("AppliedJobs", Context.MODE_PRIVATE);
        Set<String> appliedJobs = preferences.getStringSet("appliedJobs", new HashSet<>());
        appliedJobsList.addAll(appliedJobs);
    }
}
