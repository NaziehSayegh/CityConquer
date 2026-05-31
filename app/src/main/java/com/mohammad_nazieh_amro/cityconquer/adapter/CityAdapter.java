package com.mohammad_nazieh_amro.cityconquer.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.model.City;
import com.mohammad_nazieh_amro.cityconquer.ui.CityActivity;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> cities;

    public CityAdapter(List<City> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cities.get(position);
        holder.cityName.setText(city.getName());
        holder.cityCountry.setText(city.getCountry());
        holder.landmarksCount.setText(city.getTotalLandmarks() + " Landmarks");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CityActivity.class);
            intent.putExtra("cityId", city.getId());
            intent.putExtra("cityName", city.getName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void updateList(List<City> newCities) {
        this.cities = newCities;
        notifyDataSetChanged();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, cityCountry, landmarksCount;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            cityCountry = itemView.findViewById(R.id.city_country);
            landmarksCount = itemView.findViewById(R.id.landmarks_count);
        }
    }
}