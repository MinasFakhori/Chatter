package domains.brighton.mf600.chatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private OnItemClickListener listener; // Add a listener field
    private final ArrayList<User> users;
    private final Context context;


    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        // Set the listener, so we can build the recycler view
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout, and return a new holder instance.
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_holder, parent, false);
        return new UserHolder(view);
    }

    // onBindViewHolder() is called to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        // this sets the username
        holder.username.setText(users.get(holder.getAdapterPosition()).getUsername());
        // this sets the profile picture using Glide libary
        Glide.with(context).load(users.get(holder.getAdapterPosition()).getProfileImage()).error(R.drawable.baseline_account_circle_24).placeholder(R.drawable.loading).into(holder.pp);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // This inner static class holds the views for each item
    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView pp;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.other_profile_name);
            pp = itemView.findViewById(R.id.other_profile_image);
        }
    }
}
