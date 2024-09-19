package com.example.hiveeapp.company_user;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.hiveeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class usersActivity extends AppCompatActivity {

    private TextView usersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersTextView = findViewById(R.id.textView);

        // Load the users (employers) from the JSON file in assets
        String jsonStr = loadJSONFromAsset("sampledata/json/company/get_company.json");
        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);
                JSONArray employers = company.getJSONArray("employers");

                StringBuilder usersBuilder = new StringBuilder();
                for (int i = 0; i < employers.length(); i++) {
                    JSONObject employer = employers.getJSONObject(i);
                    String employerName = employer.getString("name");
                    String position = employer.getString("position");
                    usersBuilder.append("Name: ").append(employerName)
                            .append(", Position: ").append(position)
                            .append("\n");
                }

                // Set the users text in the TextView
                usersTextView.setText(usersBuilder.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to load JSON from assets
    public String loadJSONFromAsset(String fileName) {
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

