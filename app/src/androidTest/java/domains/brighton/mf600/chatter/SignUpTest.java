package domains.brighton.mf600.chatter;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;

public class SignUpTest {
    String email;

    String password;

    String confirmPassword;
    private SignUp signUpActivity;
    private ActivityScenario<SignUp> activityScenario;
    private FirebaseAuth auth;

    @Before
    public void setUp() throws Exception {
        activityScenario = launch(SignUp.class);
        activityScenario.onActivity(activity -> {
            signUpActivity = activity;
        });

        auth = FirebaseAuth.getInstance();


    }

    @Test
    public void TestEmail() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "ggmail.com";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestValidEmail() {
        email = "tony@gmail.com";

        boolean result = signUpActivity.checkInputEmail(email);

        assertTrue(result);
    }

    @Test
    public void TestEmailWithoutAt() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "ggmail.com";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }

    @Test
    public void TestEmailTwoAt() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "gg@@mail.com";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestEmailSpecialChar() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "min%as@gmail.com";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestEmailEmpty() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestEmailWithoutDot() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "minas@gmailcom";
                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestEmailAtStart() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "@minasgmailcom";

                    boolean result = signUpActivity.checkInputEmail(email);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestEmailAtEnd() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    email = "minasgmailcom@";

                    boolean result = signUpActivity.checkInputEmail(email);
                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestValidPassword() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "Password123!";
                    confirmPassword = "Password123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertTrue(result);
                }
        );
    }


    @Test
    public void TestDiffrentPassword() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "Password123!";
                    confirmPassword = "HelloWorld123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordWithoutCaptalLetters() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "password123!";
                    confirmPassword = "password123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordWithSpace() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "pass word123!";
                    confirmPassword = "pass word123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordLessThan8char() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "Pp123!";
                    confirmPassword = "Pp123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordWithNoSpacialChar() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "Password123";
                    confirmPassword = "Password123";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordWithNoLowercase() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "PASSWORD123!";
                    confirmPassword = "PASSWORD123!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }


    @Test
    public void TestPasswordWithNoNum() {
        ActivityScenario<SignUp> scenario = ActivityScenario.launch(SignUp.class);

        scenario.onActivity(activity -> {
                    password = "Password!";
                    confirmPassword = "Password!";

                    boolean result = signUpActivity.checkInputPassword(password, confirmPassword);

                    assertFalse(result);
                }
        );
    }

}