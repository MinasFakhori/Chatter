package domains.brighton.mf600.chatter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class List extends AppCompatActivity {

    private ArrayList<User> arr;

    private ArrayList<String> uidArr;

    private RecyclerView conversationList;

    private String currentUid;

    private TextView helloUsername;

    private ProgressBar progressBar;

    private ImageButton logoutButton;

    private ImageButton searchButton;

    private ImageView pp;

    private String imageUri;

    private UserAdapter userAdapter;

    private TextView noUsersText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        noUsersText = findViewById(R.id.list_no_chats);

        arr = new ArrayList<>();

        uidArr = new ArrayList<>();

        currentUid = getIntent().getStringExtra("uid");

        pp = findViewById(R.id.list_pp);

        imageUri = getIntent().getStringExtra("imageUri");

        helloUsername = findViewById(R.id.list_hello_text);

        progressBar = findViewById(R.id.list_progress_bar);

        searchButton = findViewById(R.id.list_search_btn);

        conversationList = findViewById(R.id.list_recycler_view);

        logoutButton = findViewById(R.id.list_logout_button);

        logoutButton.setOnClickListener(v -> {
            dialog();
        });

        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Search.class);
            intent.putExtra("uid", currentUid);
            startActivity(intent);
        });

        listUsers();

        pp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Quote.class);
            intent.putExtra("uid", currentUid);
            startActivity(intent);
        });

    }

    private void dialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(List.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("no", null)
                .show();
    }

    public void listUsers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // if the user is the current user, display their username and profile image
                    if (ds.getKey().equals(currentUid)) {
                        helloUsername.setText(String.format("Hello, %s", ds.child("username").getValue().toString()));
                        Glide.with(getApplicationContext()).load(ds.child("profileImage").getValue().toString()).into(pp);
                    }

                    String otherUID = ds.getKey();
                    int compare = otherUID.compareTo(currentUid);
                    String chatRoomId = compare > 0 ? otherUID + currentUid : currentUid + otherUID;
                    DatabaseReference mess = FirebaseDatabase.getInstance().getReference().child("messages");

                    mess.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dsChat : snapshot.getChildren()) {
                                if (dsChat.getKey().equals(chatRoomId)) {
                                    if (!uidArr.contains(ds.getKey())) {
                                        arr.add(ds.getValue(User.class));
                                        uidArr.add(ds.getKey());
                                    }
                                }
                            }

                            checkIfnoUsers();
                            userAdapter = new UserAdapter(arr, List.this);
                            userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                                    intent.putExtra("uid", uidArr.get(position));
                                    intent.putExtra("currentUid", currentUid);
                                    intent.putExtra("username", arr.get(position).getUsername());
                                    startActivity(intent);
                                }
                            });
                            displayRecentUsers();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(List.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(List.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkIfnoUsers() {
        if (arr.size() == 0) {
            noUsersText.setVisibility(TextView.VISIBLE);
        } else {
            noUsersText.setVisibility(TextView.GONE);
        }
    }


    public void displayRecentUsers() {
        // display the users in a recycler view, with the most recent at the top
        LinearLayoutManager layoutManager = new LinearLayoutManager(List.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        conversationList.setLayoutManager(layoutManager);
        conversationList.setAdapter(userAdapter);

        helloUsername.setVisibility(TextView.VISIBLE);
        conversationList.setVisibility(ListView.VISIBLE);
        progressBar.setVisibility(ProgressBar.GONE);
    }

}