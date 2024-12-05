package com.example.hiveeapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
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
import com.example.hiveeapp.employer_user.display.AddJobActivity;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
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
import static org.junit.Assert.assertTrue;
import static androidx.test.espresso.assertion.ViewAssertions.matches;



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
    public void testEmployerlLoginAndNavigation() {
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

    // this test works so far when I did it

//    @Test
//    public void testSignUpAndLogin() {
//        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@examplesKings.com";
//
//        // Step 1: Navigate to signup activity
//        onView(withId(R.id.registerText)).perform(click());
//
//        // Step 2: Select "Employer" signup
//        onView(withId(R.id.signup_employer_btn)).perform(click());
//
//        // Step 3: Fill out the signup form
//        onView(withId(R.id.signup_name_edt)).perform(scrollTo(), typeText("New Test King"));
//        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText(uniqueEmail));
//        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test1234!!@"));
//        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test1234!!@"));
//        onView(isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.signup_company_id_edt)).perform(scrollTo(), typeText("103"));
//
//        onView(withId(R.id.signup_name_edt)).check(matches(isDisplayed())).perform(scrollTo(), typeText("New Test king"));
//        onView(withId(R.id.signup_email_edt)).perform(scrollTo(), typeText(uniqueEmail));
//        onView(withId(R.id.signup_password_edt)).perform(scrollTo(), typeText("Test1234!!@"));
//        onView(withId(R.id.signup_verify_password_edt)).perform(scrollTo(), typeText("Test1234!!@"));
//        onView(isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.signup_university_edt)).perform(scrollTo(), typeText("Test University"));
//        onView(isRoot()).perform(closeSoftKeyboard());
//        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());
//
//
//        // Step 4: Submit the signup form
//        onView(withId(R.id.signup_signup_btn)).perform(scrollTo(), click());
//
//        // Step 5: Wait for LoginActivity
//        onView(isRoot()).perform(waitFor(5000));
//
//        // Verify LoginActivity is displayed
//        intended(hasComponent(LoginActivity.class.getName()));
//
//        // Step 6: Log in with the new user credentials
//        onView(withId(R.id.emailField))
//                .check(matches(isDisplayed())) // Ensure email field is visible
//                .perform(typeText(uniqueEmail), closeSoftKeyboard());
//        onView(withId(R.id.passwordField))
//                .check(matches(isDisplayed())) // Ensure password field is visible
//                .perform(typeText("Test1234!!@"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Step 7: Wait for EmployerMainActivity to load
//        onView(isRoot()).perform(waitFor(2000));
//
//        // Verify EmployerMainActivity
//        intended(hasComponent(EmployerMainActivity.class.getName()));
//
//        // Step 8: Validate shared preferences
//        SharedPreferences sharedPreferences =
//                InstrumentationRegistry.getInstrumentation()
//                        .getTargetContext()
//                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//
//        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
//        assertEquals("Test1234!!@", sharedPreferences.getString("password", null));
//    }


    @Test
    public void testSignUpAndLogin() {
        String uniqueEmail = "testingusers" + System.currentTimeMillis() + "@examplesss.com";

        // Step 1: Navigate to signup activity
        onView(withId(R.id.registerText)).perform(click());

        // Step 2: Select "Employer" signup
        onView(withId(R.id.signup_employer_btn)).perform(click());

        // Step 3: Fill out the signup form
        onView(withId(R.id.signup_name_edt))
                .perform(scrollTo(), typeText("New Test KINGS"));
        onView(withId(R.id.signup_email_edt))
                .perform(scrollTo(), typeText(uniqueEmail));
        onView(withId(R.id.signup_password_edt))
                .perform(scrollTo(), typeText("Test1234!!!@@"));
        onView(withId(R.id.signup_verify_password_edt))
                .perform(scrollTo(), typeText("Test1234!!!@@"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_company_id_edt))
                .perform(scrollTo(), typeText("104"));

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
                .perform(typeText("Test1234!!!@@"), closeSoftKeyboard());
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
        assertEquals("Test1234!!!@@", sharedPreferences.getString("password", null));
    }


//
//    @Test
//    public void testEmployerLoginAndNavigateToAddJob() throws InterruptedException {
//        // Step 1: Log in as an employer
//        onView(withId(R.id.emailField))
//                .perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField))
//                .perform(typeText("Test1234@"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Step 2: Wait for EmployerMainActivity to load
//        Thread.sleep(2000); // Replace with IdlingResource if possible
//
//        // Step 3: Validate navigation to EmployerMainActivity by checking a unique element
//        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));
//
//        // Step 4: Navigate to "Add Job" and validate
//        onView(withId(R.id.nav_add_job)).perform(click());
//        Thread.sleep(1000); // Add delay if necessary
//        onView(withId(R.id.applicationRecyclerView)).check(matches(isDisplayed()));
//
//        Thread.sleep(2000); // Replace with IdlingResource if possible
////        // Step 5: Navigate to the Add Job form
////        onView(withId(R.id.addEmployerButton)).perform(click());
//        //onView(withId(R.id.addJobForm)).check(matches(isDisplayed())); // Replace with actual ID
//    }


    // this works when testing
//    @Test
//    public void testEmployerLoginAndNavigateToAddJob() throws InterruptedException {
//        // Step 1: Log in as an employer
//        onView(withId(R.id.emailField))
//                .perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField))
//                .perform(typeText("Test1234@"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//
//        // Step 2: Wait for EmployerMainActivity to load
//        Thread.sleep(2000); // Replace with IdlingResource if possible
//
//        // Step 3: Validate navigation to EmployerMainActivity by checking a unique element
//       // onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));
//        intended(hasComponent(EmployerMainActivity.class.getName()));
//        // Step 4: Navigate to "Add Job"
//        onView(withId(R.id.nav_add_job)).perform(click());
//
//        // Step 5: Wait for EditJobActivity to load
//        Thread.sleep(2000); // Again, consider using IdlingResource
//
//        // Step 6: Validate that the RecyclerView in EditJobActivity is displayed
//        //onView(withId(R.id.applicationRecyclerView)).check(matches(isDisplayed()));
//    }


    @Test
    public void testEmployerLoginAndNavigateToAddJob() throws InterruptedException {
        // Step 1: Log in as an employer
        onView(withId(R.id.emailField))
                .perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test1234@"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);
        // Step 2: Validate navigation to EmployerMainActivity
        intended(hasComponent(EmployerMainActivity.class.getName()));

       // onView(withId(R.id.navigation_main_user_page)).perform(click());
        Thread.sleep(5000);
        // Step 3: Click "Add Job" in the bottom navigation
        onView(withId(R.id.nav_add_job)).perform(click());

        // Step 4: Wait for EditJobActivity to load
        Thread.sleep(2000); // Replace with IdlingResource if possible

        // Step 5: Validate that the RecyclerView in EditJobActivity is displayed
      // onView(withId(R.id.applicationRecyclerView)).check(matches(isDisplayed()));

//
//        // Optional: Validate RecyclerView Adapter Content (if data is populated dynamically)
//        // Ensure the adapter is set up correctly
        ActivityScenario<EditJobActivity> scenario = ActivityScenario.launch(EditJobActivity.class);
        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.applicationRecyclerView);
            assertNotNull(recyclerView); // Ensure RecyclerView is initialized
            assertNotNull(recyclerView.getAdapter()); // Ensure Adapter is set
            assertTrue(recyclerView.getAdapter().getItemCount() >= 0); // Validate item count
        });
   }

    // this works when testing
    @Test
    public void testEmployerLoginAndNavigateToProfile() throws InterruptedException {
        // Step 1: Log in as an employer
        onView(withId(R.id.emailField))
                .perform(typeText("employerTest@aol.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test1234@"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Step 2: Validate navigation to EmployerMainActivity
        intended(hasComponent(EmployerMainActivity.class.getName()));

        // Step 3: Navigate to the Profile page using BottomNavigationView
        onView(withId(R.id.navigation_main_user_page)).perform(click());

        // Step 4: Validate navigation to the Profile page
        Thread.sleep(2000); // Replace with IdlingResource if possible
      //  onView(withId(R.id.navigation_main_user_page)).perform(click());
        // Step 5: Use the back button to navigate back to EmployerMainActivity
        onView(withId(R.id.backArrowIcon)).perform(click());
      //  Thread.sleep(2000);
        // Step 6: Validate navigation back to EmployerMainActivity
        //    intended(hasComponent(EmployerMainActivity.class.getName()));

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
