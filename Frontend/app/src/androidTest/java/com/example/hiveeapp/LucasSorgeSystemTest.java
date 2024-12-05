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

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;

import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LucasSorgeSystemTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testSuccessfulLoginAndNavigation() {
        onView(withId(R.id.emailField)).perform(typeText("test643@example.com"));
        onView(withId(R.id.passwordField)).perform(typeText("Test$1234"));

        onView(withId(R.id.loginButton)).perform(click());

        intended(hasComponent(StudentMainActivity.class.getName()));

        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals("test643@example.com", sharedPreferences.getString("email", null));
        assertEquals("Test$1234", sharedPreferences.getString("password", null));
    }

    @Test
    public void testSignUpAndLogin() throws InterruptedException {
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";

        onView(withId(R.id.registerText)).perform(click());

        onView(withId(R.id.signup_student_btn)).perform(click());

        onView(withId(R.id.signup_name_edt)).check(matches(isDisplayed())).perform(scrollTo(), typeText("New Test User"));
        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText(uniqueEmail));
        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test@1234"));
        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test@1234"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_university_edt)).perform(scrollTo(), typeText("Test University"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());

        Thread.sleep(5000);

        onView(withId(R.id.emailField)).check(matches(isDisplayed()));

        onView(withId(R.id.emailField))
                .perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(2000);

        intended(hasComponent(StudentMainActivity.class.getName()));

        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
        assertEquals("Test@1234", sharedPreferences.getString("password", null));
    }

    @Test
    public void testLoginNavigateAndUpdateProfile() throws InterruptedException{
        onView(withId(R.id.emailField))
                .perform(typeText("test643@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(2000);

        intended(hasComponent(StudentMainActivity.class.getName()));

        onView(withId(R.id.navigation_profile)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.profileScrollView)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginAndNavigateToChat() throws InterruptedException {
        onView(withId(R.id.emailField))
                .perform(typeText("test643@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        Thread.sleep(2000);

        intended(hasComponent(StudentMainActivity.class.getName()));

        onView(withId(R.id.navigation_chat)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.chatRecyclerView))
                .check(matches(isDisplayed()));
    }
}
