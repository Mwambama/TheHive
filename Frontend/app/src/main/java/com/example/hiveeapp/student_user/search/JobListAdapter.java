package com.example.hiveeapp.student_user.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import java.util.ArrayList;
import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {

    private List<JobPosting> jobPostings = new ArrayList<>();

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_item, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPosting jobPosting = jobPostings.get(position);
        holder.jobTitle.setText(jobPosting.getTitle());
        holder.jobDescription.setText(jobPosting.getDescription());
        holder.jobSalary.setText(String.format("$%.2f", jobPosting.getSalary()));

        // Display additional job details if needed
        holder.jobStart.setText(jobPosting.getJobStart());
        holder.applicationStart.setText(jobPosting.getApplicationStart());
        holder.applicationEnd.setText(jobPosting.getApplicationEnd());
        holder.summary.setText(jobPosting.getSummary());
    }

    @Override
    public int getItemCount() {
        return jobPostings.size();
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
        notifyDataSetChanged(); // Notify the adapter about data changes
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobDescription, jobSalary, jobStart, applicationStart, applicationEnd, summary;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobDescription = itemView.findViewById(R.id.jobDescription);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobStart = itemView.findViewById(R.id.jobStart);
            applicationStart = itemView.findViewById(R.id.applicationStart);
            applicationEnd = itemView.findViewById(R.id.applicationEnd);
            summary = itemView.findViewById(R.id.summary);
        }
    }
}
