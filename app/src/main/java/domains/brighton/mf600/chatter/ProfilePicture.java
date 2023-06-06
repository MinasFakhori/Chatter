package domains.brighton.mf600.chatter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class ProfilePicture extends AppCompatActivity {
    private Button uploadButton;
    private Button skipButton;
    private ShapeableImageView imgButton;

    private FirebaseDatabase db;

    private String uid;

    private Uri ppUri;

    private boolean isSignUp;
    private String email;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        uploadButton = findViewById(R.id.profile_upload_btn);
        skipButton = findViewById(R.id.profile_skip_btn);
        imgButton = findViewById(R.id.profile_image);

        progressBar = findViewById(R.id.uploading);

        db = FirebaseDatabase.getInstance();

        uid = getIntent().getStringExtra("uid");

        email = getIntent().getStringExtra("email");


        imgButton.setOnClickListener(v -> {
            // This opens the gallery
            Intent intent = new Intent(Intent.ACTION_PICK);
            // The thing we want to pick is an image format
            intent.setType("image/*");

            // This flag makes sure that the activity is not created again
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            // This is the result we get back from the gallery
            arl.launch(intent);
        });

        uploadButton.setOnClickListener(v -> {
            uploadPP();
        });


        isSignUp = getIntent().getBooleanExtra("isSignUp", true);

        if (!isSignUp) {
            skipButton.setVisibility(Button.GONE);
        }


        skipButton.setOnClickListener(v -> {
            // If the user doesn't want to upload a profile picture, we give them a default one
            newActivity("https://firebasestorage.googleapis.com/v0/b/newchatter-5854b.appspot.com/o/images%2Fnone.png?alt=media&token=5f68884c-99d8-4a17-9aa7-ea15bf23a6fc");
        });
    }


    private void uploadPP() {
        if (ppUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(ProgressBar.VISIBLE);
        uploadButton.setVisibility(Button.GONE);
        skipButton.setVisibility(Button.GONE);
        imgButton.setVisibility(ImageButton.GONE);
       // This uploads the image to Firebase Storage
        FirebaseStorage.getInstance().getReference("images/" + uid).putFile(ppUri).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                // When the task is successful and completed, we get the download url of the image
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(e -> {
                    if (e.isSuccessful()) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        if (isSignUp) {
                            newActivity(e.getResult().toString());
                        } else {
                            // If the user is not signing up, we update their profile picture, by overwriting the old one user with the same uid
                            String name = getIntent().getStringExtra("name");
                            FirebaseDatabase.getInstance().getReference("user/" + uid)
                                    .setValue(new User(name, email, e.getResult().toString()));
                            finish();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ActivityResultLauncher gets a result from activities, <Intent> is the type of data we want to get back
    ActivityResultLauncher<Intent> arl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // If the result is OK and the data is not null, display the image
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    // Get the selected image URI from the intent data
                    ppUri = data.getData();

                    imgButton.setImageURI(ppUri);
                }else{
                    Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            });

    // This is called when the user selects an image from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the request code is 1, the result is OK and the data is not null, we get the image URI
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ppUri = data.getData();
        }
    }


    public void newActivity(String downloadUrl) {
        Intent intent = new Intent(getApplicationContext(), Username.class);
        intent.putExtra("uid", uid);
        intent.putExtra("email", email);
        intent.putExtra("imageUri", downloadUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

}
