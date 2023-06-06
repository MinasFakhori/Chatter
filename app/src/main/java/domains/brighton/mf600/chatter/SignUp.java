package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email;
    private EditText password;
    private EditText confirmPass;
    private Button signUp;
    private TextView errorText;

    private EditText ageDay;
    private EditText ageMonth;
    private EditText ageYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        confirmPass = findViewById(R.id.confirm_password);
        errorText = findViewById(R.id.sign_up_error);
        ageDay = findViewById(R.id.sign_up_age_day);
        ageMonth = findViewById(R.id.sign_up_age_month);
        ageYear = findViewById(R.id.sign_up_age_year);

        auth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.sign_up_btn);

        signUp.setOnClickListener(e -> {
            makeAccount();
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isOlderThan16() {
        String strDay = ageDay.getText().toString();
        String strMonth = ageMonth.getText().toString();
        String strYear = ageYear.getText().toString();

        int day;
        int month;
        int year;
        int dayInMonth;

        if (strDay.length() == 0) {
            ageDay.setError("Cannot be empty");
            ageDay.requestFocus();
            return false;
        } else if (strMonth.length() == 0) {
            ageMonth.setError("Cannot be empty");
            ageMonth.requestFocus();
            return false;
        } else if (strYear.length() < 4) {
            ageYear.setError("Must be a valid year");
            ageYear.requestFocus();
            return false;
        }

        try {
            month = Integer.parseInt(strMonth);

            if (month > 12 || month < 1) {
                ageMonth.setError("Must be a valid date");
                ageMonth.requestFocus();
                return false;
            } else {
                // Get the number of days in the month
                Calendar calendar = Calendar.getInstance();
                // month - 1 because the calendar class starts at 0 for January
                calendar.set(Calendar.MONTH, month - 1);
                dayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        } catch (Exception e) {
            ageMonth.setError("Must be a valid date");
            ageMonth.requestFocus();
            return false;
        }

        try {
            day = Integer.parseInt(strDay);

            if (day >= dayInMonth + 1 || day < 1) {
                ageDay.setError("Must be a valid date");
                ageDay.requestFocus();
                return false;
            } else if (month == 2 && day >= 30) {
                ageDay.setError("Must be a valid date");
                ageDay.requestFocus();
                return false;
            }

        } catch (Exception e) {
            ageDay.setError("Must be a valid date");
            ageDay.requestFocus();
            return false;
        }

        try {
            year = Integer.parseInt(strYear);
            String newYearStr = year + "";

            if (year < 1 || newYearStr.length() != 4) {
                ageYear.setError("Must be a valid year");
                ageYear.requestFocus();
                return false;
            }

        } catch (Exception e) {
            ageYear.setError("Must be a valid year");
            ageYear.requestFocus();
            return false;
        }


        Calendar currentDate = Calendar.getInstance(); // Get the current date
        int currentYear = currentDate.get(Calendar.YEAR); // Gets the current year
        int currentMonth = currentDate.get(Calendar.MONTH); // Gets the current month
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH); // Gets the current day

        int userAge = currentYear - year;
        // If the user's birthday has not happened yet, subtract 1 from their age
        if (currentMonth < month || (currentMonth == month && currentDay < day)) {
            userAge--;
        }


        if (userAge < 16) {
            ageYear.setError("Must be older than 16 to make an account");
            ageYear.requestFocus();
            return false;
        }

        return true;
    }


    public void makeAccount() {
        if (!isOlderThan16()) {
            return;
        }
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfPassword = confirmPass.getText().toString();

        if (checkInputEmail(userEmail) && checkInputPassword(userPassword, userConfPassword)) {
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            Intent intent = new Intent(this, ProfilePicture.class);
                            intent.putExtra("email", userEmail);
                            intent.putExtra("isSignUp", true);
                            intent.putExtra("uid", auth.getCurrentUser().getUid());
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            finish();
                        } else {
                            errorText.setText(R.string.signup_error);
                            errorText.setTextColor(getResources().getColor(R.color.red, null));
                            errorText.setPadding(10, 10, 10, 10);
                        }
                    });
        }
    }

    public boolean checkNumOfAt(String input) {
        int count = 0;
        // convert input string to char array
        char[] in = input.toCharArray();
        for (char c : in) {
            // if the character is '@', increment the count by 1
            if (c == '@') {
                count += 1;
            }
        }
        // if count is greater than 1, return true, else return false
        return count > 1;
    }


    public boolean checkInputEmail(String inputEmail) {
        // indexOfAt is the index of the first '@' in the email, to see if it is valid, if the email does not contain '@', indexOfAt will be 0
        int indexOfAt = inputEmail.contains("@") ? inputEmail.indexOf('@') : 0;
        // stringBeforeAt is the string before the '@' in the email
        String stringBeforeAt = inputEmail.substring(0, indexOfAt);
        //special characters that are allowed in the email, apart from '@', '.', and '-'
        Pattern speacialPattern = Pattern.compile("[^a-zA-Z0-9@_.-]", Pattern.CASE_INSENSITIVE);
        // letters that are allowed in the email, regardless of case
        Pattern letters = Pattern.compile("[a-zA-Z0-9]", Pattern.CASE_INSENSITIVE);
        // matchers to check if the email contains special characters
        Matcher specialMatcher = speacialPattern.matcher(inputEmail);
        // matchers to check if the email contains letters before the '@'
        Matcher letterMatcher = letters.matcher(stringBeforeAt);
        // check if the email contains more than one '@'
        boolean isManyAt = checkNumOfAt(inputEmail);

        if (inputEmail.isEmpty()) {
            email.setError("Email cannot be empty");
            email.requestFocus();
            return false;
        } else if (!inputEmail.contains("@")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (isManyAt) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (!inputEmail.contains(".")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (inputEmail.contains(" ")) {
            email.setError("Email cannot contain spaces");
            email.requestFocus();
            return false;
        } else if (inputEmail.startsWith("@")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (inputEmail.endsWith("@")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (inputEmail.endsWith(".")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (inputEmail.startsWith(".")) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (specialMatcher.find()) {
            email.setError("Must be a valid email");
            email.requestFocus();
            return false;
        } else if (!letterMatcher.find()) {
            email.setError("Must be a valid email");
            email.requestFocus();
        }
        return true;
    }

    public boolean checkInputPassword(String inputPassword, String inputConfirmPassword) {
        // Everything that is not a letter or a number regradless of case
        Pattern speacialPattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        // Everything that is an uppercase
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        // Everything that is a lowercase
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        // Everything that is a digit
        Pattern digitCasePattern = Pattern.compile("[0-9]");

        Matcher specialMatcher = speacialPattern.matcher(inputPassword);
        Matcher upperCaseMatcher = upperCasePattern.matcher(inputPassword);
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(inputPassword);
        Matcher digitCaseMatcher = digitCasePattern.matcher(inputPassword);


        if (inputPassword.isEmpty()) {
            password.setError("Password cannot be empty");
            password.requestFocus();
            return false;
        } else if (inputPassword.length() < 8) {
            password.setError("Password must be at least 8 characters long");
            password.requestFocus();
            return false;
        } else if (!specialMatcher.find()) {
            password.setError("Password must contain at least one special character");
            password.requestFocus();
            return false;
        } else if (!upperCaseMatcher.find()) {
            password.setError("Password must contain at least one upper case character");
            password.requestFocus();
            return false;
        } else if (!lowerCaseMatcher.find()) {
            password.setError("Password must contain at least one lower case character");
            password.requestFocus();
            return false;
        } else if (!digitCaseMatcher.find()) {
            password.setError("Password must contain at least one digit");
            password.requestFocus();
            return false;
        } else if (!inputPassword.equals(inputConfirmPassword)) {
            password.setError("Passwords must match");
            password.requestFocus();
            confirmPass.setError("Passwords must match");
            return false;
        } else if (inputPassword.contains(" ")) {
            password.setError("Password cannot contain spaces");
            password.requestFocus();
            return false;
        }
        return true;
    }

}