package com.example.hiveeapp.student_user.swipe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class JobSwipeViewModel extends ViewModel {
    private MutableLiveData<List<JobPosting>> jobPostings;

    public LiveData<List<JobPosting>> getJobPostings() {
        if (jobPostings == null) {
            jobPostings = new MutableLiveData<>();
        }
        return jobPostings;
    }

    public void setJobPostings(List<JobPosting> postings) {
        if (jobPostings == null) {
            jobPostings = new MutableLiveData<>();
        }
        jobPostings.setValue(postings);
    }
}
