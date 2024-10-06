package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployerListActivity extends AppCompatActivity {

    private RecyclerView employerRecyclerView;
    private EmployerAdapter employerAdapter;
    private ImageButton backArrowIcon;
    private Button addEmployerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);

        // Initialize views
        initViews();

        // Set up back navigation
        backArrowIcon.setOnClickListener(v -> finish());

        // Set up Add Employer button
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployerListActivity.this, EmployerCreationActivity.class);
            startActivity(intent);
        });

        // Set up RecyclerView
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter with read-only mode (isEditable = false)
        employerAdapter = new EmployerAdapter(this, false); // false indicates read-only mode
        employerRecyclerView.setAdapter(employerAdapter);

        // Load employers from the server
        loadEmployers();
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
        backArrowIcon = findViewById(R.id.backArrowIcon);
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