package com.example.hiveeapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hiveeapp.employer_user.EmployerMainActivity;
import com.example.hiveeapp.employer_user.chat.EmployerChatActivity;
import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.search.JobSearchActivity;
import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;


//import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;

import static org.junit.Assert.assertEquals;
import static java.util.EnumSet.allOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hiveeapp.registration.login.LoginActivity;
import com.example.hiveeapp.student_user.StudentMainActivity;
import com.example.hiveeapp.student_user.chat.ChatListActivity;
import com.example.hiveeapp.student_user.profile.StudentProfileViewActivity;
import com.example.hiveeapp.student_user.search.JobSearchActivity;
import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EmployerNaviTochat {

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
    private void performLogin(String email, String password) throws InterruptedException {
        onView(withId(R.id.emailField))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for navigation to StudentMainActivity
        Thread.sleep(1000);
        intended(hasComponent(EmployerMainActivity.class.getName())); // Verify navigation
    }

//    @Test
//    public void testBottomNavigation_toProfile() throws InterruptedException {
//        // Login before testing navigation
//        performLogin("employerTest@aols.com", "Test12345@");
//
//        // Navigate to Profile
//        onView(withId(R.id.navigation_main_user_page)).perform(click());
//       // intended(hasComponent(StudentProfileViewActivity.class.getName()));
//        intended(hasComponent(EmployerMainActivity.class.getName()));
//        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
//    }

    @Test
    public void testEmployerNavigation_toChat() throws InterruptedException {
        // Login before testing navigation
        performLogin("employerTest@aols.com", "Test12345@");

        // Navigate to Chat

        onView(withId(R.id.nav_chat)).perform(click());
     //  intended(hasComponent(EmployerChatActivity.class.getName()));
        onView(withId(R.id.chatRecyclerView)).check(matches(isDisplayed()));
    }
//    @Test
//    public void testBottomNavigation_toSearch() throws InterruptedException {
//        // Login before testing navigation
//        performLogin("teststudent1@example.com", "TestStudent1234@");
//
//        // Verify that the bottom navigation view is displayed
//        onView(withId(R.id.bottomNavigationView))
//                .check(matches(isDisplayed()));
//
//        // Click on the "Search" navigation item
//        onView(withId(R.id.navigation_search)).perform(click());
//
//        // Verify that the intended activity is displayed (without checking extras)
//        intended(hasComponent(JobSearchActivity.class.getName()));
//
//        // Verify the presence of UI elements in JobSearchActivity
//        onView(withId(R.id.keywordInput)).check(matches(isDisplayed()));
//
//        // Verify that the studentId exists in SharedPreferences (or use another method to confirm the value)
//        SharedPreferences preferences = ApplicationProvider.getApplicationContext()
//                .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
//        int storedStudentId = preferences.getInt("userId", -1);
//
//        assertEquals(1017, storedStudentId);
//


}

