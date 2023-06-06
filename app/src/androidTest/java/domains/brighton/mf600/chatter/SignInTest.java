package domains.brighton.mf600.chatter;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ActivityScenario;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

public class SignInTest {
    private SignIn signInActivity;

    private ActivityScenario<SignIn> activityScenario;


    @Before
    public void setUp() {
        activityScenario = launch(SignIn.class);
        activityScenario.onActivity(activity -> {
            signInActivity = activity;
        });
    }

    @Test
    public void testPreconditions() {
        ActivityScenario<SignIn> scenario = ActivityScenario.launch(SignIn.class);

        scenario.onActivity(activity -> {
            assertNotNull(signInActivity);
            assertNotNull(signInActivity.getEmail());
            assertNotNull(signInActivity.findViewById(R.id.sign_in_btn));
            assertNotNull(signInActivity.findViewById(R.id.sign_in_email));
            assertNotNull(signInActivity.findViewById(R.id.sign_in_password));
            assertNotNull(signInActivity.findViewById(R.id.sign_in_error));
        });
    }

    @Test
    public void testSignInWithValidCredentials() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String validEmail = ""; // Enter valid email
        String validPassword = ""; // Enter valid password
        onView(withId(R.id.sign_in_email))
                .perform(scrollTo(),typeText(validEmail));
        onView(withId(R.id.sign_in_password))
                .perform(scrollTo(), typeText(validPassword));

        onView(withId(R.id.sign_in_btn))
                .perform(scrollTo(), click());

    }


    @Test
    public void testSignInWithInvalidCredentials() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        String invalidEmail = ""; // Enter invalid email
        String invalidPassword = ""; // Enter invalid password
        onView(withId(R.id.sign_in_email))
                .perform(scrollTo(),typeText(invalidEmail));
        onView(withId(R.id.sign_in_password))
                .perform(scrollTo(),typeText(invalidPassword));

        onView(withId(R.id.sign_in_btn))
                .perform(scrollTo(), click());

        // Wait for 3 seconds for all the processes to finish
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.sign_in_error))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

}