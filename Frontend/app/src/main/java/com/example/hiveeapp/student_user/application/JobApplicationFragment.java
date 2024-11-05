package com.example.hiveeapp.student_user.application;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobApplicationFragment extends Fragment {
    private static final String GET_APPLICATIONS_URL = "http://coms-3090-063.class.las.iastate.edu:8080/applications/student?studentId=";
    private RecyclerView applicationRecyclerView;
    private JobApplicationAdapter adapter;
    private int studentId;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        studentId = ((StudentMainActivity) context).getUserId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_application, container, false);
        applicationRecyclerView = view.findViewById(R.id.applicationRecyclerView);
        applicationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new JobApplicationAdapter(new ArrayList<>());
        applicationRecyclerView.setAdapter(adapter);

        loadApplications();

        return view;
    }

    private void loadApplications() {
        String url = GET_APPLICATIONS_URL + studentId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                this::parseApplications,
                error -> Toast.makeText(getContext(), "Failed to load applications", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders(getContext());
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void parseApplications(JSONArray response) {
        List<Application> applications = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);
                applications.add(new Application(
                        obj.getInt("applicationId"),
                        obj.getInt("jobPostingId"),
                        obj.getString("jobTitle"),
                        obj.getString("status"),
                        obj.getString("appliedOn")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.updateApplications(applications);
    }

    /**
     * Generates headers for API requests with authorization.
     *
     * @return A map of headers including content type and authorization credentials.
     */
    public static Map<String, String> createAuthorizationHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Mocked username and password for testing purposes
        String username = "teststudent1@example.com";
        String password = "TestStudent1234@";

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }
}
