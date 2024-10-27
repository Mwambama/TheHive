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
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Retrieve userId from intent or other shared preference mechanism
        userId = getIntent().getIntExtra("USER_ID", -1);

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
        if (userId == -1) {  // Handle case where userId was not provided
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        StudentApi.getStudents(this, userId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                studentAdapter.setStudents(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentListActivity.this, "Error fetching student profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

