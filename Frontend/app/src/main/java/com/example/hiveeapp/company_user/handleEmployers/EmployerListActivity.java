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
import com.example.hiveeapp.company_user.CompanyMainActivity;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;

public class EmployerListActivity extends AppCompatActivity {

    private RecyclerView employerRecyclerView;
    private EmployerAdapter employerAdapter;
    private MaterialButton addEmployerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);

        // Initialize views
        initViews();

        // Set up Add Employer button
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerListActivity.this, EmployerCreationActivity.class);
            startActivity(intent);
        });

        // Set up RecyclerView
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        employerAdapter = new EmployerAdapter(this, true);
        employerRecyclerView.setAdapter(employerAdapter);

        // Load employers from the server
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
                startActivity(new Intent(EmployerListActivity.this, CompanyMainActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the employer list when returning to this activity
        loadEmployers();
    }

    /**
     * Initialize views in the activity
     */
    private void initViews() {
        employerRecyclerView = findViewById(R.id.employerRecyclerView);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load employers from the server using EmployerApi
     */
    private void loadEmployers() {
        EmployerApi.getEmployers(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        employerAdapter.setEmployers(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployerListActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}