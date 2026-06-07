package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private EditText searchCitiesInput;
    private FirebaseFirestore db;
    private List<City> cities = new ArrayList<>();
    private List<City> allCities = new ArrayList<>();
    private CityAdapter cityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        db = FirebaseFirestore.getInstance();
        
        searchCitiesInput = view.findViewById(R.id.search_cities);
        searchCitiesInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCities(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        citiesRecycler = view.findViewById(R.id.cities_recycler);
        citiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cityAdapter = new CityAdapter(cities);
        citiesRecycler.setAdapter(cityAdapter);
        loadCities();
        return view;
    }

    private void filterCities(String query) {
        String cleanQuery = query.toLowerCase().trim();
        List<City> filtered = new ArrayList<>();
        for (City city : allCities) {
            String name = city.getName() != null ? city.getName().toLowerCase() : "";
            String country = city.getCountry() != null ? city.getCountry().toLowerCase() : "";
            if (name.contains(cleanQuery) || country.contains(cleanQuery)) {
                filtered.add(city);
            }
        }
        cities = filtered;
        cityAdapter.updateList(filtered);
    }

    private void loadCities() {
        db.collection("cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allCities.clear();
                    cities.clear();
                    for (var doc : queryDocumentSnapshots) {
                        City city = doc.toObject(City.class);
                        city.setId(doc.getId());
                        allCities.add(city);
                        cities.add(city);
                    }
                    if (searchCitiesInput != null) {
                        filterCities(searchCitiesInput.getText().toString());
                    } else {
                        cityAdapter.updateList(cities);
                    }
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        android.widget.Toast.makeText(getContext(), "Failed to load cities: " + e.getMessage(), 
                                android.widget.Toast.LENGTH_LONG).show();
                    }
                });
    }
}