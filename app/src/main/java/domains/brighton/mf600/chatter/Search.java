package domains.brighton.mf600.chatter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private EditText search;

    private RecyclerView listView;

    private String currentUid;


    private ArrayList<User> arr;

    private String searchText;


    private UserAdapter userAdapter;


    private ArrayList<String> usersUid;


    private ProgressBar progressBar;


    private TextView noUserText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.list_search);
        listView = findViewById(R.id.search_list);
        progressBar = findViewById(R.id.search_progress_bar);
        noUserText = findViewById(R.id.search_no_user);
        arr = new ArrayList<>();
        usersUid = new ArrayList<>();

        currentUid = getIntent().getStringExtra("uid");

        search.setOnClickListener(v -> {
            searchText = search.getText().toString().trim();
            getName(searchText);
        });


    }

    public void getName(String username) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        // Clears the old array, so anything the user searched before.
        arr.clear();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.addValueEventListener(new ValueEventListener() {
            // Refreshes the list every time the database changes.
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Display every user except the current user.
                    if (!ds.getKey().equals(currentUid)) {
                        if (ds.child("username").getValue().toString().startsWith(username)) {
                            arr.add(ds.getValue(User.class));
                            usersUid.add(ds.getKey());
                        }
                    }
                }

                if (arr.size() == 0) {
                    noUserText.setVisibility(TextView.VISIBLE);
                    progressBar.setVisibility(ProgressBar.GONE);
                    listView.setVisibility(RecyclerView.GONE);
                } else {
                    userAdapter = new UserAdapter(arr, Search.this);
                    userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                        @Override
                        // When the user clicks on a user, it will open a chat with that user.
                        public void onItemClick(int position) {
                            Intent intent = new Intent(getApplicationContext(), Chat.class);
                            intent.putExtra("uid", usersUid.get(position));
                            intent.putExtra("currentUid", currentUid);
                            intent.putExtra("username", arr.get(position).getUsername());
                            startActivity(intent);
                        }
                    });


                    listView.setLayoutManager(new LinearLayoutManager(Search.this));
                    listView.setAdapter(userAdapter);
                    progressBar.setVisibility(ProgressBar.GONE);
                    noUserText.setVisibility(TextView.GONE);
                    listView.setVisibility(RecyclerView.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Search.this, "Database error", Toast.LENGTH_SHORT).show();
            }

        });


    }


}



