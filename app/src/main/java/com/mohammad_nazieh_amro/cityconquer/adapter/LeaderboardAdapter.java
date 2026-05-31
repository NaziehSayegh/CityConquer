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

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> users;

    public LeaderboardAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.rank.setText("#" + (position + 1));
        holder.username.setText(user.getUsername() != null ? user.getUsername() : "Player");
        holder.xp.setText(user.getTotalXP() + " XP");

        // Gold, Silver, Bronze colors
        if (position == 0) holder.rank.setTextColor(0xFFFFD700);
        else if (position == 1) holder.rank.setTextColor(0xFFC0C0C0);
        else if (position == 2) holder.rank.setTextColor(0xFFCD7F32);
        else holder.rank.setTextColor(0xFFFFFFFF);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateList(List<User> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank, username, xp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.rank_text);
            username = itemView.findViewById(R.id.username_text);
            xp = itemView.findViewById(R.id.xp_text);
        }
    }
}