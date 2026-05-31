package com.mohammad_nazieh_amro.cityconquer.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.model.Landmark;
import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    private TextView cityNameText, progressText;
    private RecyclerView landmarksRecycler;
    private FirebaseFirestore db;
    private String cityId;
    private List<Landmark> landmarks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        db = FirebaseFirestore.getInstance();
        cityId = getIntent().getStringExtra("cityId");

        cityNameText = findViewById(R.id.city_name);
        progressText = findViewById(R.id.progress_text);
        landmarksRecycler = findViewById(R.id.landmarks_recycler);
        landmarksRecycler.setLayoutManager(new LinearLayoutManager(this));

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
                    updateProgress();
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
                    progressText.setText(completed + "/" + landmarks.size() + " Conquered");
                });
    }
}