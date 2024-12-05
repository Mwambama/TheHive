package com.example.hiveeapp.student_user.search;

import androidx.lifecycle.ViewModel;

import com.example.hiveeapp.student_user.swipe.JobPosting;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private List<JobPosting> jobPostings;

    public List<JobPosting> getJobPostings() {
        return jobPostings;
    }

    public void setJobPostings(List<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }
}
