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
    private Context context;
    private List<JobPosting> jobPostings;
    private JobSwipeFragment jobSwipeFragment;

    public JobSwipeAdapter(Context context, JobSwipeFragment fragment) {
        this.context = context;
        this.jobSwipeFragment = fragment; // Reference to the fragment
        this.jobPostings = new ArrayList<>();

        // Use the fragment to load job postings
        initializeJobPostingsFromFragment();
    }

    @Override
    public int getCount() {
        return jobPostings.size();
    }

    @Override
    public Object getItem(int position) {
        return jobPostings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
        }

        JobPosting job = jobPostings.get(position);

        TextView jobTitle = convertView.findViewById(R.id.jobTitle);
        TextView jobDescription = convertView.findViewById(R.id.description);
        TextView jobSalary = convertView.findViewById(R.id.salary);

        jobTitle.setText(job.getTitle());
        jobDescription.setText(job.getDescription());
        jobSalary.setText("Salary: $" + job.getSalary());

        return convertView;
    }

    private void initializeJobPostingsFromFragment() {
        jobSwipeFragment.loadJobPostingsFromAdapter(new JobSwipeFragment.JobPostingsCallback() {
            @Override
            public void onJobPostingsLoaded(List<JobPosting> postings) {
                jobPostings = postings;
                notifyDataSetChanged();
                Log.d(TAG, "Job postings loaded and adapter updated.");
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading job postings: " + error);
                Toast.makeText(context, "Failed to load job postings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setJobPostings(List<JobPosting> newJobPostings) {
        this.jobPostings = new ArrayList<>(newJobPostings);
        notifyDataSetChanged();
    }

    public void removeJob(int position) {
        if (position < jobPostings.size()) {
            jobPostings.remove(position);
            notifyDataSetChanged();
        }
    }
}
