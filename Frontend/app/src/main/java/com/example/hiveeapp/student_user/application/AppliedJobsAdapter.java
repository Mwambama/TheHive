package com.example.hiveeapp.student_user.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hiveeapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppliedJobsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_JOB = 1;

    private Context context;
    private List<Object> items;

    public AppliedJobsAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_HEADER;
        } else {
            return TYPE_JOB;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_job_application, parent, false);
            return new JobViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String header = (String) items.get(position);
            ((HeaderViewHolder) holder).headerText.setText(header);
        } else if (holder instanceof JobViewHolder) {
            JobApplication job = (JobApplication) items.get(position);
            ((JobViewHolder) holder).jobTitle.setText(job.getJobTitle());
            ((JobViewHolder) holder).jobStatus.setText("Status: " + job.getStatus());

            // Format and display the application date
            String formattedDate = formatAppliedDate(job.getAppliedOn());
            ((JobViewHolder) holder).appliedDate.setText("Applied On: " + formattedDate);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateApplications(List<Object> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    private String formatAppliedDate(String appliedOn) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(appliedOn);
            return date != null ? outputFormat.format(date) : "Unknown Date";
        } catch (ParseException e) {
            return "Unknown Date";
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobStatus, appliedDate;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.job_title);
            jobStatus = itemView.findViewById(R.id.job_status);
            appliedDate = itemView.findViewById(R.id.applied_date);
        }
    }
}
