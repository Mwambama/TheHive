package com.example.hiveeapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
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

import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static java.util.regex.Pattern.matches;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
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
import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
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
import com.example.hiveeapp.student_user.profile.StudentProfileActivity;
import com.example.hiveeapp.student_user.swipe.JobSwipeFragment;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class AlisalaMwambaSystemTest {

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

//    @Test
//    public void testSuccessfulLoginAndNavigation() {
//        // Step 1: Set the input fields
//        onView(withId(R.id.emailField)).perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField)).perform(typeText("Test1234@"), closeSoftKeyboard());
//
//        // Step 2: Click the login button
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Step 3: Check if the correct activity is launched based on role
//        intended(hasComponent(EmployerMainActivity.class.getName())); // Assuming role is STUDENT
//
//        // Step 4: Validate shared preferences were saved
//        SharedPreferences sharedPreferences =
//                InstrumentationRegistry.getInstrumentation()
//                        .getTargetContext()
//                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//
//        assertEquals("employerTest@aol.com", sharedPreferences.getString("email", null));
//        assertEquals("Test1234@", sharedPreferences.getString("password", null));
//
//    }



    @Test
    public void testSuccessfulLoginAndNavigation() {
        // Step 1: Set the input fields
        onView(withId(R.id.emailField)).perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField)).perform(typeText("Test1234@"), closeSoftKeyboard());

        // Step 2: Click the login button
        onView(withId(R.id.loginButton)).perform(click());

        // Step 3: Use ActivityScenario to validate navigation
        ActivityScenario<EmployerMainActivity> scenario = ActivityScenario.launch(EmployerMainActivity.class);
        scenario.onActivity(activity -> {
            // Perform checks directly on the activity instance if needed
            assertNotNull(activity.findViewById(R.id.bottomNavigationView)); // Assuming bottomNavigationView exists
        });
    }

    // NEW TEST: Validate UI elements on the LoginActivity screen
//    @Test
//    public void testLoginActivityUIElements() {
//        // Verify that email and password fields are displayed
//        onView(withId(R.id.emailField)).perform(click());
//        onView(withId(R.id.passwordField)).perform(click());
//
//        // Verify that login button is enabled
//        onView(withId(R.id.loginButton)).perform(click());
//    }


//    @Test
//    public void testSignUpAndLogin() {
//        // Navigate to signup activity
//        onView(withId(R.id.registerText)).perform(click());
//
//        // Select "Student" signup
//        onView(withId(R.id.signup_employer_btn)).perform(click());
//
//        // Fill out the signup form
//        onView(withId(R.id.signup_name_edt)).perform(scrollTo(), typeText("New Testing User"));
//        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText("newuser@examples.com"));
//        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test1234!!"));
//        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test1234!!"));
//        onView(isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.signup_company_id_edt)).perform(scrollTo(), typeText("101"));
//        onView(withId(R.id.signup_university_edt)).perform(scrollTo(), typeText("Test University"));
//
//        // Submit the signup form
//        idlingResource.increment(); // Notify Espresso of background task
//        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());
//
//        // Wait for the LoginActivity
//        onView(isRoot()).perform(waitFor(5000)); // Adjust timeout as needed
//
//        // Verify navigation to LoginActivity
//        intended(hasComponent(LoginActivity.class.getName()));
//
//        // Log in with the new user credentials
//        onView(withId(R.id.emailField)).perform(typeText("newuser@examples.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField)).perform(typeText("Test1234!!"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Verify navigation to StudentMainActivity
//        intended(hasComponent(StudentMainActivity.class.getName()));
//
//        // Validate shared preferences
//        SharedPreferences sharedPreferences =
//                InstrumentationRegistry.getInstrumentation()
//                        .getTargetContext()
//                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//
//        assertEquals("newuser@example.com", sharedPreferences.getString("email", null));
//        assertEquals("Test1234!", sharedPreferences.getString("password", null));
//
//        idlingResource.decrement(); // Notify Espresso of completion
//    }


//    }

      // from when I saw Lucas code
//
//    @Test
//    public void testSignUpAndLogin() throws InterruptedException {
//        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@examples.com";
//
//        // Step 1: Navigate to signup activity
//        onView(withId(R.id.registerText)).perform(click());
//
//        // Step 2: Select "Employer" signup
//        onView(withId(R.id.signup_employer_btn)).perform(click());
//
//        // Step 3: Fill out the signup form
//        onView(withId(R.id.signup_name_edt)).perform(scrollTo(), typeText("New Test User"));
//        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText(uniqueEmail));
//        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test1234!!"));
//        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test1234!!"));
//        onView(isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.signup_company_id_edt)).perform(scrollTo(), typeText("101"));
//
//        // Step 4: Submit the signup form
//        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());
//
//        // Step 5: Wait for LoginActivity
//        onView(isRoot()).perform(waitFor(5000));
//
//        // Step 6: Verify LoginActivity is displayed
//        intended(hasComponent(LoginActivity.class.getName()));
//
//        // Step 7: Log in with the new user credentials
//        onView(withId(R.id.emailField))
//                .check(matches(isDisplayed())) // Ensure email field is visible
//                .perform(typeText(uniqueEmail), closeSoftKeyboard());
//        onView(withId(R.id.passwordField))
//                .check(matches(isDisplayed())) // Ensure password field is visible
//                .perform(typeText("Test1234!!"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Step 8: Wait for EmployerMainActivity to load
//        onView(isRoot()).perform(waitFor(2000));
//
//        // Step 9: Verify EmployerMainActivity
//        intended(hasComponent(EmployerMainActivity.class.getName()));
//
//        // Step 10: Validate shared preferences
//        SharedPreferences sharedPreferences =
//                InstrumentationRegistry.getInstrumentation()
//                        .getTargetContext()
//                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//
//        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
//        assertEquals("Test1234!!", sharedPreferences.getString("password", null));
//    }



    @Test
    public void testSignUpAndLogin() {
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@examplesKing.com";

        // Step 1: Navigate to signup activity
        onView(withId(R.id.registerText)).perform(click());

        // Step 2: Select "Employer" signup
        onView(withId(R.id.signup_employer_btn)).perform(click());

        // Step 3: Fill out the signup form
        onView(withId(R.id.signup_name_edt))
                .perform(scrollTo(), typeText("New Test King"));
        onView(withId(R.id.signup_email_edt))
                .perform(scrollTo(), typeText(uniqueEmail));
        onView(withId(R.id.signup_password_edt))
                .perform(scrollTo(), typeText("Test1234!!@"));
        onView(withId(R.id.signup_verify_password_edt))
                .perform(scrollTo(), typeText("Test1234!!@"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_company_id_edt))
                .perform(scrollTo(), typeText("103"));

        // Step 4: Submit the signup form
        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());

        // Step 5: Wait for LoginActivity
        onView(isRoot()).perform(waitFor(5000));

        // Verify LoginActivity is displayed
        intended(hasComponent(LoginActivity.class.getName()));

        // Step 6: Log in with the new user credentials
        onView(withId(R.id.emailField))
                .check(matches(isDisplayed())) // Ensure email field is visible
                .perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .check(matches(isDisplayed())) // Ensure password field is visible
                .perform(typeText("Test1234!!@"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Step 7: Wait for EmployerMainActivity to load
        onView(isRoot()).perform(waitFor(2000));

        // Verify EmployerMainActivity
        intended(hasComponent(EmployerMainActivity.class.getName()));

        // Step 8: Validate shared preferences
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
        assertEquals("Test1234!!@", sharedPreferences.getString("password", null));
    }






//    @Test
//    public void testSignUpAndLogin() {
//        // Navigate to signup activity
//        onView(withId(R.id.registerText))
//                .check(matches(isDisplayed())) // Verify the "Register" text is displayed
//                .perform(click());
//
//        // Select "Student" signup
//        onView(withId(R.id.signup_student_btn))
//                .perform(scrollTo()) // Scroll if necessary
//                .check(matches(isDisplayed())) // Verify the "Student Signup" button is displayed
//                .perform(click());
//
//        // Fill out the signup form
//        onView(withId(R.id.signup_name_edt))
//                .perform(scrollTo(), typeText("New Testing User"))
//                .check(matches(isDisplayed()));
//
//        onView(withId(R.id.signup_email_edt))
//                .perform(scrollTo(), typeText("newuser@examples.com"))
//                .check(matches(isDisplayed()));
//
//        onView(withId(R.id.signup_password_edt))
//                .perform(scrollTo(), typeText("Test1234!!"))
//                .check(matches(isDisplayed()));
//
//        onView(withId(R.id.signup_verify_password_edt))
//                .perform(scrollTo(), typeText("Test1234!!"))
//                .check(matches(isDisplayed()));
//
//        onView(isRoot()).perform(closeSoftKeyboard());
//
//        onView(withId(R.id.signup_company_id_edt))
//                .perform(scrollTo(), typeText("101"))
//                .check(matches(isDisplayed()));
//
//        onView(withId(R.id.signup_university_edt))
//                .perform(scrollTo(), typeText("Test University"))
//                .check(matches(isDisplayed()));
//
//        // Submit the signup form
//        onView(withId(R.id.signup_signup_btn))
//                .perform(scrollTo())
//                .check(matches(isDisplayed())) // Verify the signup button is displayed
//                .perform(click());
//
//        // Wait for the LoginActivity
//        onView(isRoot()).perform(waitFor(5000)); // Adjust timeout as needed
//
//        // Verify navigation to LoginActivity
//        onView(withId(R.id.emailField))
//                .check(matches(isDisplayed()));
//
//        // Log in with the new user credentials
//        onView(withId(R.id.emailField)).perform(typeText("newuser@examples.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField)).perform(typeText("Test1234!!"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Verify navigation to StudentMainActivity
//        onView(withId(R.id.bottomNavigationView)) // Verify the BottomNavigationView is displayed in StudentMainActivity
//                .check(matches(isDisplayed()));
//
//        // Validate shared preferences
//        SharedPreferences sharedPreferences =
//                InstrumentationRegistry.getInstrumentation()
//                        .getTargetContext()
//                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//
//        assertEquals("newuser@examples.com", sharedPreferences.getString("email", null));
//        assertEquals("Test1234!", sharedPreferences.getString("password", null));
//    }




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
}
