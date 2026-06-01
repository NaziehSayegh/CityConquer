package com.mohammad_nazieh_amro.cityconquer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.model.Landmark;
import com.mohammad_nazieh_amro.cityconquer.ui.LandmarkActivity;
import java.util.List;

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.ViewHolder> {

    private List<Landmark> landmarks;
    private String cityId;

    public LandmarkAdapter(List<Landmark> landmarks, String cityId) {
        this.landmarks = landmarks;
        this.cityId = cityId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_landmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Landmark landmark = landmarks.get(position);
        holder.name.setText(landmark.getName());
        holder.description.setText(landmark.getDescription());
        // XP badge only shows the number — "XP" label is in the layout XML
        holder.xp.setText("+" + landmark.getXp());

        if (landmark.isConquered()) {
            holder.statusIcon.setText("✅");
            holder.status.setText("Conquered  ·  " + landmark.getXp() + " XP earned");
            holder.status.setTextColor(0xFF00BCD4);
            // Green accent bar for conquered
            holder.statusBar.setBackgroundColor(0xFF00BCD4);
        } else {
            holder.statusIcon.setText("🔒");
            holder.status.setText("Tap to explore and conquer");
            holder.status.setTextColor(0xFF666666);
            // Grey bar for locked
            holder.statusBar.setBackgroundColor(0xFF333344);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), LandmarkActivity.class);
            intent.putExtra("landmarkId", landmark.getId());
            intent.putExtra("cityId", cityId);
            intent.putExtra("name", landmark.getName());
            intent.putExtra("description", landmark.getDescription());
            intent.putExtra("latitude", landmark.getLatitude());
            intent.putExtra("longitude", landmark.getLongitude());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return landmarks.size();
    }

    public void updateList(List<Landmark> newLandmarks) {
        this.landmarks = newLandmarks;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, xp, status, statusIcon;
        View statusBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.landmark_name);
            description = itemView.findViewById(R.id.landmark_description);
            xp = itemView.findViewById(R.id.landmark_xp);
            status = itemView.findViewById(R.id.landmark_status);
            statusIcon = itemView.findViewById(R.id.landmark_status_icon);
            statusBar = itemView.findViewById(R.id.status_bar);
        }
    }
}