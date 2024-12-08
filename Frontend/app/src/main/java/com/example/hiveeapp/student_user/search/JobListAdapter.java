package com.example.hiveeapp.student_user.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.search.JobPosting;

import java.util.ArrayList;
import java.util.List;

import android.widget.Button;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {

    private List<JobPosting> jobPostings = new ArrayList<>();
    private OnJobInteractionListener onJobInteractionListener;

    // Unified interface for handling clicks and applies
    public interface OnJobInteractionListener {
        void onJobClick(JobPosting jobPosting);

        void onJobApply(JobPosting jobPosting, int position);
    }

    public JobListAdapter(OnJobInteractionListener listener) {
        this.onJobInteractionListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_search_list_item, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPosting jobPosting = jobPostings.get(position);

        // Set job data
        holder.jobTitle.setText(jobPosting.getTitle());
        holder.companyName.setText(jobPosting.getCompanyName());
        holder.jobSalary.setText(String.format("$%.2f", jobPosting.getSalary()));
        holder.jobDescription.setText(jobPosting.getDescription());

        // Update the apply button based on the job's "applied" status
        if (jobPosting.isApplied()) {
            holder.applyButton.setEnabled(false);
            holder.applyButton.setText("Applied");
        } else {
            holder.applyButton.setEnabled(true);
            holder.applyButton.setText("Apply");
        }

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (onJobInteractionListener != null) {
                onJobInteractionListener.onJobClick(jobPosting);
            }
        });

        // Set apply button listener
        holder.applyButton.setOnClickListener(v -> {
            if (onJobInteractionListener != null) {
                onJobInteractionListener.onJobApply(jobPosting, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPostings.size();
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
        notifyDataSetChanged();
    }

    public void removeJob(int position) {
        if (position >= 0 && position < jobPostings.size()) {
            jobPostings.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateAppliedJobStatus(int position) {
        if (position >= 0 && position < jobPostings.size()) {
            jobPostings.get(position).setApplied(true); // Mark job as applied
            notifyItemChanged(position); // Update the UI for this item
        }
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, jobSalary, jobDescription;
        Button applyButton;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobDescription = itemView.findViewById(R.id.jobDescription);
            applyButton = itemView.findViewById(R.id.applyButton);
        }
    }
}
