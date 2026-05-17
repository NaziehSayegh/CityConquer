package com.androidkings.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.androidkings.cityconquer.R;
import com.androidkings.cityconquer.model.City;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView citiesRecycler;
    private FirebaseFirestore db;
    private List<City> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        citiesRecycler = findViewById(R.id.cities_recycler);
        citiesRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadCities();
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
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.menu_friends) {
            startActivity(new Intent(this, FriendsActivity.class));
            return true;
        } else if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}