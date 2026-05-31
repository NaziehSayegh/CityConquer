package com.androidkings.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.androidkings.cityconquer.R;
import com.androidkings.cityconquer.ui.fragment.CitiesFragment;
import com.androidkings.cityconquer.ui.fragment.FriendsFragment;
import com.androidkings.cityconquer.ui.fragment.LeaderboardFragment;
import com.androidkings.cityconquer.ui.fragment.MapFragment;
import com.androidkings.cityconquer.ui.fragment.ProfileFragment;
import com.androidkings.cityconquer.util.DataSeeder;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataSeeder.seedData();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onNavItemSelected);

        // Show Leaderboard first
        loadFragment(new LeaderboardFragment());
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_leaderboard) {
            loadFragment(new LeaderboardFragment());
        } else if (id == R.id.nav_map) {
            loadFragment(new MapFragment());
        } else if (id == R.id.nav_cities) {
            loadFragment(new CitiesFragment());
        } else if (id == R.id.nav_friends) {
            loadFragment(new FriendsFragment());
        } else if (id == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}