package com.example.hiveeapp.employer_user.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    public static Map<String, String> getHeaders(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userEmail = preferences.getString("email", "");
        String userPassword = preferences.getString("password", "");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String credentials = userEmail + ":" + userPassword;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", auth);

        return headers;
    }
}

