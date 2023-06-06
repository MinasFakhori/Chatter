package domains.brighton.mf600.chatter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Legal extends AppCompatActivity {

    private ArrayList<String> legal;
    private ListView legalList;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);

        legal = new ArrayList<>();
        legalList = findViewById(R.id.legal_list);

        legalLaws();
    }


    public void legalLaws() {
        legal.add("This page describes how we collect, use, and share information about you when you use Chatter. By using Chatter, you consent to our collection, use, and sharing of your information as described below.");
        legal.add("When you make an account, we collect your email address, username, and profile picture. We use this information to create your account and to provide you with the Chatter service.");
        legal.add("We do not knowingly collect personal information from children under the age of 16 without the consent of their parent or guardian. If we find out that we have collected personal information from a child under the age of 16 without parental consent, we will delete the information as soon as possible.");
        legal.add("All the information we collect, including the messages are stored on firebase.");
        legal.add("We do not sell your personal information to third parties.");
        legal.add("We may also share your information if we are required to do so by law.");
        legal.add("You have the right to request that we delete your personal information. To do so, please contact us at m.fakhori3@uni.brighton.ac.uk.");
        legal.add("The logo of Chatter is made by OpenAI image generator, DALL-E.");
        legal.add("All the illustrations are made by Vecteezy.");
        legal.add("The quotes are from https://api.quotable.io/random.");

        adapter = new ArrayAdapter<>(this, android.R.layout.preference_category, legal);
        legalList.setAdapter(adapter);
    }
}