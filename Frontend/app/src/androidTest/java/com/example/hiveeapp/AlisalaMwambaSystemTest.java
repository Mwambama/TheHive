package com.example.hiveeapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.idling.CountingIdlingResource;
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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AlisalaMwambaSystemTest {
//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        assertEquals("com.example.hiveeapp", appContext.getPackageName());
//    }


    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    private CountingIdlingResource idlingResource;

    @Before
    public void setup() {
        Intents.init(); // Initialize Espresso Intents
        idlingResource = new CountingIdlingResource("SignupLoader");
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void tearDown() {
        Intents.release(); // Release Espresso Intents
        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testSuccessfulLoginAndNavigation() {
        // Step 1: Set the input fields
        onView(withId(R.id.emailField)).perform(typeText("test643@example.com"));
        onView(withId(R.id.passwordField)).perform(typeText("Test$1234"));

        // Step 2: Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Step 3: Check if the correct activity is launched based on role
        intended(hasComponent(StudentMainActivity.class.getName())); // Assuming role is STUDENT

        // Step 4: Validate shared preferences were saved
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals("test643@example.com", sharedPreferences.getString("email", null));
        assertEquals("Test$1234", sharedPreferences.getString("password", null));
    }

    @Test
    public void testSignUpAndLogin() {
        // Navigate to signup activity
        onView(withId(R.id.registerText)).perform(click());

        // Select "Student" signup
        onView(withId(R.id.signup_student_btn)).perform(click());

        // Fill out the signup form
        onView(withId(R.id.signup_name_edt)).perform(scrollTo(), typeText("New Test User"));
        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText("newuser@example.com"));
        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test1234!"));
        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test1234!"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_company_id_edt)).perform(scrollTo(), typeText("101"));
        onView(withId(R.id.signup_university_edt)).perform(scrollTo(), typeText("Test University"));

        // Submit the signup form
        idlingResource.increment(); // Notify IdlingResource of loading
        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());

        // Wait for the app to navigate to the LoginActivity
        onView(isRoot()).perform(waitForActivity(LoginActivity.class, 5000));

        // Verify navigation to LoginActivity
        intended(hasComponent(LoginActivity.class.getName()));

        // Log in with the new user credentials
        onView(withId(R.id.emailField)).perform(typeText("newuser@example.com"));
        onView(withId(R.id.passwordField)).perform(typeText("Test1234!"));
        onView(withId(R.id.loginButton)).perform(click());

        // Verify navigation to StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Validate shared preferences
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals("newuser@example.com", sharedPreferences.getString("email", null));
        assertEquals("Test1234!", sharedPreferences.getString("password", null));

        idlingResource.decrement(); // Notify IdlingResource of completion
    }

    // NEW TEST: Validate UI elements on the LoginActivity screen
    @Test
    public void testLoginActivityUIElements() {
        // Verify that email and password fields are displayed
        onView(withId(R.id.emailField)).perform(click());
        onView(withId(R.id.passwordField)).perform(click());

        // Verify that login button is enabled
        onView(withId(R.id.loginButton)).perform(click());
    }

    private static ViewAction waitForActivity(final Class<?> activityClass, final long timeoutMillis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + activityClass.getSimpleName() + " to load within " + timeoutMillis + "ms.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                long endTime = System.currentTimeMillis() + timeoutMillis;
                while (System.currentTimeMillis() < endTime) {
                    Activity activity = ActivityLifecycleMonitorRegistry.getInstance()
                            .getActivitiesInStage(Stage.RESUMED)
                            .stream()
                            .filter(a -> a.getClass().equals(activityClass))
                            .findFirst()
                            .orElse(null);

                    if (activity != null) {
                        return;
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                throw new PerformException.Builder()
                        .withActionDescription(getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .build();
            }

        };
    }
}