package com.mohammad_nazieh_amro.cityconquer.util;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DataSeeder {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void seedData() {
        seedJerusalem();
        seedTelAviv();
    }

    private static void seedJerusalem() {
        Map<String, Object> jerusalem = new HashMap<>();
        jerusalem.put("name", "Jerusalem");
        jerusalem.put("country", "Israel");
        jerusalem.put("totalLandmarks", 5);
        jerusalem.put("coverImage", "");

        db.collection("cities").document("jerusalem")
                .set(jerusalem)
                .addOnSuccessListener(unused -> {
                    addLandmark("jerusalem", "western_wall",
                            "Western Wall",
                            "The holiest site in Judaism",
                            31.7767, 35.2345, 100);
                    addLandmark("jerusalem", "al_aqsa",
                            "Al-Aqsa Mosque",
                            "Third holiest site in Islam",
                            31.7761, 35.2358, 100);
                    addLandmark("jerusalem", "church_holy_sepulchre",
                            "Church of Holy Sepulchre",
                            "Most sacred Christian site",
                            31.7784, 35.2297, 100);
                    addLandmark("jerusalem", "damascus_gate",
                            "Damascus Gate",
                            "Main entrance to the Old City",
                            31.7828, 35.2298, 75);
                    addLandmark("jerusalem", "tower_of_david",
                            "Tower of David",
                            "Ancient citadel of Jerusalem",
                            31.7762, 35.2285, 75);
                });
    }

    private static void seedTelAviv() {
        Map<String, Object> telAviv = new HashMap<>();
        telAviv.put("name", "Tel Aviv");
        telAviv.put("country", "Israel");
        telAviv.put("totalLandmarks", 5);
        telAviv.put("coverImage", "");

        db.collection("cities").document("tel_aviv")
                .set(telAviv)
                .addOnSuccessListener(unused -> {
                    addLandmark("tel_aviv", "jaffa_port",
                            "Jaffa Port",
                            "Ancient port city with rich history",
                            32.0524, 34.7503, 100);
                    addLandmark("tel_aviv", "dizengoff_square",
                            "Dizengoff Square",
                            "Famous landmark in central Tel Aviv",
                            32.0796, 34.7739, 75);
                    addLandmark("tel_aviv", "carmel_market",
                            "Carmel Market",
                            "Largest open-air market in Tel Aviv",
                            32.0686, 34.7726, 75);
                    addLandmark("tel_aviv", "independence_hall",
                            "Independence Hall",
                            "Where Israel declared independence",
                            32.0641, 34.7688, 100);
                    addLandmark("tel_aviv", "neve_tzedek",
                            "Neve Tzedek",
                            "First Jewish neighborhood in Tel Aviv",
                            32.0592, 34.7678, 75);
                });
    }

    private static void addLandmark(String cityId, String landmarkId,
                                    String name, String description,
                                    double lat, double lng, int xp) {
        Map<String, Object> landmark = new HashMap<>();
        landmark.put("name", name);
        landmark.put("cityId", cityId);
        landmark.put("description", description);
        landmark.put("latitude", lat);
        landmark.put("longitude", lng);
        landmark.put("xp", xp);
        landmark.put("conquered", false);

        db.collection("cities").document(cityId)
                .collection("landmarks").document(landmarkId)
                .set(landmark);
    }
}