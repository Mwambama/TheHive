package com.example.hiveeapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.registration.signup.studentsignupActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class LucasSorgeSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSuccessfulLoginAndNavigation() throws InterruptedException {
        onView(withId(R.id.emailField))
                .perform(typeText("test644@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for navigation
        Thread.sleep(1000);

        intended(hasComponent(StudentMainActivity.class.getName()));

        SharedPreferences sharedPreferences =
                ApplicationProvider.getApplicationContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);

        assertEquals("test644@example.com", savedEmail);
        assertEquals("Test$1234", savedPassword);
    }

    @Test
    public void testSuccessfulSignupAndLogin() throws InterruptedException {
        // Generate a unique email for the test
        String uniqueEmail = generateUniqueEmail();

        // Navigate to the role selection screen
        onView(withId(R.id.registerText)).perform(click());

        // Wait for the role selection screen to load
        Thread.sleep(1000);

        // Select "Student" role
        onView(withId(R.id.signup_student_btn)).perform(click());

        // Wait for the student signup page to load
        Thread.sleep(1000);

        // Signup test
        onView(withId(R.id.signup_name_edt))
                .perform(scrollTo(), typeText("Test User"), closeSoftKeyboard());
        onView(withId(R.id.signup_email_edt))
                .perform(scrollTo(), typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.signup_password_edt))
                .perform(scrollTo(), typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.signup_verify_password_edt))
                .perform(scrollTo(), typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.signup_university_edt))
                .perform(scrollTo(), typeText("Test University"), closeSoftKeyboard());

        // Explicitly scroll to the signup button using UiAutomator
        scrollToBottomWithUiAutomator();

        // Click the signup button
        onView(withId(R.id.signup_signup_btn)).perform(click());

        // Wait for navigation to login activity
        Thread.sleep(5000);

        // Login test
        onView(withId(R.id.emailField))
                .perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for navigation to main activity
        Thread.sleep(1000);

        // Check navigation to main activity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Verify SharedPreferences saved data
        SharedPreferences sharedPreferences =
                ApplicationProvider.getApplicationContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);

        // Assertions for the saved email and password
        assertEquals(uniqueEmail, savedEmail);
        assertEquals("Test1234", savedPassword);
    }

    // Helper method to generate a unique email
    private String generateUniqueEmail() {
        String uuid = UUID.randomUUID().toString().substring(0, 8); // Shorten UUID for brevity
        return "testuser_" + uuid + "@example.com";
    }

    // Helper method to scroll using UiAutomator
    private void scrollToBottomWithUiAutomator() {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Swipe from the bottom to the top multiple times
        for (int i = 0; i < 3; i++) {
            device.swipe(500, 1500, 500, 500, 10); // Adjust coordinates based on the device screen
        }
    }
}


