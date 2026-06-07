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

    private TextView usernameText, emailText, levelText, xpText;
    private TextView rankBadge, citiesCountText, landmarksCountText, friendsCountText;
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
        
        rankBadge = view.findViewById(R.id.rank_badge);
        citiesCountText = view.findViewById(R.id.cities_count_text);
        landmarksCountText = view.findViewById(R.id.landmarks_count_text);
        friendsCountText = view.findViewById(R.id.friends_count_text);
        
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
                        Long levelObj = documentSnapshot.getLong("level");
                        Long xpObj = documentSnapshot.getLong("totalXP");
                        int level = levelObj != null ? levelObj.intValue() : 1;
                        int xp = xpObj != null ? xpObj.intValue() : 0;
                        
                        java.util.List<String> friendsList = (java.util.List<String>) documentSnapshot.get("friends");
                        int friendsCount = friendsList != null ? friendsList.size() : 0;

                        usernameText.setText(username != null ? username : "Player");
                        emailText.setText(email != null ? email : "");
                        
                        levelText.setText(String.valueOf(level));
                        xpText.setText(String.valueOf(xp));
                        rankBadge.setText(com.mohammad_nazieh_amro.cityconquer.util.LevelSystem.getRankForLevel(level));
                        friendsCountText.setText(String.valueOf(friendsCount));
                    }
                });

        db.collection("users").document(currentUserId)
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
}