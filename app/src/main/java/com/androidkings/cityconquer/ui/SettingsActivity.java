package com.androidkings.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.androidkings.cityconquer.R;
import com.androidkings.cityconquer.ui.auth.AuthActivity;


public class SettingsActivity extends AppCompatActivity {

    private Switch notificationsSwitch, darkModeSwitch;
    private SeekBar radiusSeekBar;
    private TextView radiusText;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationsSwitch = findViewById(R.id.notifications_switch);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        radiusSeekBar = findViewById(R.id.radius_seekbar);
        radiusText = findViewById(R.id.radius_text);
        logoutBtn = findViewById(R.id.logout_btn);

        radiusSeekBar.setMax(200);
        radiusSeekBar.setProgress(100);
        radiusText.setText("Detection Radius: 100m");

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusText.setText("Detection Radius: " + progress + "m");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }
}