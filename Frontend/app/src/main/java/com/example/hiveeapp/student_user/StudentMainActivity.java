package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.application.JobApplicationFragment;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.search.JobSearchActivity;
import com.example.hiveeapp.student_user.swipe.JobSwipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

/**
 * Main activity for student users, providing navigation between profile,
 * chat, job applications, and job swiping features.
 */
public class StudentMainActivity extends AppCompatActivity {

    private static final String TAG = "StudentMainActivity";
    private int userId;
    private String userEmail;
    private String userPassword;
    private BottomNavigationView bottomNavigationView;

    /**
     * Called when the activity is first created. Initializes the UI components
     * and sets up navigation features.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigationView();

        // Retrieve user details from SharedPreferences
        retrieveUserDetails();

        // Initialize TabLayout and set default fragment
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setupTabLayout(tabLayout);

        // Set default fragment to JobSwipeFragment (swiping jobs)
        replaceFragment(new JobSwipeFragment());
    }

    /**
     * Called when the activity resumes. Sets the default selected item
     * in the BottomNavigationView.
     */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);
    }

    /**
     * Sets up the BottomNavigationView and handles navigation item selections.
     */
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            } else if (itemId == R.id.navigation_chat) {
                startActivity(new Intent(StudentMainActivity.this, ChatListActivity.class));
                return true;
            } else if (itemId == R.id.navigation_apply) {
                replaceFragment(new JobSwipeFragment());
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(StudentMainActivity.this, JobSearchActivity.class));
                return true;
            }
            return false;
        });
    }

    /**
     * Retrieves user details from SharedPreferences and checks if they are valid.
     * If user details are missing, prompts the user to log in again.
     */
    private void retrieveUserDetails() {
        SharedPreferences preferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userId = preferences.getInt("userId", -1);
        userEmail = preferences.getString("email", "");
        userPassword = preferences.getString("password", "");

        if (userId == -1 || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "User credentials not found. Please log in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Sets up the TabLayout and handles tab selection events.
     *
     * @param tabLayout The TabLayout to set up.
     */
    private void setupTabLayout(TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
                Fragment selectedFragment;
                if (tab.getPosition() == 0) {
                    selectedFragment = new JobSwipeFragment();
                } else {
                    selectedFragment = new JobApplicationFragment();
                }
                replaceFragment(selectedFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Replaces the current fragment in the frame layout with the specified fragment.
     *
     * @param fragment The fragment to display.
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Navigates to the StudentProfileViewActivity.
     */
    private void navigateToProfile() {
        Intent intent = new Intent(StudentMainActivity.this, StudentProfileViewActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    /**
     * Gets the user ID of the currently logged-in user.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }
}
