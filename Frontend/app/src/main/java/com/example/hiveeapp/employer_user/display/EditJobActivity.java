package com.example.hiveeapp.employer_user.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;

public class EditJobActivity extends AppCompatActivity {

    private EditText jobTitleEditText;
    private EditText jobDescriptionEditText;
    private EditText jobTypeEditText;
    private EditText salaryRequirementsEditText;
    private EditText ageRequirementEditText;
    private EditText minimumGpaEditText;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        jobTitleEditText = findViewById(R.id.job_title_edit_text);
        jobDescriptionEditText = findViewById(R.id.job_description_edit_text);
        jobTypeEditText = findViewById(R.id.job_type_edit_text);
        salaryRequirementsEditText = findViewById(R.id.salary_requirements_edit_text);
        ageRequirementEditText = findViewById(R.id.age_requirement_edit_text);
        minimumGpaEditText = findViewById(R.id.minimum_gpa_edit_text);
        saveButton = findViewById(R.id.save_button);

        // Get the job details from the intent
        Intent intent = getIntent();
        final PostedJobs jobDetails = (PostedJobs) intent.getSerializableExtra("jobDetails");

        // Populate the fields with the existing job details
        if (jobDetails != null) {
            jobTitleEditText.setText(jobDetails.getJobTitle());
            jobDescriptionEditText.setText(jobDetails.getJobDescription());
            jobTypeEditText.setText(jobDetails.getJobType());
            salaryRequirementsEditText.setText(jobDetails.getSalaryRequirements());
            ageRequirementEditText.setText(jobDetails.getAgeRequirement());
            minimumGpaEditText.setText(jobDetails.getMinimumGpa());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the job details
                jobDetails.setJobTitle(jobTitleEditText.getText().toString());
                jobDetails.setJobDescription(jobDescriptionEditText.getText().toString());
                jobDetails.setJobType(jobTypeEditText.getText().toString());
                jobDetails.setSalaryRequirements(salaryRequirementsEditText.getText().toString());
                jobDetails.setAgeRequirement(ageRequirementEditText.getText().toString());
                jobDetails.setMinimumGpa(minimumGpaEditText.getText().toString());

                // Send the updated job details back to the CreateJobsActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedJob", jobDetails);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

