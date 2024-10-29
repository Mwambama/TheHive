package com.example.hiveeapp.employer_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.employer_user.model.CreateJobsActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmployerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView employerNameTextView, jobTitleTextView, emailTextView, userTypeTextView;
    private Button logoutButton, viewInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Toolbar setup
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> onBackPressed());

        // Initialize TextView fields
        employerNameTextView = findViewById(R.id.employer_name);
        jobTitleTextView = findViewById(R.id.job_title);
        emailTextView = findViewById(R.id.email);
        userTypeTextView = findViewById(R.id.userTypeTextView);
        userTypeTextView.setText("This is the main page for Employer");

        // Set employer information
        employerNameTextView.setText("John Steve");
        jobTitleTextView.setText("Software Developer");
        emailTextView.setText("john.steve@example.com");

        // Initialize Log Out button
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logout());

        // Initialize View Info button
        viewInfoButton = findViewById(R.id.view_employer_info_button);
        viewInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerMainActivity.this, ViewEmployerInfoActivity.class);
            startActivity(intent);
        });

        // Temporarily removing job display functionality
        // displayPostedJobs();

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    // Temporarily remove job display functionality
    // private void displayPostedJobs() {
    //     List<PostedJobs> jobs = loadJobsFromAssets();
    //     if (jobs != null) {
    //         RecyclerView recyclerView = findViewById(R.id.posted_jobs_recycler_view);
    //         JobAdapter adapter = new JobAdapter(jobs);
    //         recyclerView.setAdapter(adapter);
    //         recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //     }
    // }

    // Temporarily remove loading jobs method
    // private List<PostedJobs> loadJobsFromAssets() {
    //     List<PostedJobs> jobs = new ArrayList<>();
    //     try {
    //         InputStream inputStream = getAssets().open("jobs.json");
    //         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    //         StringBuilder jsonBuilder = new StringBuilder();
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             jsonBuilder.append(line);
    //         }

    //         JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
    //         for (int i = 0; i < jsonArray.length(); i++) {
    //             JSONObject jobObject = jsonArray.getJSONObject(i);
    //             PostedJobs job = new PostedJobs(
    //                     jobObject.getString("title"),
    //                     jobObject.getString("location"),
    //                     jobObject.getString("date"),
    //                     jobObject.getString("description")
    //             );
    //             jobs.add(job);
    //         }
    //     } catch (IOException | JSONException e) {
    //         e.printStackTrace();
    //     }
    //     return jobs;
    // }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            // Handle home navigation
            return true;
        } else if (itemId == R.id.nav_tracking) {
            // Navigate to Tracking Page
            startActivity(new Intent(this, TrackingApplicationActivity.class));
            return true;
        } else if (itemId == R.id.nav_add_job) {
            // Navigate to Add Job Page
            startActivity(new Intent(this, EditJobActivity.class));
            return true;
        } else if (itemId == R.id.nav_chat) {
            // Navigate to Chat Page
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        }
        return false;
    }

}
