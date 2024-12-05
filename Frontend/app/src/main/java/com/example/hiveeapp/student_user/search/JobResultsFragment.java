package com.example.hiveeapp.student_user.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.swipe.JobPosting;

import java.util.List;

public class JobResultsFragment extends Fragment implements JobListAdapter.OnJobClickListener {

    private RecyclerView jobResultsRecyclerView;
    private JobListAdapter jobListAdapter;
    private SharedViewModel sharedViewModel;

    public JobResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_job_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jobResultsRecyclerView = view.findViewById(R.id.job_results_recycler_view);
        jobListAdapter = new JobListAdapter(this);
        jobResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        jobResultsRecyclerView.setAdapter(jobListAdapter);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        List<JobPosting> jobPostings = sharedViewModel.getJobPostings();
        if (jobPostings != null && !jobPostings.isEmpty()) {
            jobListAdapter.setJobPostings(jobPostings);
        } else {
            // Handle empty or null list
        }
    }

    @Override
    public void onJobClick(JobPosting jobPosting) {
        // Handle job item click, e.g., navigate to job details
        // For now, just show a toast
        // Toast.makeText(getContext(), "Clicked on: " + jobPosting.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
