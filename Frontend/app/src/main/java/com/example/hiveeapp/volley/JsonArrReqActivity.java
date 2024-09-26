package com.example.hiveeapp.volley;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hiveeapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonArrReqActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        textView = findViewById(R.id.textView);

        // Load the JSON data from assets
        String jsonString = loadJSONFromAsset("sampledata/json/company/get_all_companies.json");

        if (jsonString != null) {
            try {
                // Parse the JSON array
                JSONArray jsonArray = new JSONArray(jsonString);
                StringBuilder jsonData = new StringBuilder();

                // Loop through the array and append each company's details
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject company = jsonArray.getJSONObject(i);

                    // Get basic company info
                    String companyName = company.getString("name");
                    String companyEmail = company.getString("email");
                    String companyPhone = company.getString("phone");

                    // Get the nested address object
                    JSONObject address = company.getJSONObject("address");
                    String street = address.getString("street");
                    String complement = address.getString("complement");
                    String city = address.getString("city");
                    String state = address.getString("state");
                    int zipCode = address.getInt("zip_code");

                    // Build the display string
                    jsonData.append("Company Name: ").append(companyName)
                            .append("\nEmail: ").append(companyEmail)
                            .append("\nPhone: ").append(companyPhone)
                            .append("\nAddress: ").append(street)
                            .append(", ").append(complement.isEmpty() ? "" : complement + ", ")
                            .append(city).append(", ").append(state).append(" ").append(zipCode)
                            .append("\n\n");
                }

                // Display the data in the TextView
                textView.setText(jsonData.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                textView.setText("Error parsing JSON data");
            }
        } else {
            textView.setText("Failed to load JSON file");
        }
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}