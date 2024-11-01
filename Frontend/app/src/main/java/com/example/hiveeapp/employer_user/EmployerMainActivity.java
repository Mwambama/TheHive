package com.example.hiveeapp.employer_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
import com.example.hiveeapp.employer_user.model.ChatActivity;
import com.example.hiveeapp.employer_user.model.TrackingApplicationActivity;
import com.example.hiveeapp.employer_user.setting.EmployerProfileActivity;
import com.example.hiveeapp.employer_user.setting.ViewEmployerInfoActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class EmployerMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Button logoutButton, viewInfoButton;
    private TabLayout tabLayout;
    private int userId;
    private static final String TAG = "EmployerMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employer_profile);

        // Toolbar setup
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(view -> onBackPressed());


        // Retrieve userId from SharedPreferences
//        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
//        userId = preferences.getInt("userId", -1);
//
//        //  I will remove these two for now so that I do not get this message when clicking goToemployerActivty button and these messages in there
//        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);
//
//        if (userId == -1) {
//            Toast.makeText(this, "User ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "User ID is invalid. Redirecting to login screen.");
//        }

        // since this does not work, try yet since it not recogning who the user is and since we are not coming the
        // from the user LOgin page, so there is no way of knowing who the user is just yet.
        // I can use this in the future in case I get lost when trying to find user

        // Retrieve companyId from SharedPreferences
//        SharedPreferences preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);
//        // Retrieve companyId directly from the job JSONObject
//        long employerId = job.optLong("employerId", -1);
//        Log.d("EmployerApis", "Retrieved employerId: " + employerId);
//
//        if (employerId == -1) {
//            Toast.makeText(context, "Error: employerId not found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Ensure jobPostingId is provided
//        long jobPostingId = job.optLong("jobPostingId", -1);
//        if (jobPostingId == -1) {
//            Toast.makeText(context, "Error: Job PostingId not found", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // this feature on the top will be used for recogninzing and knowing who the user is and redirecting them to the their profile and load their profile with their sinformation



        // Initialize views
        tabLayout = findViewById(R.id.tabLayouts);

        // Add tabs programmatically for "Employer Info"
        tabLayout.addTab(tabLayout.newTab().setText("Main Page"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));

        // Set up the tab layout listener for fragment switching
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new employerInfoFragment();
                        break;
                    default:
                        selectedFragment = new employerInfoFragment();    // change this to the employerInfoFragment
                }
                loadFragment(selectedFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Load default fragment (Employer Info) when the activity is opened
        loadFragment(new employerInfoFragment());

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> logout());

        viewInfoButton = findViewById(R.id.view_employer_info_button);
        viewInfoButton.setOnClickListener(view -> {
            Intent intent = new Intent(EmployerMainActivity.this, ViewEmployerInfoActivity.class);
            startActivity(intent);
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_main_user_page) {

            // this works for the long profile with address that will be fixed lated fully
            navigateToProfile();
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

    private void navigateToProfile() {
        Intent intent = new Intent(EmployerMainActivity.this, EmployerProfileActivity.class);
        intent.putExtra("USER_ID", userId);
        Log.d(TAG, "Navigating to StudentProfileViewActivity with userId: " + userId);
        startActivity(intent);
    }


    // this works for the long profile with address that will be fixed lated fully | for the secondemployerApi that is uncommented
//    private void navigateToProfile() {
//        Intent intent = new Intent(EmployerMainActivity.this, EmployerProfileActivity.class);
//        intent.putExtra("USER_ID", userId);
//        Log.d(TAG, "Navigating to StudentProfileViewActivity with userId: " + userId);
//        startActivity(intent);
//    }

    // Log out of the page
    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // Helper method to load fragments into the frame layout
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
}
