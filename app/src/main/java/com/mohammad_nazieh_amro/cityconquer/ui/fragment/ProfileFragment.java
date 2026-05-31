package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;

public class ProfileFragment extends Fragment {

    private TextView usernameText, emailText, levelText, xpText, citiesText;
    private FirebaseFirestore db;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usernameText = view.findViewById(R.id.username_text);
        emailText = view.findViewById(R.id.email_text);
        levelText = view.findViewById(R.id.level_text);
        xpText = view.findViewById(R.id.xp_text);
        citiesText = view.findViewById(R.id.cities_text);
        loadProfile();
        return view;
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
                .addOnSuccessListener(queryDocumentSnapshots ->
                        citiesText.setText(queryDocumentSnapshots.size() + " Cities Explored"));
    }
}