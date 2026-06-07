package com.mohammad_nazieh_amro.cityconquer.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, emailText, levelText, xpText;
    private TextView rankBadge, citiesCountText, landmarksCountText, friendsCountText;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String extraUserId = getIntent().getStringExtra("userId");
        final String targetUserId = (extraUserId != null) ? extraUserId : currentUserId;
        final boolean isOwnProfile = targetUserId.equals(currentUserId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isOwnProfile ? "My Profile" : "Friend's Profile");
        }

        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        levelText = findViewById(R.id.level_text);
        xpText = findViewById(R.id.xp_text);
        
        rankBadge = findViewById(R.id.rank_badge);
        citiesCountText = findViewById(R.id.cities_count_text);
        landmarksCountText = findViewById(R.id.landmarks_count_text);
        friendsCountText = findViewById(R.id.friends_count_text);

        loadProfile(targetUserId, isOwnProfile);
    }

    private void loadProfile(String targetUserId, boolean isOwnProfile) {
        db.collection("users").document(targetUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        Long levelObj = documentSnapshot.getLong("level");
                        Long xpObj = documentSnapshot.getLong("totalXP");
                        int level = levelObj != null ? levelObj.intValue() : 1;
                        int xp = xpObj != null ? xpObj.intValue() : 0;
                        
                        java.util.List<String> friendsList = (java.util.List<String>) documentSnapshot.get("friends");
                        int friendsCount = friendsList != null ? friendsList.size() : 0;

                        usernameText.setText(username != null ? username : "Player");
                        
                        if (isOwnProfile) {
                            emailText.setVisibility(View.VISIBLE);
                            emailText.setText(email != null ? email : "");
                        } else {
                            emailText.setVisibility(View.GONE);
                        }
                        
                        levelText.setText(String.valueOf(level));
                        xpText.setText(String.valueOf(xp));
                        rankBadge.setText(com.mohammad_nazieh_amro.cityconquer.util.LevelSystem.getRankForLevel(level));
                        friendsCountText.setText(String.valueOf(friendsCount));
                    }
                });

        db.collection("users").document(targetUserId)
                .collection("conquests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int citiesCount = queryDocumentSnapshots.size();
                    citiesCountText.setText(String.valueOf(citiesCount));
                    
                    int totalLandmarksConquered = 0;
                    for (var doc : queryDocumentSnapshots) {
                        java.util.List<String> completedList = (java.util.List<String>) doc.get("completedLandmarks");
                        if (completedList != null) {
                            totalLandmarksConquered += completedList.size();
                        }
                    }
                    landmarksCountText.setText(String.valueOf(totalLandmarksConquered));
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}