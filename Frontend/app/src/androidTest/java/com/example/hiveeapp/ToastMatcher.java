package com.example.hiveeapp;

import android.view.WindowManager;
import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import androidx.test.espresso.Root;

public class ToastMatcher extends TypeSafeMatcher<Root> {
    @Override
    public void describeTo(Description description) {
        description.appendText("is a toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
            View decorView = root.getDecorView();
            return decorView != null && decorView.getWindowToken() == decorView.getApplicationWindowToken();
        }
        return false;
    }
}