package com.example.hiveeapp.student_user.swipe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import com.example.hiveeapp.volley.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplyActivity extends AppCompatActivity {

    private static final String TAG = "ApplyActivity";
    private int studentId;
    private int jobPostingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        studentId = getIntent().getIntExtra("studentId", -1);
        jobPostingId = getIntent().getIntExtra("jobPostingId", -1);
    }

    private void applyForJob(int studentId, int jobPostingId) {
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/applications/apply";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("studentId", studentId);
            jsonObject.put("jobPostingId", jobPostingId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_SHORT).show(),
                this::handleError);

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void handleError(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 500) {
            Toast.makeText(this, "You have already applied for this job.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to apply. Please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.e(TAG, "Error applying for job: ", error);
    }
}
