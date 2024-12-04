package com.example.hiveeapp.student_user.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.swipe.JobPosting;

import java.util.ArrayList;
import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {

    private List<JobPosting> jobPostings = new ArrayList<>();
    private OnJobClickListener onJobClickListener;

    public interface OnJobClickListener {
        void onJobClick(JobPosting jobPosting);
    }

    public JobListAdapter(OnJobClickListener listener) {
        this.onJobClickListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout file
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_list_item, parent, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobPosting jobPosting = jobPostings.get(position);

        // Ensure views are not null before setting text
        if (holder.jobTitle != null) {
            holder.jobTitle.setText(jobPosting.getTitle());
        }
        if (holder.companyName != null) {
            holder.companyName.setText(jobPosting.getCompanyName());
        }
        if (holder.jobSalary != null) {
            holder.jobSalary.setText(String.format("$%.2f", jobPosting.getSalary()));
        }
        if (holder.jobDescription != null) {
            holder.jobDescription.setText(jobPosting.getDescription());
        }

        holder.itemView.setOnClickListener(v -> {
            if (onJobClickListener != null) {
                onJobClickListener.onJobClick(jobPosting);
            } else {
                Toast.makeText(v.getContext(), "Clicked on: " + jobPosting.getTitle(), Toast.LENGTH_SHORT).show();
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

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, companyName, jobSalary, jobDescription;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure the IDs match those in job_search_list_item.xml
            jobTitle = itemView.findViewById(R.id.jobTitle);
            companyName = itemView.findViewById(R.id.companyName);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobDescription = itemView.findViewById(R.id.jobDescription);
        }
    }
}
