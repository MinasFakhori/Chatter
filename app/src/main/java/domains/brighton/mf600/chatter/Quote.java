package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quote extends AppCompatActivity {

    private TextView quote;
    private ShapeableImageView imgButton;
    private FirebaseDatabase db;
    private String uid;

    private ProgressBar progressBar;

    private LinearLayout quoteContainer;

    private FirebaseAuth auth;

    private String email;

    private String name;

    private TextView legal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        imgButton = findViewById(R.id.quote_pp);

        progressBar = findViewById(R.id.quote_progress);

        quoteContainer = findViewById(R.id.quote_container);

        auth = FirebaseAuth.getInstance();

        legal = findViewById(R.id.quote_legal);


        db = FirebaseDatabase.getInstance();
        uid = getIntent().getStringExtra("uid");

        try {
            getImg();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        quote = findViewById(R.id.quote);
        getQuote();


        imgButton.setOnClickListener(v -> {
            changeImg();
        });

        legal.setOnClickListener(v -> {
            Intent intent = new Intent(this, Legal.class);
            startActivity(intent);
        });
    }

    public void getQuote() {
        QuoteGetter task = new QuoteGetter(quote);
        task.execute();
        quote.setVisibility(TextView.VISIBLE);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        imgButton.setVisibility(ImageView.VISIBLE);
        quoteContainer.setVisibility(ScrollView.VISIBLE);

    }


    public void getImg() throws Exception {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.getKey().equals(uid)) {
                        // Gets the image by checking the uid, then loads it into the image button
                        //Glide loads gets profile image url, loads it and displays it into the image button
                        Glide.with(getApplicationContext()).load(ds.child("profileImage").getValue().toString()).into(imgButton);
                        email = ds.child("email").getValue().toString();
                        name = ds.child("username").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quote.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeImg() {
        Intent intent = new Intent(this, ProfilePicture.class);
        intent.putExtra("isSignUp", false);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        intent.putExtra("uid", auth.getCurrentUser().getUid());
        startActivity(intent);
        finish();
    }
}

