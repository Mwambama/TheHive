package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.hiveeapp.R;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonArrRequestActivity2 extends AppCompatActivity {
    TextView textView;
    private Button updateInfoBtn, deleteInfoBtn;
    private EditText nameEditText, passwordEditText, companyIdEditText, emailEditText, phoneEditText, fieldEditText;
    private EditText streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText;

    private String userId = "314"; // Initialize with actual user ID
    private String addressId = "314"; // Initialize with actual address ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_arr_request2);

        textView = findViewById(R.id.textView);
        updateInfoBtn = findViewById(R.id.update_info_btn);
        deleteInfoBtn = findViewById(R.id.delete_info_btn);

        nameEditText = findViewById(R.id.signup_name_edt);
        passwordEditText = findViewById(R.id.signup_password_edt);
        companyIdEditText = findViewById(R.id.signup_company_id_edt);
        emailEditText = findViewById(R.id.signup_email_edt);
        phoneEditText = findViewById(R.id.signup_phone_edt);
        fieldEditText = findViewById(R.id.signup_field_edt);
        streetEditText = findViewById(R.id.signup_street_edt);
        complementEditText = findViewById(R.id.signup_complement_edt);
        cityEditText = findViewById(R.id.signup_city_edt);
        stateEditText = findViewById(R.id.signup_state_edt);
        zipCodeEditText = findViewById(R.id.signup_zipcode_edt);

        // Set click listeners for update and delete buttons
        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

        deleteInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo();
            }
        });
    }

    private void updateInfo() {
        // Collect data from EditText fields
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String companyId = companyIdEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String field = fieldEditText.getText().toString();
        String street = streetEditText.getText().toString();
        String complement = complementEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String state = stateEditText.getText().toString();
        String zipCode = zipCodeEditText.getText().toString();

        // Assuming the URL is set properly
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer/update" + userId;

        // Create a JSONObject with the collected data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("password", password);
            jsonObject.put("companyId", companyId);
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("field", field);
            jsonObject.put("street", street);
            jsonObject.put("complement", complement);
            jsonObject.put("city", city);
            jsonObject.put("state", state);
            jsonObject.put("zipCode", zipCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest to send the update request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        textView.setText("Update successful: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        textView.setText("Error: " + error.toString());
                    }
                });

        // Add the request to the VolleySingleton request queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void deleteInfo() {
        // Assuming the URL is set properly and userId is available
        String url = "http://coms-3090-063.class.las.iastate.edu:8080/employer/delete/" + userId;

        // Create a JsonObjectRequest to send the delete request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        textView.setText("Delete successful: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        textView.setText("Error: " + error.toString());
                    }
                });

        // Add the request to the VolleySingleton request queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
