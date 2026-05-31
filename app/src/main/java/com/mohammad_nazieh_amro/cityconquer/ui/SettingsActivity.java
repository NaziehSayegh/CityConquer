package com.mohammad_nazieh_amro.cityconquer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.ui.auth.AuthActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch cityAlertsSwitch, landmarkAlertsSwitch;
    private SeekBar radiusSeekBar;
    private TextView radiusText;
    private Button logoutBtn;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("CityConquerPrefs", MODE_PRIVATE);

        cityAlertsSwitch = findViewById(R.id.notifications_switch); // Represents City entry alerts
        landmarkAlertsSwitch = findViewById(R.id.dark_mode_switch); // Represents Nearby landmark alerts in layout XML
        radiusSeekBar = findViewById(R.id.radius_seekbar);
        radiusText = findViewById(R.id.radius_text);
        logoutBtn = findViewById(R.id.logout_btn);

        // Load saved settings
        boolean cityAlerts = prefs.getBoolean("city_alerts", true);
        boolean landmarkAlerts = prefs.getBoolean("landmark_alerts", true);
        int captureRadius = prefs.getInt("capture_radius", 100); // Default to 100m

        cityAlertsSwitch.setChecked(cityAlerts);
        landmarkAlertsSwitch.setChecked(landmarkAlerts);

        // Match seekbar properties in activity_settings.xml: max is 475 (min is 25m, max is 500m)
        radiusSeekBar.setMax(475);
        radiusSeekBar.setProgress(captureRadius - 25);
        radiusText.setText(captureRadius + "m");

        // Save switch states immediately on toggle
        cityAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("city_alerts", isChecked).apply();
        });

        landmarkAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("landmark_alerts", isChecked).apply();
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int radius = progress + 25;
                radiusText.setText(radius + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int radius = seekBar.getProgress() + 25;
                prefs.edit().putInt("capture_radius", radius).apply();
            }
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, AuthActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}