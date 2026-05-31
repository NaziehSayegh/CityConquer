package com.mohammad_nazieh_amro.cityconquer.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.adapter.LandmarkAdapter;
import com.mohammad_nazieh_amro.cityconquer.model.Landmark;
import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    private TextView cityNameText, progressText;
    private ProgressBar progressBar;
    private RecyclerView landmarksRecycler;
    private FirebaseFirestore db;
    private String cityId;
    private List<Landmark> landmarks = new ArrayList<>();
    private LandmarkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        db = FirebaseFirestore.getInstance();
        cityId = getIntent().getStringExtra("cityId");
        String cityName = getIntent().getStringExtra("cityName");

        cityNameText = findViewById(R.id.city_name);
        progressText = findViewById(R.id.progress_text);
        progressBar = findViewById(R.id.progress_bar);
        landmarksRecycler = findViewById(R.id.landmarks_recycler);
        landmarksRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (cityName != null) cityNameText.setText(cityName);

        adapter = new LandmarkAdapter(landmarks, cityId);
        landmarksRecycler.setAdapter(adapter);

        loadLandmarks();
    }

    private void loadLandmarks() {
        db.collection("cities").document(cityId)
                .collection("landmarks")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    landmarks.clear();
                    for (var doc : queryDocumentSnapshots) {
                        Landmark landmark = doc.toObject(Landmark.class);
                        landmark.setId(doc.getId());
                        landmarks.add(landmark);
                    }
                    // Render landmarks list immediately so they are visible
                    adapter.updateList(landmarks);
                    
                    // Fetch conquest history to update status
                    updateProgress();
                })
                .addOnFailureListener(e -> {
                    android.widget.Toast.makeText(this, "Failed to load landmarks: " + e.getMessage(), 
                            android.widget.Toast.LENGTH_LONG).show();
                });
    }

    private void updateProgress() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId)
                .collection("conquests").document(cityId)
                .get()
                .addOnSuccessListener(doc -> {
                    int completed = 0;
                    if (doc.exists() && doc.get("completedLandmarks") != null) {
                        List<String> completedList =
                                (List<String>) doc.get("completedLandmarks");
                        completed = completedList.size();
                        for (Landmark l : landmarks) {
                            l.setConquered(completedList.contains(l.getId()));
                        }
                    }
                    int total = landmarks.size();
                    int percent = total > 0 ? (completed * 100 / total) : 0;
                    progressText.setText(completed + "/" + total + " Conquered");
                    progressBar.setProgress(percent);
                    
                    // Update list with the updated conquered states
                    adapter.updateList(landmarks);
                })
                .addOnFailureListener(e -> {
                    // Fallback to update view details if conquest check fails (e.g. firestore rules)
                    int total = landmarks.size();
                    progressText.setText("0/" + total + " Conquered");
                    progressBar.setProgress(0);
                    adapter.updateList(landmarks);
                });
    }
}