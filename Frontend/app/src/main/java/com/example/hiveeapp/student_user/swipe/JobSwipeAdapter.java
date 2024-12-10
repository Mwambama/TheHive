package com.example.hiveeapp.student_user.swipe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hiveeapp.R;

import java.util.ArrayList;
import java.util.List;

public class JobSwipeAdapter extends BaseAdapter {
    private static final String TAG = "JobSwipeAdapter";
    private final Context context;
    private List<JobPosting> jobPostings;

    public JobSwipeAdapter(Context context, List<JobPosting> initialJobPostings) {
        this.context = context;
        this.jobPostings = initialJobPostings != null ? new ArrayList<>(initialJobPostings) : new ArrayList<>();
        Log.d(TAG, "Adapter initialized with " + this.jobPostings.size() + " job postings.");
    }

    @Override
    public int getCount() {
        return jobPostings.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < jobPostings.size()) {
            return jobPostings.get(position);
        } else {
            Log.e(TAG, "Attempted to get item at invalid position: " + position);
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
            holder = new ViewHolder();
            holder.jobTitle = convertView.findViewById(R.id.jobTitle);
            holder.jobDescription = convertView.findViewById(R.id.description);
            holder.jobSalary = convertView.findViewById(R.id.salary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position >= 0 && position < jobPostings.size()) {
            JobPosting job = jobPostings.get(position);
            holder.jobTitle.setText(job.getTitle());
            holder.jobDescription.setText(job.getDescription());
            holder.jobSalary.setText("Salary: $" + job.getSalary());
        } else {
            Log.e(TAG, "Invalid position in getView: " + position);
        }

        return convertView;
    }

    public void setJobPostings(List<JobPosting> newJobPostings) {
        if (newJobPostings == null || newJobPostings.isEmpty()) {
            Log.w(TAG, "Received empty or null job postings. Clearing existing data.");
            this.jobPostings.clear();
        } else {
            Log.d(TAG, "Updating job postings with " + newJobPostings.size() + " items.");
            this.jobPostings = new ArrayList<>(newJobPostings);
        }
        notifyDataSetChanged();
    }

    public void addJobPostings(List<JobPosting> additionalJobPostings) {
        if (additionalJobPostings != null && !additionalJobPostings.isEmpty()) {
            Log.d(TAG, "Adding " + additionalJobPostings.size() + " more job postings.");
            this.jobPostings.addAll(additionalJobPostings);
            notifyDataSetChanged();
        } else {
            Log.w(TAG, "Attempted to add empty or null job postings.");
        }
    }

    public void removeJob(int position) {
        if (position >= 0 && position < jobPostings.size()) {
            Log.d(TAG, "Removing job at position: " + position);
            jobPostings.remove(position);
            notifyDataSetChanged();
        } else {
            Log.e(TAG, "Invalid position to remove: " + position);
        }
    }

    private static class ViewHolder {
        TextView jobTitle;
        TextView jobDescription;
        TextView jobSalary;
    }
}
