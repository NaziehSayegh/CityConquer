package com.mohammad_nazieh_amro.cityconquer.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameText, emailText, levelText, xpText, citiesText;
    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usernameText = findViewById(R.id.username_text);
        emailText = findViewById(R.id.email_text);
        levelText = findViewById(R.id.level_text);
        xpText = findViewById(R.id.xp_text);
        citiesText = findViewById(R.id.cities_text);

        loadProfile();
    }

    private void loadProfile() {
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        Long level = documentSnapshot.getLong("level");
                        Long xp = documentSnapshot.getLong("totalXP");

                        usernameText.setText(username != null ? username : "Player");
                        emailText.setText(email != null ? email : "");
                        levelText.setText("Level " + (level != null ? level : 1));
                        xpText.setText((xp != null ? xp : 0) + " XP");
                    }
                });

        db.collection("users").document(currentUserId)
                .collection("conquests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int citiesCount = queryDocumentSnapshots.size();
                    citiesText.setText(citiesCount + " Cities Explored");
                });
    }
}