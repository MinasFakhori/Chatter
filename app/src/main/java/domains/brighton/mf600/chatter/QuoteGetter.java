package domains.brighton.mf600.chatter;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class QuoteGetter extends AsyncTask<Void, Void, String> {
    // WeakReference is used to prevent stopping the garbage collector from collecting the TextView
    private final WeakReference<TextView> textViewRef;

    // The Constructor takes a TextView as a parameter and stores it in a WeakReference
    public QuoteGetter(TextView textView) {
        textViewRef = new WeakReference<>(textView);
    }

    // This method is executed in the background thread, which gets the quote from the API
    @Override
    protected String doInBackground(Void... voids) {
        String quote = "";
        try {
            // The API is called and the quote is stored in a String
            URL url = new URL("https://api.quotable.io/random");
            // This opens the connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // The connection timeout is set to 3 seconds
            connection.setConnectTimeout(3000);
            // input reads the response from the API
            InputStream input = connection.getInputStream();
            // The response is converted to a String, \\A is used to read the entire response
            Scanner s = new Scanner(input).useDelimiter("\\A");
            // Check if the response is empty.
            String result = s.hasNext() ? s.next() : "";
            // The response is converted to a JsonObject and the quote is extracted
            JsonObject obj = JsonParser.parseString(result).getAsJsonObject();
            // The quote is stored in a String
            quote = obj.get("content").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quote;
    }

    // This displays the quote in the TextView, which is passed as a parameter
    @Override
    protected void onPostExecute(String quote) {
        TextView textView = textViewRef.get();
        if (textView != null) {
            textView.setText(quote);
        }
    }
}