package com.example.hiveeapp.student_user.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.invitations.InvitationManagementActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * StudentListActivity handles the display and management of a list of students.
 * It allows users to view, add, and interact with students, and also includes a bottom navigation for navigation to other activities.
 */
public class StudentListActivity extends AppCompatActivity {

    private RecyclerView studentRecyclerView;   // RecyclerView for displaying the list of students
    private StudentAdapter studentAdapter;     // Adapter for managing student data in the RecyclerView
    private MaterialButton addStudentButton;    // Button for adding a new student

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize views
        initViews();

        // Set up "Add Student" button to navigate to StudentCreationActivity
        addStudentButton.setOnClickListener(v -> {
            Intent intent = new Intent(StudentListActivity.this, StudentUpdateActivity.class);
            startActivity(intent);
        });

        // Set up the RecyclerView with a linear layout and the student adapter
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this, true);  // true indicates editable mode
        studentRecyclerView.setAdapter(studentAdapter);

        // Load the list of students from the server
        loadStudents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the student list when the activity is resumed
        loadStudents();
    }

    /**
     * Initialize views in the activity.
     */
    private void initViews() {
        studentRecyclerView = findViewById(R.id.studentRecyclerView);
        addStudentButton = findViewById(R.id.addStudentButton);
    }

    /**
     * Load the list of students from the server using StudentApi and update the adapter.
     * The students are reversed before being set in the adapter.
     */
    private void loadStudents() {
        StudentApi.getStudents(this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Reverse the JSONArray before setting it in the adapter
                        JSONArray reversedStudents = reverseJSONArray(response);
                        studentAdapter.setStudents(reversedStudents);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error when fetching students fails
                        Toast.makeText(StudentListActivity.this, "Error fetching students: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

