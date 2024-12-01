package com.example.hiveeapp.registration.onBoarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.hiveeapp.MainActivity;
import com.example.hiveeapp.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);
        com.example.hiveeapp.onboarding.OnboardingAdapter adapter = new com.example.hiveeapp.onboarding.OnboardingAdapter();

        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);
    }
}
