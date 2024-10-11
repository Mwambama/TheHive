package com.example.hiveeapp.company_user;

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
import com.example.hiveeapp.company_user.handleEmployers.EmployerAdapter;
import com.example.hiveeapp.company_user.handleEmployers.EmployerApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment that displays a list of employers in a RecyclerView.
 * The list is fetched from the server using the EmployerApi.
 */
public class EmployersFragment extends Fragment {

    private RecyclerView employerRecyclerView;  // RecyclerView to display employers
    private EmployerAdapter employerAdapter;    // Adapter for the RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employers, container, false);

        // Initialize RecyclerView and set its layout manager
        employerRecyclerView = view.findViewById(R.id.employerRecyclerView);
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter in read-only mode (no edit/delete buttons)
        employerAdapter = new EmployerAdapter(getContext(), false); // 'false' indicates read-only
        employerRecyclerView.setAdapter(employerAdapter);

        // Fetch employers from the server
        getEmployers();

        return view;
    }

    /**
     * Fetches the list of employers from the server using EmployerApi.
     * Populates the RecyclerView with the data.
     * Handles errors and displays error messages in case of failure.
     */
    private void getEmployers() {
        EmployerApi.getEmployers(
                getContext(),
                response -> {
                    // Reverse the JSONArray to display the most recent employers first
                    JSONArray reversedEmployers = reverseJSONArray(response);
                    // Update the RecyclerView adapter with the reversed employers list
                    employerAdapter.setEmployers(reversedEmployers);
                },
                error -> {
                    // Handle error from the API request
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load employers. Please try again.", Toast.LENGTH_SHORT).show();
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