package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;

    private Button signInBtn;

    private EditText email;

    private EditText password;

    private TextView errorText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();
        errorText = findViewById(R.id.sign_in_error);
        signInBtn = findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(e -> {
            loginInUser();
        });


        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);
    }

    // When the back button is pressed, the user is taken back to the main activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }


    public void loginInUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString();

        if (userEmail.isEmpty()) {
            email.setError("Email is required");
        } else if (userPassword.isEmpty()) {
            password.setError("Password is required");
        } else {
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(this, List.class);
                            intent.putExtra("uid", auth.getUid());
                            startActivity(intent);
                            finish();
                        } else {
                            errorText.setText(R.string.signinerror);
                            errorText.setPadding(10, 10, 10, 10);
                        }
                    });
        }
    }


    public EditText getEmail() {
        return email;
    }

}