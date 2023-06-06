package domains.brighton.mf600.chatter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    private final ArrayList<String> messagesText;
    private final String uid;
    private final ArrayList<Message> messages;

    public MessageAdapter(ArrayList<Message> msg, ArrayList<String> messagesText, String uid) {
        this.messagesText = messagesText;
        this.uid = uid;
        this.messages = msg;
    }

    // onCreateViewHolder() is called when the RecyclerView needs a new ViewHolder.
    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout, and return a new holder instance. Inflating the layout means to render the XML file into a View object.
        View view = View.inflate(parent.getContext(), R.layout.message_holder, null);
        return new MessageHolder(view);
    }

// onBindViewHolder() is called to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = messages.get(position);

        if (message.getSenderId() == null) {
            Toast.makeText(holder.itemView.getContext(), "Sender ID is null", Toast.LENGTH_SHORT).show();
        } else if (message.getSenderId().equals(uid)) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
            holder.right.setVisibility(View.VISIBLE);
            holder.right.setText(message.getMessageText());
        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.right.setVisibility(View.GONE);
            holder.left.setVisibility(View.VISIBLE);
            holder.left.setText(message.getMessageText());
        }

    }

    @Override
    public int getItemCount() {
        return messagesText.size();
    }

    // This inner static class holds the views for each item
    public static class MessageHolder extends RecyclerView.ViewHolder {
        TextView left;
        TextView right;

        LinearLayout leftLayout;

        LinearLayout rightLayout;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);
        }
    }
}
