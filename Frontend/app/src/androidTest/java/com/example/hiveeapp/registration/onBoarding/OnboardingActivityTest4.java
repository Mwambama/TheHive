package com.example.hiveeapp.registration.onBoarding;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.UiController;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.hiveeapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OnboardingActivityTest4 {

    @Rule
    public ActivityScenarioRule<OnboardingActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(OnboardingActivity.class);

    @Test
    public void testNavigateAndLoginWithSavedCredentials() {
        // Wait to ensure the activity has fully loaded
        onView(isRoot()).perform(waitFor(1000));

        // Step 1: Click "Get Started" to proceed to the login screen
        onView(allOf(withId(R.id.getStartedButton), withText("Get Started")))
                .perform(scrollTo(), click());

        // Step 2: Click "Login" to go to the login form
        onView(allOf(withId(R.id.main_login_btn), withText("Login")))
                .perform(scrollTo(), click());

        // Step 3: Retrieve saved credentials from SharedPreferences
        Context context = androidx.test.core.app.ApplicationProvider.getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        // Ensure credentials exist
        assert !email.isEmpty() : "Email not found in SharedPreferences";
        assert !password.isEmpty() : "Password not found in SharedPreferences";

        // Step 4: Enter saved credentials into the login form
        onView(withId(R.id.emailField)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordField)).perform(typeText(password), closeSoftKeyboard());

        // Step 5: Click the "Login" button
        onView(withId(R.id.loginButton)).perform(scrollTo(), click());

        // Step 6: Verify navigation to the main screen
        //onView(withId(R.id.student_main_activity_root)) // Assuming this is the root ID for StudentMainActivity
              //  .check(matches(isDisplayed()));
    }

//    @Test
//    public void testNavigateAndLoginWithSavedCredentials() {
//        // Wait to ensure the activity has fully loaded
//        onView(isRoot()).perform(waitFor(1000));
//
//        // Step 1: Click "Get Started" to proceed to the login screen
//        onView(allOf(withId(R.id.getStartedButton), withText("Get Started")))
//                .perform(scrollTo(), click());
//
//        // Step 2: Click "Login" to go to the login form
//        onView(allOf(withId(R.id.main_login_btn), withText("Login")))
//                .perform(scrollTo(), click());
//
//        // Step 3: Retrieve saved credentials from SharedPreferences
//        Context context = androidx.test.core.app.ApplicationProvider.getApplicationContext();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//        String email = sharedPreferences.getString("email", "");
//        String password = sharedPreferences.getString("password", "");
//
//        // Ensure credentials exist
//        assert !email.isEmpty() : "Email not found in SharedPreferences";
//        assert !password.isEmpty() : "Password not found in SharedPreferences";
//
//        // Step 4: Enter saved credentials into the login form
//        onView(withId(R.id.emailField)).perform(typeText(email), closeSoftKeyboard());
//        onView(withId(R.id.passwordField)).perform(typeText(password), closeSoftKeyboard());
//
//        // Step 5: Click the "Login" button
//        onView(withId(R.id.loginButton)).perform(scrollTo(), click());
//
//        // Step 6: Verify navigation to the StudentMainActivity
//        onView(withId(R.id.bottomNavigationView)) // Verify BottomNavigationView is displayed
//                .check(matches(isDisplayed()));
//
//        // Alternatively, verify the TabLayout for swiping jobs
//        onView(withId(R.id.tabLayout)) // Verify TabLayout is displayed
//                .check(matches(isDisplayed()));
//    }

    // Custom ViewAction for adding delays
    private static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
