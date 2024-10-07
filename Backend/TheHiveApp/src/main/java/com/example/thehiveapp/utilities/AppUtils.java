package com.example.thehiveapp.utilities;

import java.util.Random;

public class AppUtils {
    public static final String FORGET_PASSWORD_BODY = "The Hive has sent you an OTP to reset " +
            "your password. This otp expires in 1 hour. Please paste in the app to reset.\nOTP: ";
    public static String generateOtp(){
        StringBuilder otp = new StringBuilder();
        Random rand = new Random();
        int count = 0;
        while(count < 5){
            otp.append(rand.nextInt(10));
            count++;
        }
        return otp.toString();
    }
}
