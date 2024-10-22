package com.example.hiveeapp.student_user.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.setting.studentinfoApi;
import org.json.JSONArray;

/**
 * Fragment that fetches student information from the server.
 */
public class studentFragment extends Fragment {

    private static final String TAG = "studentFragment"; // Log tag for debugging

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment (if you have a layout file, replace 'fragment_student' with it)
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        // Fetch students from the backend
        fetchStudents();

        return view;
    }

    /**
     * Fetches the list of students from the server using studentinfoApi.
     */
    private void fetchStudents() {
        studentinfoApi.getStudents(getContext(),
                response -> handleResponse(response),
                error -> handleError(error)
        );
    }

    /**
     * Handles the successful response from the server.
     *
     * @param response The JSONArray response containing student information.
     */
    private void handleResponse(JSONArray response) {
        Log.d(TAG, "Students fetched successfully: " + response.toString());
        // Process the student data as needed; no display is required
    }

    /**
     * Handles errors from the API request.
     *
     * @param error The error returned from the server.
     */
    private void handleError(VolleyError error) {
        Log.e(TAG, "Error fetching students: " + error.getMessage());
        // Handle the error appropriately (e.g., log the error)
    }
}
