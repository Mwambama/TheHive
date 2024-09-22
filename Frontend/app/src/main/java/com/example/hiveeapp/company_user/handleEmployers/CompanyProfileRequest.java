package com.example.hiveeapp.company_user.handleEmployers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hiveeapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class CompanyProfileRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the company profile from the assets folder
        String jsonStr = loadJSONFromAsset(this, "sampledata/json/company/get_company.json");

        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);
                String companyName = company.getString("name");
                String companyImage = company.getString("profile_image");

                // Use this data to populate the UI
                // e.g., companyNameTextView.setText(companyName);
                Log.d("Company Name", companyName);
                Log.d("Company Image", companyImage);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to load JSON from assets
    public String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

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