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
    private Button updateInfoBtn;
    private EditText nameEditText, passwordEditText, companyIdEditText, emailEditText, phoneEditText, fieldEditText;
    private EditText streetEditText, complementEditText, cityEditText, stateEditText, zipCodeEditText;

    private String userId, addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_arr_request2);

        textView = findViewById(R.id.textView);
        updateInfoBtn = findViewById(R.id.update_info_btn);

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

        String fetchUrl = "http://coms-3090-063.class.las.iastate.edu:8080/account/employer/{employerId}";
        fetchEmployerInfo(fetchUrl.replace("{employerId}", userId));

        String updateUrl = "http://coms-3090-063.class.las.iastate.edu:8080/account/update/employer";

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployerInfo(updateUrl);
            }
        });
    }

    private void fetchEmployerInfo(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        populateEmployerInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Error: " + error.toString());
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void populateEmployerInfo(JSONObject response) {
        try {
            nameEditText.setText(response.getString("name"));
            emailEditText.setText(response.getString("email"));
            phoneEditText.setText(response.getString("phone"));
            fieldEditText.setText(response.getString("field"));

            JSONObject address = response.getJSONObject("address");
            streetEditText.setText(address.getString("street"));
            complementEditText.setText(address.getString("complement"));
            cityEditText.setText(address.getString("city"));
            stateEditText.setText(address.getString("state"));
            zipCodeEditText.setText(address.getString("zipCode"));

            userId = response.getString("userId");
            addressId = address.getString("addressId");
            companyIdEditText.setText(response.getString("companyId"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing employer info", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmployerInfo(String updateUrl) {
        JSONObject updateData = new JSONObject();
        try {
            updateData.put("name", nameEditText.getText().toString());
            updateData.put("password", passwordEditText.getText().toString());
            updateData.put("companyId", companyIdEditText.getText().toString());
            updateData.put("email", emailEditText.getText().toString());
            updateData.put("phone", phoneEditText.getText().toString());
            updateData.put("field", fieldEditText.getText().toString());

            JSONObject address = new JSONObject();
            address.put("street", streetEditText.getText().toString());
            address.put("complement", complementEditText.getText().toString());
            address.put("city", cityEditText.getText().toString());
            address.put("state", stateEditText.getText().toString());
            address.put("zipCode", zipCodeEditText.getText().toString());

            updateData.put("address", address);
            updateData.put("userId", userId);
            updateData.put("addressId", addressId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, updateUrl, updateData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleUpdateResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Error: " + error.toString());
                    }
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void handleUpdateResponse(JSONObject response) {
        StringBuilder responseData = new StringBuilder();
        try {
            String status = response.getString("status");
            String message = response.getString("message");
            responseData.append("Status: ").append(status).append("\n");
            responseData.append("Message: ").append(message).append("\n\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setText(responseData.toString());
    }
}
