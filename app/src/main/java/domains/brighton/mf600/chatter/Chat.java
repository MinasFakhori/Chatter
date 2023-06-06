package domains.brighton.mf600.chatter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.text.MessageFormat;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    private EditText theMessage;

    private ImageButton send;

    private String uid;

    private String currentUid;

    private FirebaseDatabase database;

    private ArrayList<String> messages;

    private MessageAdapter messageAdapter;

    private TextView otherUser;

    private RecyclerView recyclerView;

    private LinearLayoutManager llm;

    private ArrayList<Message> message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = findViewById(R.id.chat_name);

        recyclerView = findViewById(R.id.chat_list);

        message = new ArrayList<>();
        messages = new ArrayList<>();
        send = findViewById(R.id.send);
        theMessage = findViewById(R.id.chat_input);
        database = FirebaseDatabase.getInstance();
        uid = getIntent().getStringExtra("uid");
        currentUid = getIntent().getStringExtra("currentUid");

        otherUser.setText(MessageFormat.format("Chat with {0}", getIntent().getStringExtra("username")));

        // compare the two uids to get the chat room id this is to ensure that the chat room id is the same for both users
        int compare = uid.compareTo(currentUid);

        // if the uid is greater than the current uid then the chat room id is the uid + current uid
        String chatRoomId = compare > 0 ? uid + currentUid : currentUid + uid;
        getMessages(chatRoomId);

        send.setOnClickListener(v -> {
            sendMessage();
        });

        theMessage.setOnClickListener(v -> {
            sendMessage();
        });

        // set the layout manager to the recycler view
        llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);

        recyclerView.setLayoutManager(llm);
    }


    private void sendMessage() {
        if (!isInternetConnected()) {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            finish();
        }

        String getMessage = theMessage.getText().toString().trim();
        if (getMessage.isEmpty()) {
            return;
        }
        int compare = uid.compareTo(currentUid);
        String chatRoomId = compare > 0 ? uid + currentUid : currentUid + uid;

        // get the reference to the messages node in the database and push the message to the database
        DatabaseReference messagesRef = database.getReference("messages/" + chatRoomId);
        Message message = new Message(currentUid, uid, getMessage);
        messagesRef.push().setValue(message);
        // clear the message input
        theMessage.setText("");
    }


    public boolean isInternetConnected() {
        // check if the device is connected to the internet return true if connected and false if not connected
        ConnectivityManager network = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return network.getActiveNetworkInfo() != null;
    }


    public void getMessages(String chatRoomId) {
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                message.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message message = ds.getValue(Message.class);
                    assert message != null;
                    messages.add(message.getMessageText());
                    Chat.this.message.add(message);
                }
                messageAdapter = new MessageAdapter(message, messages, currentUid);

                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}