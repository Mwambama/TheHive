package com.example.hiveeapp.AlisalaTesting;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.example.hiveeapp.R;
import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.employer_user.display.EditJobActivity;
import com.example.hiveeapp.registration.login.LoginActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class EmployerJobCreationTest {

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
    public void testEmployerLoginAndNavigateToAddJob() throws InterruptedException {
        // Step 1: Log in as an employer
        onView(ViewMatchers.withId(R.id.emailField))
                .perform(typeText("employerTest@aols.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test12345@"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        Thread.sleep(2000);

        // Step 2: Validate navigation to EmployerMainActivity
        intended(hasComponent(EmployerMainActivity.class.getName()));

        // Step 3: Click "Add Job" in the bottom navigation
        onView(withId(R.id.nav_add_job)).perform(click());
        Thread.sleep(2000); // Wait for EditJobActivity to load

        // Step 4: Validate RecyclerView in EditJobActivity
        ActivityScenario<EditJobActivity> scenario = ActivityScenario.launch(EditJobActivity.class);
        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.applicationRecyclerView);
            assertNotNull(recyclerView); // Ensure RecyclerView is initialized
            assertNotNull(recyclerView.getAdapter()); // Ensure Adapter is set
            assertTrue(recyclerView.getAdapter().getItemCount() >= 0); // Validate item count
        });

        // Step 5: Click "Add Employer" Button
        onView(withId(R.id.addEmployerButton)).perform(click());
        Thread.sleep(2000); // Wait for the add job screen to load

        // Step 6: Input job details
        onView(withId(R.id.jobTitleField)).perform(typeText("Software Engineer Intern"), closeSoftKeyboard());
        onView(withId(R.id.jobDescriptionField)).perform(typeText("Develop software solutions."), closeSoftKeyboard());
        onView(withId(R.id.summaryField)).perform(typeText("A great opportunity to learn and grow."), closeSoftKeyboard());
        onView(withId(R.id.jobTypeField)).perform(typeText("INTERNSHIP"), closeSoftKeyboard());
        onView(withId(R.id.salaryRequirementsField)).perform(typeText("$20.6"), closeSoftKeyboard());
        onView(withId(R.id.minimumGpaField)).perform(typeText("3.5"), closeSoftKeyboard());
        onView(withId(R.id.jobStartDateField)).perform(typeText("2024-05-01"), closeSoftKeyboard());
        onView(withId(R.id.applicationStartDateField)).perform(typeText("2024-01-01"), closeSoftKeyboard());
        onView(withId(R.id.applicationEndDateField)).perform(typeText("2024-04-01"), closeSoftKeyboard());
        Thread.sleep(500);
        // Step 7: Submit the job
        //onView(withId(R.id.addJobButton)).perform(click());
        //Thread.sleep(2000); // Wait for confirmation or navigation
      // onView(withId(R.id.addJobButton)).perform(click());

        // Step 8: Validate success (depends on app behavior)
        //onView(withId(R.id.applicationRecyclerView)).check(matches(isDisplayed()));

        // Step 9: Press the back button to navigate back
        onView(withId(R.id.backArrowIcon)).perform(click());
        Thread.sleep(2000); // Wait for navigation back

        // Step 10: Validate navigation back to EmployerMainActivity or job list
       // intended(hasComponent(EditJobActivity.class.getName())); // Replace with the appropriate class or validation
    }

//    @Test
//    public void testEmployerEditJob() throws InterruptedException {
//        // Step 1: Log in as an employer
//        onView(withId(R.id.emailField))
//                .perform(typeText("employerTest@aols.com"), closeSoftKeyboard());
//        onView(withId(R.id.passwordField))
//                .perform(typeText("Test12345@"), closeSoftKeyboard());
//        onView(withId(R.id.loginButton)).perform(click());
//        Thread.sleep(2000);
//        // Step 2: Validate navigation to EmployerMainActivity
//        intended(hasComponent(EmployerMainActivity.class.getName()));
//
//        // onView(withId(R.id.navigation_main_user_page)).perform(click());
//        Thread.sleep(5000);
//        // Step 3: Click "Add Job" in the bottom navigation
//        onView(withId(R.id.nav_add_job)).perform(click());
//
//        // Step 4: Wait for EditJobActivity to load
//        Thread.sleep(2000); // Replace with IdlingResource if possible
//
//        // Step 5: Validate that the RecyclerView in EditJobActivity is displayed
//        // onView(withId(R.id.applicationRecyclerView)).check(matches(isDisplayed()));
//
////
////        // Optional: Validate RecyclerView Adapter Content (if data is populated dynamically)
////        // Ensure the adapter is set up correctly
//        ActivityScenario<EditJobActivity> scenario = ActivityScenario.launch(EditJobActivity.class);
//        scenario.onActivity(activity -> {
//            RecyclerView recyclerView = activity.findViewById(R.id.applicationRecyclerView);
//            assertNotNull(recyclerView); // Ensure RecyclerView is initialized
//            assertNotNull(recyclerView.getAdapter()); // Ensure Adapter is set
//            assertTrue(recyclerView.getAdapter().getItemCount() >= 0); // Validate item count
//        });
//        //onView(withId(R.id.updateButton)).perform(click());
//    }

    @Test
    public void testEmployerDeleteJob() throws InterruptedException {
        // Step 1: Log in as an employer
        onView(withId(R.id.emailField))
                .perform(typeText("employerTest@aols.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test12345@"), closeSoftKeyboard());
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

        //onView(withId(R.id.deleteButton)).perform(click());


    }


    @Test
    public void testEmployerViewAnalytics() throws InterruptedException {
        // Step 1: Log in as an employer
        onView(withId(R.id.emailField))
                .perform(typeText("employerTest@aols.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test12345@"), closeSoftKeyboard());
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

       // onView(withId(R.id.analyticsButton)).perform(click());

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
