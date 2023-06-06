package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private String uid;
    private Button login;
    private Button signup;

    private TextView title;

    private TextView or;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();

        uid = auth.getUid();

        checkuserLoggedIn();


        login = findViewById(R.id.main_login_btn);
        signup = findViewById(R.id.main_sign_up_btn);
        or = findViewById(R.id.main_or);

        progressBar = findViewById(R.id.main_progress_bar);

        title = findViewById(R.id.main_title);


        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
        });

    }


    public void checkuserLoggedIn() {
        // If this user is already logged in, then check if they have a username
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(uid)) {
                        checkUserName(true);
                        return;
                    }
                }
                checkUserName(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkUserName(boolean hasUserName) {
        // If the user has a username, then go to the list activity
        if (currentUser != null && hasUserName) {
            Intent intent = new Intent(this, List.class);
            intent.putExtra("uid", uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            // If the user is logged in, but has no profile picture or username, then go to the profile picture activity
        } else if (currentUser != null && !hasUserName) {
            Intent intent = new Intent(this, ProfilePicture.class);
            intent.putExtra("uid", auth.getCurrentUser().getUid());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        } else {
            // If the user is not logged in, then show the login and signup buttons
            currentUser = null;
            title.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            signup.setVisibility(View.VISIBLE);
            or.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}



