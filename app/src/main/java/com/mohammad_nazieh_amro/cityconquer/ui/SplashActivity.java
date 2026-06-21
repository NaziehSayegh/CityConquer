package com.mohammad_nazieh_amro.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.ui.auth.AuthActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the action bar for immersive splash
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);
        TextView tagline = findViewById(R.id.tagline);
        android.widget.LinearLayout authorsContainer = findViewById(R.id.authors_container);

        // Load and start animations
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.splash_text_anim);
        Animation taglineAnim = AnimationUtils.loadAnimation(this, R.anim.splash_tagline_anim);

        logo.startAnimation(logoAnim);
        appName.startAnimation(textAnim);
        tagline.startAnimation(taglineAnim);
        authorsContainer.startAnimation(taglineAnim);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Force reload the user from the Firebase server to check if the session is still active
                currentUser.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && FirebaseAuth.getInstance().getCurrentUser() != null) {
                        // User exists and session is valid on the server!
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        // User does not exist in Firebase console or session was revoked. Sign out local cache.
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    }
                    finish();
                });
            } else {
                startActivity(new Intent(this, AuthActivity.class));
                finish();
            }
        }, 3000);
    }
}