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
    public void testSuccessfulLoginAndNavigation() throws InterruptedException {
        // Preenche email e senha
        onView(withId(R.id.emailField))
                .perform(typeText("test644@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Aguarda a navegação para a próxima tela
        Thread.sleep(5000); // Use IdlingResource se possível

        // Verifica se a navegação para StudentMainActivity ocorreu
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Valida os dados no SharedPreferences
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString("email", null);
        String savedPassword = sharedPreferences.getString("password", null);

        System.out.println("Email salvo: " + savedEmail);
        System.out.println("Senha salva: " + savedPassword);

        assertEquals("test644@example.com", savedEmail);
        assertEquals("Test$1234", savedPassword);
    }

    @Test
    public void testSignUpAndLogin() {
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";

        onView(withId(R.id.registerText)).perform(click());
        onView(withId(R.id.signup_student_btn)).perform(click());

        onView(withId(R.id.signup_name_edt))
                .check(matches(isDisplayed()))
                .perform(scrollTo(), typeText("New Test User"));
        onView(withId(R.id.signup_email_edt))
                .perform(scrollTo(), typeText(uniqueEmail));
        onView(withId(R.id.signup_password_edt))
                .perform(scrollTo(), typeText("Test@1234"));
        onView(withId(R.id.signup_verify_password_edt))
                .perform(scrollTo(), typeText("Test@1234"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_university_edt))
                .perform(scrollTo(), typeText("Test University"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.signup_signup_btn))
                .perform(scrollTo(), click());

        // Verifica se o campo de e-mail está visível após o registro
        onView(withId(R.id.emailField)).check(matches(isDisplayed()));

        // Faz login com as credenciais criadas
        onView(withId(R.id.emailField))
                .perform(typeText(uniqueEmail), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test@1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Verifica a navegação para StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Valida dados salvos no SharedPreferences
        SharedPreferences sharedPreferences =
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext()
                        .getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        assertEquals(uniqueEmail, sharedPreferences.getString("email", null));
        assertEquals("Test@1234", sharedPreferences.getString("password", null));
    }

    @Test
    public void testLoginNavigateAndUpdateProfile() {
        onView(withId(R.id.emailField))
                .perform(typeText("test643@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Verifica a navegação para StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Navega até o perfil
        onView(withId(R.id.navigation_profile)).perform(click());

        // Verifica se a view do perfil está visível
        onView(withId(R.id.profileScrollView)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginAndNavigateToChat() {
        onView(withId(R.id.emailField))
                .perform(typeText("test643@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordField))
                .perform(typeText("Test$1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // Verifica a navegação para StudentMainActivity
        intended(hasComponent(StudentMainActivity.class.getName()));

        // Navega para o chat
        onView(withId(R.id.navigation_chat)).perform(click());

        // Verifica se a RecyclerView do chat está visível
        onView(withId(R.id.chatRecyclerView))
                .check(matches(isDisplayed()));
    }
}
