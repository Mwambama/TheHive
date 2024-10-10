package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.CompanyActivity;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * EmployerListActivity handles the display and management of a list of employers.
 * It allows users to view, add, and interact with employers, and also includes a bottom navigation for navigation to other activities.
 */
public class EmployerListActivity extends AppCompatActivity {

    private RecyclerView employerRecyclerView;   // RecyclerView for displaying the list of employers
    private EmployerAdapter employerAdapter;     // Adapter for managing employer data in the RecyclerView
    private MaterialButton addEmployerButton;    // Button for adding a new employer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);

        // Initialize views
        initViews();

        // Set up "Add Employer" button to navigate to EmployerCreationActivity
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerListActivity.this, EmployerCreationActivity.class);
            startActivity(intent);
        });

        // Set up the RecyclerView with a linear layout and the employer adapter
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employerAdapter = new EmployerAdapter(this, true);  // true indicates editable mode
        employerRecyclerView.setAdapter(employerAdapter);

        // Load the list of employers from the server
        loadEmployers();

        // Set up Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                startActivity(new Intent(EmployerListActivity.this, InvitationManagementActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                Toast.makeText(EmployerListActivity.this, "You are already on this page", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                startActivity(new Intent(EmployerListActivity.this, CompanyActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the employer list when the activity is resumed
        loadEmployers();
    }

    /**
     * Initialize views in the activity.
     */
    private void initViews() {
        employerRecyclerView = findViewById(R.id.employerRecyclerView);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load the list of employers from the server using EmployerApi and update the adapter.
     * The employers are reversed before being set in the adapter.
     */
    private void loadEmployers() {
        EmployerApi.getEmployers(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Reverse the JSONArray before setting it in the adapter
                        JSONArray reversedEmployers = reverseJSONArray(response);
                        employerAdapter.setEmployers(reversedEmployers);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error when fetching employers fails
                        Toast.makeText(EmployerListActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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