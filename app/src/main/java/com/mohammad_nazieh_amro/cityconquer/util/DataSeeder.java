package com.mohammad_nazieh_amro.cityconquer.util;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DataSeeder {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void seedData() {
        android.util.Log.d("SEEDER", "Checking if database is already seeded...");
        db.collection("cities")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        android.util.Log.d("SEEDER", "Cities collection is empty. Starting seed...");
                        executeSeeding();
                    } else {
                        android.util.Log.d("SEEDER", "Database already seeded. Skipping seeding.");
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Failed to access cities collection: " + e.getMessage());
                    android.util.Log.e("SEEDER", "CRITICAL: If the error is 'PERMISSION_DENIED', please update your Firebase Firestore Security Rules " +
                            "to allow read/write access to the 'cities' collection for authenticated users.");
                });
    }

    private static void executeSeeding() {
        seedJerusalem();
        seedTelAviv();
        seedHaifa();
        seedNazareth();
        seedEilat();
        seedDeadSea();
        seedNetanya();
        seedBeerSheva();
        android.util.Log.d("SEEDER", "Seeding calls initiated!");
    }

    // ==================== JERUSALEM ====================
    private static void seedJerusalem() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Jerusalem");
        city.put("country", "Israel");
        city.put("totalLandmarks", 8);
        city.put("coverImage", "");

        db.collection("cities").document("jerusalem")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Jerusalem SUCCESS!");
                    addLandmark("jerusalem", "western_wall",
                            "Western Wall",
                            "The holiest site in Judaism, part of the ancient temple.",
                            31.7767, 35.2345, 100);
                    addLandmark("jerusalem", "al_aqsa",
                            "Al-Aqsa Mosque",
                            "Third holiest site in Islam, located on Temple Mount.",
                            31.7761, 35.2358, 100);
                    addLandmark("jerusalem", "church_holy_sepulchre",
                            "Church of Holy Sepulchre",
                            "Most sacred Christian site, where Jesus was crucified.",
                            31.7784, 35.2297, 100);
                    addLandmark("jerusalem", "damascus_gate",
                            "Damascus Gate",
                            "The largest and most magnificent gate of the Old City.",
                            31.7828, 35.2298, 75);
                    addLandmark("jerusalem", "tower_of_david",
                            "Tower of David",
                            "Ancient citadel located near the Jaffa Gate.",
                            31.7762, 35.2285, 75);
                    addLandmark("jerusalem", "yad_vashem",
                            "Yad Vashem",
                            "Israel's official Holocaust memorial and museum.",
                            31.7741, 35.1753, 100);
                    addLandmark("jerusalem", "mount_of_olives",
                            "Mount of Olives",
                            "Famous mountain with stunning views of Jerusalem.",
                            31.7784, 35.2460, 75);
                    addLandmark("jerusalem", "mahane_yehuda",
                            "Mahane Yehuda Market",
                            "Famous open-air market with food and culture.",
                            31.7842, 35.2136, 50);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Jerusalem FAILED: " + e.getMessage());
                });
    }

    // ==================== TEL AVIV ====================
    private static void seedTelAviv() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Tel Aviv");
        city.put("country", "Israel");
        city.put("totalLandmarks", 8);
        city.put("coverImage", "");

        db.collection("cities").document("tel_aviv")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Tel Aviv SUCCESS!");
                    addLandmark("tel_aviv", "jaffa_port",
                            "Jaffa Port",
                            "Ancient port city with rich history and culture.",
                            32.0524, 34.7503, 100);
                    addLandmark("tel_aviv", "dizengoff_square",
                            "Dizengoff Square",
                            "Famous landmark in central Tel Aviv with a fountain.",
                            32.0796, 34.7739, 75);
                    addLandmark("tel_aviv", "carmel_market",
                            "Carmel Market",
                            "Largest open-air market in Tel Aviv.",
                            32.0686, 34.7726, 75);
                    addLandmark("tel_aviv", "independence_hall",
                            "Independence Hall",
                            "Where Israel declared independence in 1948.",
                            32.0641, 34.7688, 100);
                    addLandmark("tel_aviv", "neve_tzedek",
                            "Neve Tzedek",
                            "First Jewish neighborhood in Tel Aviv, now trendy area.",
                            32.0592, 34.7678, 75);
                    addLandmark("tel_aviv", "tel_aviv_promenade",
                            "Tel Aviv Promenade",
                            "Beautiful beachside promenade along the Mediterranean.",
                            32.0853, 34.7659, 50);
                    addLandmark("tel_aviv", "old_jaffa",
                            "Old Jaffa",
                            "Ancient city with art galleries and restaurants.",
                            32.0530, 34.7520, 100);
                    addLandmark("tel_aviv", "sarona_market",
                            "Sarona Market",
                            "Modern food market in a historic Templar compound.",
                            32.0697, 34.7895, 75);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Tel Aviv FAILED: " + e.getMessage());
                });
    }

    // ==================== HAIFA ====================
    private static void seedHaifa() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Haifa");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");

        db.collection("cities").document("haifa")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Haifa SUCCESS!");
                    addLandmark("haifa", "bahai_gardens",
                            "Bahá'í Gardens",
                            "UNESCO World Heritage Site with stunning terraced gardens.",
                            32.8146, 34.9893, 100);
                    addLandmark("haifa", "german_colony",
                            "German Colony",
                            "Historic neighborhood with restaurants and cafes.",
                            32.8103, 34.9897, 75);
                    addLandmark("haifa", "carmel_center",
                            "Carmel Center",
                            "The upscale neighborhood on top of Mount Carmel.",
                            32.8184, 34.9896, 75);
                    addLandmark("haifa", "elijah_cave",
                            "Cave of Elijah",
                            "Sacred cave where the prophet Elijah is said to have rested.",
                            32.8132, 34.9848, 100);
                    addLandmark("haifa", "haifa_port",
                            "Haifa Port",
                            "One of Israel's largest ports with a beautiful view.",
                            32.8192, 35.0043, 75);
                    addLandmark("haifa", "clandestine_museum",
                            "Clandestine Immigration Museum",
                            "Museum dedicated to Jewish immigration to Israel.",
                            32.8167, 34.9898, 75);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Haifa FAILED: " + e.getMessage());
                });
    }

    // ==================== NAZARETH ====================
    private static void seedNazareth() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Nazareth");
        city.put("country", "Israel");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");

        db.collection("cities").document("nazareth")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Nazareth SUCCESS!");
                    addLandmark("nazareth", "basilica_annunciation",
                            "Basilica of the Annunciation",
                            "Largest Christian church in the Middle East.",
                            32.7027, 35.2967, 100);
                    addLandmark("nazareth", "old_city_nazareth",
                            "Old City of Nazareth",
                            "Historic market and ancient streets.",
                            32.7018, 35.2981, 75);
                    addLandmark("nazareth", "st_joseph_church",
                            "St. Joseph's Church",
                            "Built over the site of Joseph's carpentry workshop.",
                            32.7031, 35.2971, 75);
                    addLandmark("nazareth", "nazareth_village",
                            "Nazareth Village",
                            "Open-air museum recreating life in ancient Nazareth.",
                            32.6992, 35.3042, 100);
                    addLandmark("nazareth", "white_mosque",
                            "White Mosque",
                            "Historic mosque in the heart of Nazareth.",
                            32.7021, 35.2978, 75);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Nazareth FAILED: " + e.getMessage());
                });
    }

    // ==================== EILAT ====================
    private static void seedEilat() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Eilat");
        city.put("country", "Israel");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");

        db.collection("cities").document("eilat")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Eilat SUCCESS!");
                    addLandmark("eilat", "underwater_observatory",
                            "Underwater Observatory",
                            "Marine park with underwater viewing of Red Sea fish.",
                            29.4969, 34.9135, 100);
                    addLandmark("eilat", "coral_beach",
                            "Coral Beach Nature Reserve",
                            "Beautiful coral reef nature reserve.",
                            29.5050, 34.9100, 100);
                    addLandmark("eilat", "dolphins_reef",
                            "Dolphin Reef",
                            "Swim with dolphins in the Red Sea.",
                            29.5100, 34.9078, 100);
                    addLandmark("eilat", "kings_city",
                            "Kings City",
                            "Large entertainment and adventure park.",
                            29.5480, 34.9519, 75);
                    addLandmark("eilat", "eilat_promenade",
                            "Eilat Promenade",
                            "Beautiful seaside promenade along the Red Sea.",
                            29.5569, 34.9519, 50);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Eilat FAILED: " + e.getMessage());
                });
    }

    // ==================== DEAD SEA ====================
    private static void seedDeadSea() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Dead Sea");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");

        db.collection("cities").document("dead_sea")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Dead Sea SUCCESS!");
                    addLandmark("dead_sea", "ein_gedi",
                            "Ein Gedi Nature Reserve",
                            "Beautiful oasis with waterfalls and wildlife.",
                            31.4622, 35.3886, 100);
                    addLandmark("dead_sea", "masada",
                            "Masada Fortress",
                            "UNESCO World Heritage ancient fortress on a clifftop.",
                            31.3156, 35.3536, 100);
                    addLandmark("dead_sea", "dead_sea_beach",
                            "Dead Sea Beach",
                            "Float in the saltiest lake in the world.",
                            31.5590, 35.4732, 100);
                    addLandmark("dead_sea", "qumran_caves",
                            "Qumran Caves",
                            "Where the Dead Sea Scrolls were discovered.",
                            31.7425, 35.4605, 100);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Dead Sea FAILED: " + e.getMessage());
                });
    }

    // ==================== NETANYA ====================
    private static void seedNetanya() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Netanya");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");

        db.collection("cities").document("netanya")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Netanya SUCCESS!");
                    addLandmark("netanya", "independence_square",
                            "Independence Square",
                            "The main square of Netanya with sea views.",
                            32.3328, 34.8598, 75);
                    addLandmark("netanya", "netanya_beach",
                            "Netanya Beach",
                            "Beautiful Mediterranean beach.",
                            32.3315, 34.8520, 75);
                    addLandmark("netanya", "poleg_nature_reserve",
                            "Poleg Nature Reserve",
                            "Beautiful nature reserve near the beach.",
                            32.3089, 34.8578, 75);
                    addLandmark("netanya", "netanya_promenade",
                            "Netanya Cliff Promenade",
                            "Stunning promenade on top of cliffs.",
                            32.3315, 34.8530, 50);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Netanya FAILED: " + e.getMessage());
                });
    }

    // ==================== BEER SHEVA ====================
    private static void seedBeerSheva() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Beer Sheva");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");

        db.collection("cities").document("beer_sheva")
                .set(city)
                .addOnSuccessListener(unused -> {
                    android.util.Log.d("SEEDER", "Beer Sheva SUCCESS!");
                    addLandmark("beer_sheva", "tel_beer_sheva",
                            "Tel Beer Sheva",
                            "UNESCO World Heritage ancient city of the patriarchs.",
                            31.2437, 34.8397, 100);
                    addLandmark("beer_sheva", "old_city_bs",
                            "Old City of Beer Sheva",
                            "Ottoman-era buildings and the oldest mosque.",
                            31.2459, 34.7953, 75);
                    addLandmark("beer_sheva", "negev_museum",
                            "Negev Museum of Art",
                            "Art museum in a historic Ottoman building.",
                            31.2461, 34.7947, 75);
                    addLandmark("beer_sheva", "abraham_well",
                            "Abraham's Well",
                            "The ancient well of the patriarch Abraham.",
                            31.2459, 34.7953, 100);
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SEEDER", "Beer Sheva FAILED: " + e.getMessage());
                });
    }

    // ==================== HELPER ====================
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
                .set(landmark)
                .addOnSuccessListener(unused ->
                        android.util.Log.d("SEEDER", landmarkId + " landmark added!"))
                .addOnFailureListener(e ->
                        android.util.Log.e("SEEDER", landmarkId + " FAILED: " + e.getMessage()));
    }
}