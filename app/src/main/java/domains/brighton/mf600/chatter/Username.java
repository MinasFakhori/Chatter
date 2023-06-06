package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Username extends AppCompatActivity {
    private EditText username;
    private Button usernameBtn;
    private String email;

    private String uid;

    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        imageUri = getIntent().getStringExtra("imageUri");

        uid = getIntent().getStringExtra("uid");

        email = getIntent().getStringExtra("email");
        username = findViewById(R.id.username_input);
        usernameBtn = findViewById(R.id.username_btn);

        usernameBtn.setOnClickListener(e -> {
            isUsernameTaken();
        });
    }


    private void isUsernameTaken() {
        ArrayList<String> usernamesIsTaken = new ArrayList<>();
        String usernameText = username.getText().toString().trim();

        if (usernameText.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        } else if (usernameText.length() > 12) {
            username.setError("Username must be less than 12 characters");
            username.requestFocus();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child("username").getValue() != null) {
                        // Add all usernames to an arraylist
                        usernamesIsTaken.add(ds.child("username").getValue().toString());
                    }
                }

                // If the username is not taken, add the user to the database and go to the list activity
                if (!usernamesIsTaken.contains(usernameText)) {
                    addUser();
                    Intent intent = new Intent(Username.this, List.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("username", usernameText);
                    intent.putExtra("email", email);
                    intent.putExtra("imageUri", imageUri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    finish();
                } else {
                    // If the username is taken, show an error
                    username.setError("Username is taken");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Username.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

    // Add the user to the database, using the uid as the key.
    public void addUser() {
        FirebaseDatabase.getInstance().getReference("user/" + uid)
                // Set the value of the user to a new User object
                .setValue(new User(username.getText().toString(), email, imageUri));
    }

}