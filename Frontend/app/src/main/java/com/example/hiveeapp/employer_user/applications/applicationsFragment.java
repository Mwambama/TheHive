package com.example.hiveeapp.employer_user.applications;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EmployerApis;
import com.example.hiveeapp.employer_user.display.JobsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment that displays a list of Jobs in a RecyclerView.
 * The list is fetched from the server using the EmployerApis.
 */
public class applicationsFragment extends Fragment {

    private RecyclerView applicationsRecyclerView;  // RecyclerView to display Jobs
    private applicationAdapter applicationsdapter;    // Adapter for the RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applications, container, false);

        // Initialize RecyclerView and set its layout manager
        applicationsRecyclerView = view.findViewById(R.id.applicationsRecyclerView);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter in read-only mode (no edit/delete buttons)
        applicationsdapter = new applicationAdapter(getContext(), false); // 'false' indicates read-only
        applicationsRecyclerView.setAdapter(applicationsdapter);

        // Fetch employers from the server
        getApplications();

        return view;
    }

    /**
     * Fetches the list of jobs from the server using EmployerApis.
     * Populates the RecyclerView with the data.
     * Handles errors and displays error messages in case of failure.
     */
    private void getApplications() {
        EmployerApis.getJobs(
                getContext(),
                response -> {
                    // Reverse the JSONArray to display the most recent employers first
                    JSONArray reversedEmployers = reverseJSONArray(response);
                    // Update the RecyclerView adapter with the reversed employers list
                    applicationsdapter.setJobs(reversedEmployers);
                },
                error -> {
                    // Handle error from the API request
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load applications. Please try again.", Toast.LENGTH_SHORT).show();
                }
        );
    }

    /**
     * Reverses the order of the given JSONArray.
     *
     * @param array The JSONArray to be reversed.
     * @return A new JSONArray in reversed order.
     */
    private JSONArray reverseJSONArray(JSONArray array) {
        JSONArray reversedArray = new JSONArray();
        for (int i = array.length() - 1; i >= 0; i--) {
            try {
                JSONObject jsonObject = array.getJSONObject(i);
                reversedArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return reversedArray;
    }
}
