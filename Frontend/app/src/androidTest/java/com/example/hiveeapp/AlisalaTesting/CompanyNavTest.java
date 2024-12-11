package com.example.hiveeapp.AlisalaTesting;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hiveeapp.R;
import com.example.hiveeapp.company_user.CompanyMainActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CompanyNavTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {
        Intents.init(); // Initialize Intents
    }

    @After
    public void tearDown() {
        Intents.release(); // Release Intents to avoid leaks
    }

    // Helper method to log in before running tests
    private void CompanyperviewLogin(String email, String password) throws InterruptedException {
        onView(ViewMatchers.withId(R.id.emailField))
                .perform(typeText(email), closeSoftKeyboard());

        onView(withId(R.id.passwordField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for navigation to StudentMainActivity
        Thread.sleep(1000);
        intended(hasComponent(CompanyMainActivity.class.getName())); // Verify navigation
    }

    @Test
    public void testBottomNavigation_toProfile() throws InterruptedException {
        // Login before testing navigation
        CompanyperviewLogin("companyTester@aols.com", "Test12345@");

        // Navigate to Profile
        onView(withId(R.id.navigation_invitations)).perform(click());
        //intended(hasComponent(StudentProfileViewActivity.class.getName()));
        //onView(withId(R.id.profileNameView)).check(matches(isDisplayed()));
    }

    @Test
    public void testBottomNavigationToEmployer() throws InterruptedException {
        // Login before testing navigation
        CompanyperviewLogin("companyTester@aols.com", "Test12345@");

        // Navigate to Chat
        onView(withId(R.id.navigation_employers)).perform(click());
      //  intended(hasComponent(ChatListActivity.class.getName()));
       // onView(withId(R.id.chatRecyclerView)).check(matches(isDisplayed()));
    }
}

