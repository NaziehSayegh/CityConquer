package com.mohammad_nazieh_amro.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.service.LocationTrackingService;
import com.mohammad_nazieh_amro.cityconquer.ui.fragment.CitiesFragment;
import com.mohammad_nazieh_amro.cityconquer.ui.fragment.FriendsFragment;
import com.mohammad_nazieh_amro.cityconquer.ui.fragment.LeaderboardFragment;
import com.mohammad_nazieh_amro.cityconquer.ui.fragment.MapFragment;
import com.mohammad_nazieh_amro.cityconquer.ui.fragment.ProfileFragment;
import com.mohammad_nazieh_amro.cityconquer.util.DataSeeder;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verify that the user is logged in
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, com.mohammad_nazieh_amro.cityconquer.ui.auth.AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        Log.d("MAIN", "MainActivity started!");
        DataSeeder.seedData();
        Log.d("MAIN", "Seeder called!");

        // Start location tracking service check
        checkLocationPermissionAndStartService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this::onBottomNavItemSelected);

        loadFragment(new LeaderboardFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
    }

    private boolean onBottomNavItemSelected(@NonNull MenuItem item) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_leaderboard) {
            loadFragment(new LeaderboardFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
        } else if (id == R.id.nav_map) {
            loadFragment(new MapFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_map);
        } else if (id == R.id.nav_cities) {
            loadFragment(new CitiesFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_cities);
        } else if (id == R.id.nav_friends) {
            loadFragment(new FriendsFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_friends);
        } else if (id == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void checkLocationPermissionAndStartService() {
        java.util.List<String> permissionsToRequest = new java.util.ArrayList<>();
        
        boolean hasLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        
        boolean hasNotifications = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            hasNotifications = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }

        if (hasLocation && hasNotifications) {
            Intent serviceIntent = new Intent(this, LocationTrackingService.class);
            startForegroundService(serviceIntent);
        } else {
            if (!hasLocation) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (!hasNotifications && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS);
            }
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]), 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            boolean locationGranted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) 
                        && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    locationGranted = true;
                }
            }
            if (locationGranted) {
                Intent serviceIntent = new Intent(this, LocationTrackingService.class);
                startForegroundService(serviceIntent);
            }
        }
    }
}