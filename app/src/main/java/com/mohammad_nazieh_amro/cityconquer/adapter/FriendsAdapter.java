package com.mohammad_nazieh_amro.cityconquer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.model.User;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<User> friends;

    public FriendsAdapter(List<User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friends.get(position);
        String name = friend.getUsername() != null ? friend.getUsername() : "Friend";
        holder.username.setText(name);
        // Show first letter of username as avatar initial
        holder.avatarInitial.setText(name.substring(0, 1).toUpperCase());
        holder.level.setText("Level " + friend.getLevel());
        holder.xp.setText(String.valueOf(friend.getTotalXP()));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void updateList(List<User> newFriends) {
        this.friends = newFriends;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, xp, level, avatarInitial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text);
            xp = itemView.findViewById(R.id.xp_text);
            level = itemView.findViewById(R.id.level_text);
            avatarInitial = itemView.findViewById(R.id.avatar_initial);
        }
    }
}
