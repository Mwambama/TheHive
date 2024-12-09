package com.example.hiveeapp.employer_user.applications;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
//import com.example.hiveeapp.employer_user.model.CreateJobsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * EmployerListActivity handles the display and management of a list of employers.
 * It allows users to view, add, and interact with employers, and also includes a bottom navigation for navigation to other activities.
 */
public class ApplicationListActivity extends AppCompatActivity {

    private RecyclerView applicationRecyclerView;   // RecyclerView for displaying the list of employers
//    private EmployerAdapter employerAdapter;     // Adapter for managing employer data in the RecyclerView

    private applicationAdapter applicAdapter;     // Adapter for managing employer data in the RecyclerView

    private MaterialButton addEmployerButton;    // Button for adding a new employer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_application_list);

        // Initialize views
        initViews();

        // Set up the RecyclerView with a linear layout and the employer adapter
        applicationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicAdapter = new applicationAdapter(this, true);  // true indicates editable mode
        applicationRecyclerView.setAdapter(applicAdapter);

        // Load the list of Jobs from the server
        loadApplications();

        // Set up Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                //for now, this inviattion will be sent to home of user, then I will direct it later when I am working on employer application status
                startActivity(new Intent(com.example.hiveeapp.employer_user.applications.ApplicationListActivity.this, ApplicationListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                startActivity(new Intent(com.example.hiveeapp.employer_user.applications.ApplicationListActivity.this, EmployerMainActivity.class));
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_employers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the job list when the activity is resumed
        loadApplications();
    }

    /**
     * Initialize views in the activity.
     */
    private void initViews() {
        applicationRecyclerView = findViewById(R.id.applicationRecyclerView);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load the list of jobs from the server using EmployerApis and update the adapter.
     * The jobs are reversed before being set in the adapter.
     */
    private void loadApplications() {
        applicationsApi.getApplications(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Reverse the JSONArray before setting it in the adapter
                        JSONArray reversedEmployers = reverseJSONArray(response);
                        applicAdapter.setApplications(reversedEmployers);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error when fetching employers fails
                        Toast.makeText(com.example.hiveeapp.employer_user.applications.ApplicationListActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
