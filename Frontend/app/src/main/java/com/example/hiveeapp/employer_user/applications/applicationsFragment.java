package com.example.hiveeapp.employer_user.applications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Fragment that displays a list of Jobs in a RecyclerView.
 * The list is fetched from the server using EmployerApis.
 */
public class applicationsFragment extends Fragment {

    private static final String TAG = "applicationsFragment";
    private RecyclerView applicationsRecyclerView;  // RecyclerView to display Jobs
    private applicationAdapter applicationsAdapter;    // Adapter for the RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applied_jobs, container, false);

        // Initialize RecyclerView and set its layout manager
        applicationsRecyclerView = view.findViewById(R.id.applicationsRecyclerView);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter in read-only mode (no edit/delete buttons)
        applicationsAdapter = new applicationAdapter(getContext(), false); // 'false' indicates read-only
        applicationsRecyclerView.setAdapter(applicationsAdapter);

        // Fetch job postings from the server
        loadApplications();

        return view;
    }

    /**
     * Fetches the list of jobs from the server using applicationsApi.
     * Populates the RecyclerView with the data.
     * Handles errors and displays error messages in case of failure.
     */
    private void loadApplications() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        int employerId = preferences.getInt("userId", -1);

        if (employerId == -1) {
            Toast.makeText(getContext(), "Error: User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construct the API URL for fetching applications by employerId
        String apiUrl = "http://coms-3090-063.class.las.iastate.edu:8080/applications?employerId=" + employerId;
        Log.d(TAG, "GET Applications Request URL: " + apiUrl);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response -> {
                    // Reverse the JSONArray before setting it in the adapter
                    JSONArray reversedApplications = reverseJSONArray(response);
                    applicationsAdapter.setApplications(reversedApplications);
                },
                error -> {
                    String errorMessage = getErrorMessage(error);
                    Log.e(TAG, "Error fetching job postings: " + errorMessage);
                    Toast.makeText(getContext(), "Error fetching job postings: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return applicationsApi.getHeaders(requireContext());
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
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

    /**
     * Extracts a meaningful error message from a VolleyError.
     *
     * @param error The VolleyError object.
     * @return A string containing the error message.
     */
    private String getErrorMessage(VolleyError error) {
        String errorMsg = "An unexpected error occurred";
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorData = new String(error.networkResponse.data, "UTF-8");
                try {
                    JSONObject jsonError = new JSONObject(errorData);
                    errorMsg = jsonError.optString("message", errorMsg);
                } catch (JSONException jsonException) {
                    errorMsg = errorData;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                errorMsg = "Error parsing error message";
            }
        } else if (error.getMessage() != null) {
            errorMsg = error.getMessage();
        }
        return errorMsg;
    }
}