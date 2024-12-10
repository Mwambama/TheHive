package com.example.hiveeapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.hiveeapp.student_user.profile.StudentProfileActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StudentProfileUpdateTest {

    @Rule
    public ActivityScenarioRule<StudentProfileActivity> activityRule =
            new ActivityScenarioRule<>(StudentProfileActivity.class);

    @Before
    public void setUp() {
        // Launch the activity with the required intent
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), StudentProfileActivity.class);
        intent.putExtra("USER_ID", 1017); // Pass the required user ID
        activityRule.getScenario().onActivity(activity -> activity.startActivity(intent));
    }

    @After
    public void tearDown() {
        // Clean up after test execution
    }

    @Test
    public void testUpdateProfileInfoWithScroll() throws InterruptedException {
        // Step 1: Update Profile Fields
        onView(withId(R.id.profileName))
                .perform(replaceText("Updated Name"), closeSoftKeyboard());
        onView(withId(R.id.profileEmail))
                .perform(replaceText("updated_email@example.com"), closeSoftKeyboard());
        onView(withId(R.id.profilePhone))
                .perform(replaceText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.profileUniversity))
                .perform(replaceText("Updated University"), closeSoftKeyboard());
        onView(withId(R.id.profileGraduationDate))
                .perform(replaceText("2024-12-31"), closeSoftKeyboard());
        onView(withId(R.id.profileGPA))
                .perform(replaceText("3.9"), closeSoftKeyboard());
        onView(withId(R.id.profileStreet))
                .perform(replaceText("123 Updated St"), closeSoftKeyboard());
        onView(withId(R.id.profileCity))
                .perform(replaceText("Updated City"), closeSoftKeyboard());
        onView(withId(R.id.profileState))
                .perform(replaceText("NY"), closeSoftKeyboard());
        onView(withId(R.id.profileZipCode))
                .perform(replaceText("12345"), closeSoftKeyboard());

        // Step 2: Scroll to the Bottom Using UiAutomator
        scrollToBottomWithUiAutomator();

        // Step 3: Save Changes
        onView(withId(R.id.saveProfileButton)).perform(click());

        // Wait for save operation to complete
        Thread.sleep(2000);

        // Step 4: Verify Success Message
        onView(withText("Profile updated successfully"))
                .check(matches(isDisplayed()));

        // Step 5: Verify Updated Profile in View
        onView(withId(R.id.profileNameView)).check(matches(withText("Updated Name")));
        onView(withId(R.id.profileEmailView)).check(matches(withText("updated_email@example.com")));
        onView(withId(R.id.profilePhoneView)).check(matches(withText("1234567890")));
        onView(withId(R.id.profileUniversityView)).check(matches(withText("Updated University")));
        onView(withId(R.id.profileGraduationDateView)).check(matches(withText("2024-12-31")));
        onView(withId(R.id.profileGpaView)).check(matches(withText("3.9")));
        onView(withId(R.id.profileStreet)).check(matches(withText("123 Updated St")));
        onView(withId(R.id.profileCity)).check(matches(withText("Updated City")));
        onView(withId(R.id.profileState)).check(matches(withText("NY")));
        onView(withId(R.id.profileZipCode)).check(matches(withText("12345")));
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
