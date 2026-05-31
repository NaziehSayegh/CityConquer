package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.adapter.CityAdapter;
import com.mohammad_nazieh_amro.cityconquer.model.City;
import java.util.ArrayList;
import java.util.List;

public class CitiesFragment extends Fragment {

    private RecyclerView citiesRecycler;
    private FirebaseFirestore db;
    private List<City> cities = new ArrayList<>();
    private CityAdapter cityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        db = FirebaseFirestore.getInstance();
        citiesRecycler = view.findViewById(R.id.cities_recycler);
        citiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cityAdapter = new CityAdapter(cities);
        citiesRecycler.setAdapter(cityAdapter);
        loadCities();
        return view;
    }

    private void loadCities() {
        db.collection("cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cities.clear();
                    for (var doc : queryDocumentSnapshots) {
                        City city = doc.toObject(City.class);
                        city.setId(doc.getId());
                        cities.add(city);
                    }
                    cityAdapter.updateList(cities);
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        android.widget.Toast.makeText(getContext(), "Failed to load cities: " + e.getMessage(), 
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                });
    }
}