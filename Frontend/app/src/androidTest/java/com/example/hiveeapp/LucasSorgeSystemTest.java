package com.example.hiveeapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LucasSorgeSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init(); // Initialize Espresso Intents
    }

    @After
    public void tearDown() {
        Intents.release(); // Release Espresso Intents
    }

    @Test
    public void testSuccessfulLoginAndNavigation() {
        // Log in with an existing user
        onView(withId(R.id.emailField)).perform(typeText("test643@example.com"));
        onView(withId(R.id.passwordField)).perform(typeText("Test$1234"));

        onView(withId(R.id.loginButton)).perform(click());

        // Verify navigation to StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Validate shared preferences were saved
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals("test643@example.com", sharedPreferences.getString("email", null));
        assertEquals("Test$1234", sharedPreferences.getString("password", null));
    }

    @Test
    public void testSignUpAndLogin() throws InterruptedException {
        // Generate a unique email for each test run
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";

        // Navigate to signup activity
        onView(withId(R.id.registerText)).perform(click());

        // Select "Student" signup
        onView(withId(R.id.signup_student_btn)).perform(click());

        // Fill out the signup form
        onView(withId(R.id.signup_name_edt)).perform(scrollTo(), typeText("New Test User"));
        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText(uniqueEmail));
        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test@1234"));
        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test@1234"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_university_edt)).perform(scrollTo(), typeText("Test University"));

        // Submit the signup form
        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());

        // Wait for API response and navigation to the login page
        Thread.sleep(5000); // Use IdlingResource in production

        // Ensure the login page is displayed
        onView(withId(R.id.emailField)).check(matches(isDisplayed()));

        // Log in with the newly created user credentials
        onView(withId(R.id.emailField))
                .perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for the navigation to StudentMainActivity
        Thread.sleep(2000);

        // Verify navigation to StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Validate shared preferences
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
        assertEquals("Test@1234", sharedPreferences.getString("password", null));
    }
}
