
package com.example.hiveeapp.student_user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.application.AppliedJobsFragment;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;

import com.example.hiveeapp.student_user.search.JobSearchActivity;

import com.example.hiveeapp.student_user.swipe.JobSwipeFragment;
import com.example.hiveeapp.volley.VolleySingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Main activity for student users, providing navigation between profile,
 * chat, job applications, and job swiping features.
 */
public class StudentMainActivity extends AppCompatActivity {

    private static final String TAG = "StudentMainActivity";
    private static final String GET_STUDENT_INFO_URL = "http://coms-3090-063.class.las.iastate.edu:8080/student";
    private static final String PREFERENCES_NAME = "StudentPreferences";
    public static final String DAILY_APPLIED_COUNT_KEY = "dailyAppliedCount";
    private static final String LAST_UPDATE_DATE_KEY = "lastUpdateDate";

    private int userId;
    private String userEmail;
    private String userPassword;
    private BottomNavigationView bottomNavigationView;
    private TextView dailyAppliedCount;

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

        dailyAppliedCount = findViewById(R.id.appliedBoxCount);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupBottomNavigationView();
        retrieveUserDetails();
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        setupTabLayout(tabLayout);


        replaceFragment(new JobSwipeFragment());
        resetDailyAppliedCountIfNewDay();
        displaySavedDailyAppliedCount();
        fetchDailyAppliedCount(userId);
    }

    /**
     * Called when the activity resumes. Sets the default selected item
     * in the BottomNavigationView.
     */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.navigation_apply);
        // Refresh daily applied count on resume
        fetchDailyAppliedCount(userId);
    }

    public void incrementAndDisplayDailyAppliedCount() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int currentCount = preferences.getInt(DAILY_APPLIED_COUNT_KEY, 0);
        currentCount++; // Increment the count
        preferences.edit().putInt(DAILY_APPLIED_COUNT_KEY, currentCount).apply(); // Save the new count

        // Update the TextView dynamically
        if (dailyAppliedCount != null) { // Ensure TextView is initialized
            dailyAppliedCount.setText(String.valueOf(currentCount));
        }
        Log.d(TAG, "Daily Applied Count incremented and refreshed: " + currentCount);
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
                    selectedFragment = new AppliedJobsFragment();
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
    private void resetDailyAppliedCountIfNewDay() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String lastUpdateDate = preferences.getString(LAST_UPDATE_DATE_KEY, "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!currentDate.equals(lastUpdateDate)) {
            preferences.edit()
                    .putString(LAST_UPDATE_DATE_KEY, currentDate)
                    .putInt(DAILY_APPLIED_COUNT_KEY, 0)
                    .apply();
            Log.d(TAG, "Daily applied count reset for a new day.");
        }
    }


    /**
     * Fetches the daily applied count for the user from the backend and updates the UI.
     *
     * @param studentId The ID of the currently logged-in student.
     */
    /**
     * Fetches the daily applied count for the user from the backend and updates the UI.
     *
     * @param studentId The ID of the currently logged-in student.
     */
    private void fetchDailyAppliedCount(int studentId) {
        String url = GET_STUDENT_INFO_URL + "/" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Safely parse the daily applied count with a default value of 0
                        int applicationsMadeToday = response.optInt("applicationsMadeToday", 0);

                        // Log the fetched value
                        Log.d(TAG, "Fetched applicationsMadeToday: " + applicationsMadeToday);

                        // Update the UI and save the count locally
                        updateDailyAppliedCount(applicationsMadeToday);
                        saveDailyAppliedCount(applicationsMadeToday);

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing daily applied count", e);
                    }
                },
                error -> {
                    Log.e(TAG, "Failed to fetch daily applied count", error);
                    Toast.makeText(this, "Failed to fetch Daily Applied count.", Toast.LENGTH_SHORT).show();

                    // If the fetch fails, fallback to displaying the saved count
                    displaySavedDailyAppliedCount();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return createAuthorizationHeaders();
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    /**
     * Updates the Daily Applied Count TextView with the fetched count.
     *
     * @param count The number of applications made today by the user.
     */
    private void updateDailyAppliedCount(int count) {
        dailyAppliedCount.setText(String.valueOf(count));
    }

    /**
     * Saves the daily applied count in SharedPreferences.
     *
     * @param count The count to save.
     */
    private void saveDailyAppliedCount(int count) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        preferences.edit().putInt(DAILY_APPLIED_COUNT_KEY, count).apply();
        Log.d(TAG, "Updated Daily Applied Count TextView: " + count);
    }

    /**
     * Displays the locally saved daily applied count.
     */
    private void displaySavedDailyAppliedCount() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int savedCount = preferences.getInt(DAILY_APPLIED_COUNT_KEY, 0);
        dailyAppliedCount.setText(String.valueOf(savedCount));
        Log.d(TAG, "Displayed saved Daily Applied Count: " + savedCount);
    }



    /**
     * Creates authorization headers for the API requests.
     *
     * @return A map containing the authorization headers.
     */
    private Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // Example credentials
        String credentials = userEmail + ":" + userPassword;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
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
