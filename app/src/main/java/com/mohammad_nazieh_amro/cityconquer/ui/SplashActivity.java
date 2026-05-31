package com.mohammad_nazieh_amro.cityconquer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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