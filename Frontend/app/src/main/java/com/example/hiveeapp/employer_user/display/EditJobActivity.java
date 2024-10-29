package com.example.hiveeapp.employer_user.display;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.handleEmployers.EmployerAdapter;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.employer_user.model.CreateJobsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * EmployerListActivity handles the display and management of a list of employers.
 * It allows users to view, add, and interact with employers, and also includes a bottom navigation for navigation to other activities.
 */
public class EditJobActivity extends AppCompatActivity {

    private RecyclerView employerRecyclerView;   // RecyclerView for displaying the list of employers
    private EmployerAdapter employerAdapter;     // Adapter for managing employer data in the RecyclerView
    private MaterialButton addEmployerButton;    // Button for adding a new employer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        // Initialize views
        initViews();

        // Set up "Add Employer" button to navigate to  CreationJobsActivity
        addEmployerButton.setOnClickListener(v -> {
            Intent intent = new Intent(com.example.hiveeapp.employer_user.display.EditJobActivity.this, CreateJobsActivity.class);
            startActivity(intent);
        });

        // Set up the RecyclerView with a linear layout and the employer adapter
        employerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employerAdapter = new EmployerAdapter(this, true);  // true indicates editable mode
        employerRecyclerView.setAdapter(employerAdapter);

        // Load the list of employers from the server
        loadJobs();

        // Set up Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_invitations) {
                startActivity(new Intent(com.example.hiveeapp.employer_user.display.EditJobActivity.this, CreateJobsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_employers) {
                return true;
            } else if (itemId == R.id.navigation_main_user_page) {
                startActivity(new Intent(com.example.hiveeapp.employer_user.display.EditJobActivity.this, EmployerMainActivity.class));
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_employers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the employer list when the activity is resumed
        loadJobs();
    }

    /**
     * Initialize views in the activity.
     */
    private void initViews() {
        employerRecyclerView = findViewById(R.id.employerRecyclerView);
        addEmployerButton = findViewById(R.id.addEmployerButton);
    }

    /**
     * Load the list of jobs from the server using EmployerApis and update the adapter.
     * The employers are reversed before being set in the adapter.
     */
    private void loadJobs() {
        EmployerApis.getJobs(this,
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
                        Toast.makeText(com.example.hiveeapp.employer_user.display.EditJobActivity.this, "Error fetching employers: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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













//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.hiveeapp.R;
//
//public class EditJobActivity extends AppCompatActivity {
//
//    private EditText jobTitleEditText;
//    private EditText jobDescriptionEditText;
//    private EditText jobTypeEditText;
//    private EditText salaryRequirementsEditText;
//    private EditText ageRequirementEditText;
//    private EditText minimumGpaEditText;
//    private Button saveButton;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_job);
//
//        jobTitleEditText = findViewById(R.id.job_title_edit_text);
//        jobDescriptionEditText = findViewById(R.id.job_description_edit_text);
//        jobTypeEditText = findViewById(R.id.job_type_edit_text);
//        salaryRequirementsEditText = findViewById(R.id.salary_requirements_edit_text);
//        ageRequirementEditText = findViewById(R.id.age_requirement_edit_text);
//        minimumGpaEditText = findViewById(R.id.minimum_gpa_edit_text);
//        saveButton = findViewById(R.id.save_button);
//
//        // Get the job details from the intent
//        Intent intent = getIntent();
//        final PostedJobs jobDetails = (PostedJobs) intent.getSerializableExtra("jobDetails");
//
//        // Populate the fields with the existing job details
//        if (jobDetails != null) {
//            jobTitleEditText.setText(jobDetails.getJobTitle());
//            jobDescriptionEditText.setText(jobDetails.getJobDescription());
//            jobTypeEditText.setText(jobDetails.getJobType());
//            salaryRequirementsEditText.setText(jobDetails.getSalaryRequirements());
//            ageRequirementEditText.setText(jobDetails.getAgeRequirement());
//            minimumGpaEditText.setText(jobDetails.getMinimumGpa());
//        }
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Update the job details
//                jobDetails.setJobTitle(jobTitleEditText.getText().toString());
//                jobDetails.setJobDescription(jobDescriptionEditText.getText().toString());
//                jobDetails.setJobType(jobTypeEditText.getText().toString());
//                jobDetails.setSalaryRequirements(salaryRequirementsEditText.getText().toString());
//                jobDetails.setAgeRequirement(ageRequirementEditText.getText().toString());
//                jobDetails.setMinimumGpa(minimumGpaEditText.getText().toString());
//
//                // Send the updated job details back to the CreateJobsActivity
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("updatedJob", jobDetails);
//                setResult(RESULT_OK, resultIntent);
//                finish();
//            }
//        });
//    }
//}

