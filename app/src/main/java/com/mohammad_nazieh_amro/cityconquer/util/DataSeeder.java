package com.mohammad_nazieh_amro.cityconquer.util;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DataSeeder {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * List of ALL city IDs we want in the app.
     * Any city in this list that doesn't exist in Firestore will be seeded automatically.
     */
    private static final String[] ALL_CITY_IDS = {
            "jerusalem", "tel_aviv", "haifa", "nazareth", "eilat",
            "dead_sea", "netanya", "beer_sheva", "ramallah",
            "acre", "tiberias", "jericho", "nablus", "hebron",
            "gaza_city", "bethlehem", "jenin", "tulkarm", "qalqilya",
            "safed", "afula", "rehovot", "petah_tikva", "rishon_lezion",
            "ashdod", "ashkelon", "modiin", "caesarea", "beit_shean",
            "jordan_valley", "galilee", "negev", "golan_heights"
    };

    public static void seedData(final android.content.Context context) {
        android.util.Log.d("SEEDER", "Starting full city check & seed...");
        // Seed every city that is missing
        for (String cityId : ALL_CITY_IDS) {
            seedCityIfMissing(context, cityId);
        }
        seedFakeUsers(context);
    }

    /** Checks Firestore – if the city document doesn't exist, seed it. */
    private static void seedCityIfMissing(final android.content.Context context, String cityId) {
        db.collection("cities").document(cityId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        android.util.Log.d("SEEDER", cityId + " missing – seeding...");
                        seedCity(cityId);
                    } else {
                        android.util.Log.d("SEEDER", cityId + " already exists – skipping.");
                    }
                })
                .addOnFailureListener(e ->
                        android.util.Log.e("SEEDER", "Failed to check " + cityId + ": " + e.getMessage()));
    }

    private static void seedCity(String cityId) {
        switch (cityId) {
            case "jerusalem":       seedJerusalem();     break;
            case "tel_aviv":        seedTelAviv();       break;
            case "haifa":           seedHaifa();         break;
            case "nazareth":        seedNazareth();      break;
            case "eilat":           seedEilat();         break;
            case "dead_sea":        seedDeadSea();       break;
            case "netanya":         seedNetanya();       break;
            case "beer_sheva":      seedBeerSheva();     break;
            case "ramallah":        seedRamallah();      break;
            case "acre":            seedAcre();          break;
            case "tiberias":        seedTiberias();      break;
            case "jericho":         seedJericho();       break;
            case "nablus":          seedNablus();        break;
            case "hebron":          seedHebron();        break;
            case "gaza_city":       seedGaza();          break;
            case "bethlehem":       seedBethlehem();     break;
            case "jenin":           seedJenin();         break;
            case "tulkarm":         seedTulkarm();       break;
            case "qalqilya":        seedQalqilya();      break;
            case "safed":           seedSafed();         break;
            case "afula":           seedAfula();         break;
            case "rehovot":         seedRehovot();       break;
            case "petah_tikva":     seedPetahTikva();    break;
            case "rishon_lezion":   seedRishonLezion();  break;
            case "ashdod":          seedAshdod();        break;
            case "ashkelon":        seedAshkelon();      break;
            case "modiin":          seedModiin();        break;
            case "caesarea":        seedCaesarea();      break;
            case "beit_shean":      seedBeitShean();     break;
            case "jordan_valley":   seedJordanValley();  break;
            case "galilee":         seedGalilee();       break;
            case "negev":           seedNegev();         break;
            case "golan_heights":   seedGolanHeights();  break;
        }
    }

    // ==================== JERUSALEM ====================
    private static void seedJerusalem() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Jerusalem");
        city.put("country", "Israel/Palestine");
        city.put("totalLandmarks", 10);
        city.put("coverImage", "");
        db.collection("cities").document("jerusalem").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("jerusalem", "western_wall", "Western Wall",
                            "The holiest site in Judaism, part of the ancient temple.", 31.7767, 35.2345, 100);
                    addLandmark("jerusalem", "al_aqsa", "Al-Aqsa Mosque",
                            "Third holiest site in Islam, located on Temple Mount.", 31.7761, 35.2358, 100);
                    addLandmark("jerusalem", "church_holy_sepulchre", "Church of Holy Sepulchre",
                            "Most sacred Christian site, where Jesus was crucified.", 31.7784, 35.2297, 100);
                    addLandmark("jerusalem", "damascus_gate", "Damascus Gate",
                            "The largest and most magnificent gate of the Old City.", 31.7828, 35.2298, 75);
                    addLandmark("jerusalem", "tower_of_david", "Tower of David",
                            "Ancient citadel located near the Jaffa Gate.", 31.7762, 35.2285, 75);
                    addLandmark("jerusalem", "yad_vashem", "Yad Vashem",
                            "Israel's official Holocaust memorial and museum.", 31.7741, 35.1753, 100);
                    addLandmark("jerusalem", "mount_of_olives", "Mount of Olives",
                            "Famous mountain with stunning views of Jerusalem.", 31.7784, 35.2460, 75);
                    addLandmark("jerusalem", "mahane_yehuda", "Mahane Yehuda Market",
                            "Famous open-air market with food and culture.", 31.7842, 35.2136, 50);
                    addLandmark("jerusalem", "israel_museum", "Israel Museum",
                            "Home to the Dead Sea Scrolls and world-class art.", 31.7726, 35.2042, 100);
                    addLandmark("jerusalem", "garden_of_gethsemane", "Garden of Gethsemane",
                            "Ancient olive grove where Jesus prayed before his arrest.", 31.7793, 35.2397, 100);
                });
    }

    // ==================== TEL AVIV ====================
    private static void seedTelAviv() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Tel Aviv");
        city.put("country", "Israel");
        city.put("totalLandmarks", 9);
        city.put("coverImage", "");
        db.collection("cities").document("tel_aviv").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("tel_aviv", "jaffa_port", "Jaffa Port",
                            "Ancient port city with rich history and culture.", 32.0524, 34.7503, 100);
                    addLandmark("tel_aviv", "dizengoff_square", "Dizengoff Square",
                            "Famous landmark in central Tel Aviv with a fountain.", 32.0796, 34.7739, 75);
                    addLandmark("tel_aviv", "carmel_market", "Carmel Market",
                            "Largest open-air market in Tel Aviv.", 32.0686, 34.7726, 75);
                    addLandmark("tel_aviv", "independence_hall", "Independence Hall",
                            "Where Israel declared independence in 1948.", 32.0641, 34.7688, 100);
                    addLandmark("tel_aviv", "neve_tzedek", "Neve Tzedek",
                            "First Jewish neighborhood in Tel Aviv, now trendy area.", 32.0592, 34.7678, 75);
                    addLandmark("tel_aviv", "tel_aviv_promenade", "Tel Aviv Promenade",
                            "Beautiful beachside promenade along the Mediterranean.", 32.0853, 34.7659, 50);
                    addLandmark("tel_aviv", "old_jaffa", "Old Jaffa",
                            "Ancient city with art galleries and restaurants.", 32.0530, 34.7520, 100);
                    addLandmark("tel_aviv", "sarona_market", "Sarona Market",
                            "Modern food market in a historic Templar compound.", 32.0697, 34.7895, 75);
                    addLandmark("tel_aviv", "tel_aviv_museum", "Tel Aviv Museum of Art",
                            "Premier art museum with Israeli and international works.", 32.0775, 34.7905, 75);
                });
    }

    // ==================== HAIFA ====================
    private static void seedHaifa() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Haifa");
        city.put("country", "Israel");
        city.put("totalLandmarks", 7);
        city.put("coverImage", "");
        db.collection("cities").document("haifa").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("haifa", "bahai_gardens", "Bahá'í Gardens",
                            "UNESCO World Heritage terraced gardens on Mount Carmel.", 32.8146, 34.9893, 100);
                    addLandmark("haifa", "german_colony", "German Colony",
                            "Historic neighborhood with restaurants and cafes.", 32.8103, 34.9897, 75);
                    addLandmark("haifa", "carmel_center", "Carmel Center",
                            "The upscale neighborhood on top of Mount Carmel.", 32.8184, 34.9896, 75);
                    addLandmark("haifa", "elijah_cave", "Cave of Elijah",
                            "Sacred cave where the prophet Elijah is said to have rested.", 32.8132, 34.9848, 100);
                    addLandmark("haifa", "haifa_port", "Haifa Port",
                            "One of Israel's largest ports with a beautiful view.", 32.8192, 35.0043, 75);
                    addLandmark("haifa", "clandestine_museum", "Clandestine Immigration Museum",
                            "Museum dedicated to Jewish immigration to Israel.", 32.8167, 34.9898, 75);
                    addLandmark("haifa", "tikotin_museum", "Tikotin Museum of Japanese Art",
                            "The only museum in the Middle East dedicated to Japanese art.", 32.8170, 34.9893, 75);
                });
    }

    // ==================== NAZARETH ====================
    private static void seedNazareth() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Nazareth");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("nazareth").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("nazareth", "basilica_annunciation", "Basilica of the Annunciation",
                            "Largest Christian church in the Middle East.", 32.7027, 35.2967, 100);
                    addLandmark("nazareth", "old_city_nazareth", "Old City of Nazareth",
                            "Historic market and ancient streets.", 32.7018, 35.2981, 75);
                    addLandmark("nazareth", "st_joseph_church", "St. Joseph's Church",
                            "Built over the site of Joseph's carpentry workshop.", 32.7031, 35.2971, 75);
                    addLandmark("nazareth", "nazareth_village", "Nazareth Village",
                            "Open-air museum recreating life in ancient Nazareth.", 32.6992, 35.3042, 100);
                    addLandmark("nazareth", "white_mosque", "White Mosque",
                            "Historic mosque in the heart of Nazareth.", 32.7021, 35.2978, 75);
                    addLandmark("nazareth", "mary_well", "Mary's Well",
                            "Ancient spring where Mary is said to have drawn water.", 32.7027, 35.2992, 75);
                });
    }

    // ==================== EILAT ====================
    private static void seedEilat() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Eilat");
        city.put("country", "Israel");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");
        db.collection("cities").document("eilat").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("eilat", "underwater_observatory", "Underwater Observatory",
                            "Marine park with underwater viewing of Red Sea fish.", 29.4969, 34.9135, 100);
                    addLandmark("eilat", "coral_beach", "Coral Beach Nature Reserve",
                            "Beautiful coral reef nature reserve.", 29.5050, 34.9100, 100);
                    addLandmark("eilat", "dolphins_reef", "Dolphin Reef",
                            "Swim with dolphins in the Red Sea.", 29.5100, 34.9078, 100);
                    addLandmark("eilat", "kings_city", "Kings City",
                            "Large entertainment and adventure park.", 29.5480, 34.9519, 75);
                    addLandmark("eilat", "eilat_promenade", "Eilat Promenade",
                            "Beautiful seaside promenade along the Red Sea.", 29.5569, 34.9519, 50);
                });
    }

    // ==================== DEAD SEA ====================
    private static void seedDeadSea() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Dead Sea");
        city.put("country", "Israel/Palestine");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("dead_sea").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("dead_sea", "ein_gedi", "Ein Gedi Nature Reserve",
                            "Beautiful oasis with waterfalls and wildlife.", 31.4622, 35.3886, 100);
                    addLandmark("dead_sea", "masada", "Masada Fortress",
                            "UNESCO World Heritage ancient fortress on a clifftop.", 31.3156, 35.3536, 100);
                    addLandmark("dead_sea", "dead_sea_beach", "Dead Sea Beach",
                            "Float in the saltiest lake in the world.", 31.5590, 35.4732, 100);
                    addLandmark("dead_sea", "qumran_caves", "Qumran Caves",
                            "Where the Dead Sea Scrolls were discovered.", 31.7425, 35.4605, 100);
                });
    }

    // ==================== NETANYA ====================
    private static void seedNetanya() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Netanya");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("netanya").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("netanya", "independence_square", "Independence Square",
                            "The main square of Netanya with sea views.", 32.3328, 34.8598, 75);
                    addLandmark("netanya", "netanya_beach", "Netanya Beach",
                            "Beautiful Mediterranean beach.", 32.3315, 34.8520, 75);
                    addLandmark("netanya", "poleg_nature_reserve", "Poleg Nature Reserve",
                            "Beautiful nature reserve near the beach.", 32.3089, 34.8578, 75);
                    addLandmark("netanya", "netanya_promenade", "Netanya Cliff Promenade",
                            "Stunning promenade on top of cliffs overlooking the sea.", 32.3315, 34.8530, 50);
                });
    }

    // ==================== BEER SHEVA ====================
    private static void seedBeerSheva() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Beer Sheva");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("beer_sheva").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("beer_sheva", "tel_beer_sheva", "Tel Beer Sheva",
                            "UNESCO World Heritage ancient city of the patriarchs.", 31.2437, 34.8397, 100);
                    addLandmark("beer_sheva", "old_city_bs", "Old City of Beer Sheva",
                            "Ottoman-era buildings and the oldest mosque.", 31.2459, 34.7953, 75);
                    addLandmark("beer_sheva", "negev_museum", "Negev Museum of Art",
                            "Art museum in a historic Ottoman building.", 31.2461, 34.7947, 75);
                    addLandmark("beer_sheva", "abraham_well", "Abraham's Well",
                            "The ancient well of the patriarch Abraham.", 31.2459, 34.7953, 100);
                });
    }

    // ==================== RAMALLAH ====================
    private static void seedRamallah() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Ramallah");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("ramallah").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("ramallah", "lazaward", "Lazaward Restaurant",
                            "A beautiful restaurant at Al Tireh Street, Ramallah.", 31.9209736, 35.1762547, 100);
                    addLandmark("ramallah", "muqata", "Muqata (Presidential Palace)",
                            "The official headquarters of the Palestinian Authority.", 31.9074, 35.1985, 100);
                    addLandmark("ramallah", "yasser_arafat_mausoleum", "Yasser Arafat Mausoleum",
                            "The final resting place of Palestinian leader Yasser Arafat.", 31.9080, 35.1987, 100);
                    addLandmark("ramallah", "ramallah_cultural_palace", "Ramallah Cultural Palace",
                            "A major cultural center hosting events and exhibitions.", 31.9041, 35.2051, 75);
                    addLandmark("ramallah", "al_manara_square", "Al-Manara Square",
                            "The central square of Ramallah, heart of the city.", 31.9022, 35.2064, 75);
                    addLandmark("ramallah", "bireh_old_city", "Al-Bireh Old City",
                            "Historic twin city of Ramallah with ancient ruins.", 31.9139, 35.2256, 75);
                });
    }

    // ==================== ACRE (AKKO) ====================
    private static void seedAcre() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Acre (Akko)");
        city.put("country", "Israel");
        city.put("totalLandmarks", 7);
        city.put("coverImage", "");
        db.collection("cities").document("acre").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("acre", "old_city_acre", "Old City of Acre",
                            "UNESCO World Heritage walled city on the Mediterranean.", 32.9261, 35.0699, 100);
                    addLandmark("acre", "crusader_tunnel", "Crusader Tunnel",
                            "Underground tunnel built by the Knights Templar.", 32.9262, 35.0700, 100);
                    addLandmark("acre", "al_jazzar_mosque", "Al-Jazzar Mosque",
                            "The largest mosque in Israel outside of Jerusalem.", 32.9264, 35.0706, 100);
                    addLandmark("acre", "knights_halls", "Knights' Halls",
                            "Magnificent underground Crusader-era halls.", 32.9259, 35.0697, 100);
                    addLandmark("acre", "acre_port", "Acre Harbor",
                            "Ancient harbor used since Phoenician times.", 32.9278, 35.0664, 75);
                    addLandmark("acre", "bahai_gardens_acre", "Bahá'í Gardens (Acre)",
                            "The holiest Bahá'í site, containing the Shrine of Bahá'u'lláh.", 32.9437, 35.0913, 100);
                    addLandmark("acre", "hammam_basha", "Hammam al-Basha",
                            "Ottoman-era bathhouse turned into a museum.", 32.9265, 35.0710, 75);
                });
    }

    // ==================== TIBERIAS ====================
    private static void seedTiberias() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Tiberias");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("tiberias").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("tiberias", "sea_of_galilee", "Sea of Galilee (Kinneret)",
                            "The largest freshwater lake in Israel, sacred to Christians.", 32.8200, 35.5800, 100);
                    addLandmark("tiberias", "tomb_of_maimonides", "Tomb of Maimonides",
                            "Resting place of the famous Jewish philosopher and physician.", 32.7967, 35.5317, 100);
                    addLandmark("tiberias", "ancient_synagogue_tiberias", "Ancient Tiberias Synagogue",
                            "Ruins of an ancient synagogue from Roman times.", 32.7960, 35.5319, 100);
                    addLandmark("tiberias", "hot_springs_tiberias", "Hamat Tiberias Hot Springs",
                            "Ancient hot springs used since Roman times.", 32.7723, 35.5442, 75);
                    addLandmark("tiberias", "tiberias_promenade", "Tiberias Promenade",
                            "Beautiful lakeside promenade with restaurants.", 32.7951, 35.5313, 50);
                    addLandmark("tiberias", "mount_arbel", "Mount Arbel",
                            "Stunning cliff with panoramic views of the Sea of Galilee.", 32.8168, 35.4978, 100);
                });
    }

    // ==================== JERICHO ====================
    private static void seedJericho() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Jericho");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 7);
        city.put("coverImage", "");
        db.collection("cities").document("jericho").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("jericho", "tel_es_sultan", "Tel es-Sultan (Ancient Jericho)",
                            "The oldest city in the world, continuously inhabited for 10,000 years.", 31.8671, 35.4397, 100);
                    addLandmark("jericho", "hisham_palace", "Hisham's Palace",
                            "Stunning Umayyad-era palace ruins from the 8th century.", 31.8830, 35.4459, 100);
                    addLandmark("jericho", "mount_temptation", "Mount of Temptation",
                            "Where Jesus fasted for 40 days. Monastery carved into cliff.", 31.8784, 35.4350, 100);
                    addLandmark("jericho", "jericho_cable_car", "Jericho Cable Car",
                            "Rides up to the Mount of Temptation monastery.", 31.8758, 35.4342, 75);
                    addLandmark("jericho", "sycamore_tree", "Zacchaeus Sycamore Tree",
                            "Ancient sycamore tree mentioned in the Bible.", 31.8557, 35.4473, 75);
                    addLandmark("jericho", "baptism_site", "Qasr al-Yahud Baptism Site",
                            "Traditional site of Jesus' baptism by John the Baptist.", 31.8378, 35.5534, 100);
                    addLandmark("jericho", "elisha_spring", "Elisha's Spring (Ein es-Sultan)",
                            "Ancient spring said to have been purified by the prophet Elisha.", 31.8665, 35.4451, 75);
                });
    }

    // ==================== NABLUS ====================
    private static void seedNablus() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Nablus");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 7);
        city.put("coverImage", "");
        db.collection("cities").document("nablus").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("nablus", "jacob_well", "Jacob's Well",
                            "Where Jesus spoke to the Samaritan woman. Biblical site.", 32.2123, 35.2885, 100);
                    addLandmark("nablus", "joseph_tomb", "Joseph's Tomb",
                            "Sacred site believed to be the burial place of the patriarch Joseph.", 32.2130, 35.2891, 100);
                    addLandmark("nablus", "old_city_nablus", "Nablus Old City",
                            "Ottoman-era city with famous soap factories and markets.", 32.2215, 35.2602, 100);
                    addLandmark("nablus", "mount_gerizim", "Mount Gerizim",
                            "The holy mountain of the Samaritan people.", 32.1966, 35.2726, 100);
                    addLandmark("nablus", "al_nasr_mosque", "Al-Nasr Mosque",
                            "Historic mosque built on the remains of a Crusader church.", 32.2218, 35.2608, 75);
                    addLandmark("nablus", "balata_refugee_camp", "Balata Refugee Camp",
                            "Largest refugee camp in the West Bank.", 32.2160, 35.2810, 75);
                    addLandmark("nablus", "kanafeh_shop", "Nablus Kanafeh",
                            "The birthplace of the world-famous kanafeh dessert.", 32.2219, 35.2605, 50);
                });
    }

    // ==================== HEBRON ====================
    private static void seedHebron() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Hebron (Al-Khalil)");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("hebron").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("hebron", "cave_of_machpelah", "Cave of Machpelah",
                            "Burial site of the patriarchs Abraham, Isaac, Jacob and their wives.", 31.5245, 35.1102, 100);
                    addLandmark("hebron", "old_city_hebron", "Hebron Old City",
                            "One of the oldest continuously inhabited cities in the world.", 31.5251, 35.1098, 100);
                    addLandmark("hebron", "hebron_glass", "Hebron Glass Factory",
                            "Famous for centuries-old tradition of handblown colored glass.", 31.5240, 35.1088, 75);
                    addLandmark("hebron", "tel_hebron", "Tel Hebron (Tel Rumeida)",
                            "Archaeological site of ancient Canaanite Hebron.", 31.5290, 35.1062, 100);
                    addLandmark("hebron", "ibrahimi_mosque", "Ibrahimi Mosque",
                            "Mosque built over the Cave of Machpelah, shared with a synagogue.", 31.5245, 35.1102, 100);
                    addLandmark("hebron", "grape_festival", "Hebron Grape Festival Square",
                            "Site of the famous annual grape festival.", 31.5250, 35.1090, 50);
                });
    }

    // ==================== GAZA CITY ====================
    private static void seedGaza() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Gaza City");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");
        db.collection("cities").document("gaza_city").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("gaza_city", "great_mosque_gaza", "Great Mosque of Gaza",
                            "One of the oldest mosques in the world, ancient Crusader church converted.", 31.5017, 34.4667, 100);
                    addLandmark("gaza_city", "al_qasr_palace", "Al-Qasr Palace",
                            "Remains of a grand Mamluk-era palace in Gaza.", 31.5013, 34.4672, 100);
                    addLandmark("gaza_city", "pasha_palace", "Napoleon's Palace (Pasha's Palace)",
                            "Historic palace used by Napoleon during his campaign.", 31.5025, 34.4669, 100);
                    addLandmark("gaza_city", "gold_market_gaza", "Gaza Gold Market",
                            "The famous gold and jewelry market of Gaza.", 31.5018, 34.4665, 50);
                    addLandmark("gaza_city", "anthedon_harbor", "Anthedon Harbor (Blakhiya)",
                            "Ancient Hellenistic-era harbor site.", 31.5467, 34.4872, 100);
                });
    }

    // ==================== BETHLEHEM ====================
    private static void seedBethlehem() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Bethlehem");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 7);
        city.put("coverImage", "");
        db.collection("cities").document("bethlehem").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("bethlehem", "church_of_nativity", "Church of the Nativity",
                            "UNESCO-listed birthplace of Jesus Christ.", 31.7042, 35.2077, 100);
                    addLandmark("bethlehem", "manger_square", "Manger Square",
                            "The central square of Bethlehem in front of the Nativity Church.", 31.7040, 35.2070, 75);
                    addLandmark("bethlehem", "milk_grotto", "Milk Grotto",
                            "Chapel where Mary nursed baby Jesus according to tradition.", 31.7035, 35.2081, 75);
                    addLandmark("bethlehem", "shepherds_field", "Shepherd's Field",
                            "Where angels announced the birth of Jesus to shepherds.", 31.6961, 35.2176, 100);
                    addLandmark("bethlehem", "rachel_tomb", "Rachel's Tomb",
                            "Sacred tomb of the biblical matriarch Rachel.", 31.7193, 35.1989, 100);
                    addLandmark("bethlehem", "banksy_wall", "Banksy Wall Art (West Bank Barrier)",
                            "Famous murals by Banksy on the West Bank separation wall.", 31.7052, 35.2000, 75);
                    addLandmark("bethlehem", "herodion", "Herodion",
                            "Herod's magnificent palace-fortress built on a hilltop.", 31.6665, 35.2407, 100);
                });
    }

    // ==================== JENIN ====================
    private static void seedJenin() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Jenin");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("jenin").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("jenin", "jenin_freedom_theatre", "Freedom Theatre",
                            "Famous Palestinian cultural center and theatre.", 32.4625, 35.3008, 75);
                    addLandmark("jenin", "jenin_refugee_camp", "Jenin Refugee Camp",
                            "One of the most prominent Palestinian refugee camps.", 32.4648, 35.3021, 75);
                    addLandmark("jenin", "bir_al_amir", "Bir al-Amir Spring",
                            "Ancient spring with historical significance.", 32.4590, 35.2948, 75);
                    addLandmark("jenin", "zababdeh", "Zababdeh Village",
                            "Historic village with a major Christian community.", 32.4218, 35.3215, 75);
                });
    }

    // ==================== TULKARM ====================
    private static void seedTulkarm() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Tulkarm");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("tulkarm").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("tulkarm", "tulkarm_old_market", "Old Market of Tulkarm",
                            "Historic market in the center of the city.", 32.3100, 35.0296, 75);
                    addLandmark("tulkarm", "khirbet_samra", "Khirbet Samra",
                            "Ancient ruins from the Byzantine era.", 32.3312, 35.0651, 75);
                    addLandmark("tulkarm", "an_najah_univ_tulkarm", "Khader Shaker Garden",
                            "Public garden and green space in Tulkarm.", 32.3093, 35.0276, 50);
                    addLandmark("tulkarm", "tulkarm_refugee_camp", "Tulkarm Refugee Camp",
                            "One of the oldest Palestinian refugee camps.", 32.3110, 35.0308, 75);
                });
    }

    // ==================== QALQILYA ====================
    private static void seedQalqilya() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Qalqilya");
        city.put("country", "Palestine");
        city.put("totalLandmarks", 3);
        city.put("coverImage", "");
        db.collection("cities").document("qalqilya").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("qalqilya", "qalqilya_zoo", "Qalqilya Zoo",
                            "The only zoo in the West Bank.", 32.1895, 34.9703, 75);
                    addLandmark("qalqilya", "qalqilya_museum", "Qalqilya Museum",
                            "Local history and archaeology museum.", 32.1892, 34.9699, 75);
                    addLandmark("qalqilya", "habla_gate", "Habla Agricultural Gate",
                            "One of the notable gates of the West Bank barrier.", 32.1511, 34.9829, 75);
                });
    }

    // ==================== SAFED (TZFAT) ====================
    private static void seedSafed() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Safed (Tzfat)");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("safed").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("safed", "artist_quarter", "Artists' Quarter",
                            "Colorful quarter with galleries in the ancient Jewish mystical city.", 32.9652, 35.4948, 100);
                    addLandmark("safed", "ancient_synagogues", "Ancient Synagogues of Safed",
                            "16th-century Kabbalistic synagogues still in use today.", 32.9648, 35.4952, 100);
                    addLandmark("safed", "citadel_safed", "Safed Citadel",
                            "Ruins of a Crusader-era fortress on the hilltop.", 32.9662, 35.4962, 100);
                    addLandmark("safed", "old_cemetery_safed", "Old Cemetery of Safed",
                            "Resting place of great Kabbalistic scholars.", 32.9637, 35.4963, 75);
                    addLandmark("safed", "ari_synagogue", "Ari Synagogue (Ashkenazi)",
                            "Synagogue associated with Rabbi Isaac Luria, the great Kabbalist.", 32.9645, 35.4961, 100);
                    addLandmark("safed", "safed_candles", "Safed Candles Factory",
                            "Unique handmade candles made by a local family since the 1960s.", 32.9651, 35.4951, 50);
                });
    }

    // ==================== AFULA ====================
    private static void seedAfula() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Afula");
        city.put("country", "Israel");
        city.put("totalLandmarks", 3);
        city.put("coverImage", "");
        db.collection("cities").document("afula").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("afula", "megiddo", "Megiddo (Armageddon)",
                            "UNESCO World Heritage site — the biblical Armageddon.", 32.5844, 35.1838, 100);
                    addLandmark("afula", "jezreel_valley", "Jezreel Valley Lookout",
                            "Panoramic view over the fertile Jezreel Valley.", 32.6072, 35.2818, 75);
                    addLandmark("afula", "mount_tabor", "Mount Tabor",
                            "Site of the Transfiguration of Jesus, with a stunning basilica.", 32.6873, 35.3932, 100);
                });
    }

    // ==================== REHOVOT ====================
    private static void seedRehovot() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Rehovot");
        city.put("country", "Israel");
        city.put("totalLandmarks", 3);
        city.put("coverImage", "");
        db.collection("cities").document("rehovot").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("rehovot", "weizmann_institute", "Weizmann Institute of Science",
                            "World-renowned scientific research institute.", 31.9040, 34.8055, 100);
                    addLandmark("rehovot", "herzl_garden", "Herzl Garden",
                            "Beautiful botanical garden in the city center.", 31.8948, 34.8088, 75);
                    addLandmark("rehovot", "rehovot_museum", "Ayalon Institute (Bullet Factory)",
                            "Secret underground bullet factory used by the Haganah.", 31.8944, 34.8133, 100);
                });
    }

    // ==================== PETAH TIKVA ====================
    private static void seedPetahTikva() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Petah Tikva");
        city.put("country", "Israel");
        city.put("totalLandmarks", 3);
        city.put("coverImage", "");
        db.collection("cities").document("petah_tikva").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("petah_tikva", "park_hadarim", "Park HaYarkon (Petah Tikva)",
                            "Large park near the Yarkon River.", 32.0841, 34.8854, 75);
                    addLandmark("petah_tikva", "old_cemetery_pt", "Petah Tikva Old Cemetery",
                            "Cemetery of the pioneers who founded the first modern Hebrew city.", 32.0869, 34.8826, 75);
                    addLandmark("petah_tikva", "yad_labanim_museum", "Yad LaBanim Memorial Museum",
                            "Memorial and museum honoring fallen IDF soldiers.", 32.0897, 34.8823, 75);
                });
    }

    // ==================== RISHON LEZION ====================
    private static void seedRishonLezion() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Rishon LeZion");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("rishon_lezion").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("rishon_lezion", "rishon_founders_well", "Founders' Well",
                            "The first artesian well dug by the city's founders in 1882.", 31.9728, 34.8013, 100);
                    addLandmark("rishon_lezion", "carmel_winery", "Carmel Winery",
                            "Israel's oldest and largest winery, founded in 1890.", 31.9672, 34.7992, 75);
                    addLandmark("rishon_lezion", "gan_ha_atzmaut", "Gan HaAtzmaut Park",
                            "The main park of Rishon LeZion.", 31.9745, 34.8028, 50);
                    addLandmark("rishon_lezion", "beit_haaliyah", "Beit HaAliyah Museum",
                            "Museum telling the story of Jewish immigration waves.", 31.9703, 34.8020, 75);
                });
    }

    // ==================== ASHDOD ====================
    private static void seedAshdod() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Ashdod");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("ashdod").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("ashdod", "ashdod_beach", "Ashdod Beach",
                            "Beautiful Mediterranean beach with a boardwalk.", 31.8038, 34.6374, 75);
                    addLandmark("ashdod", "ashdod_yam", "Ashdod-Yam (Ancient Ashdod)",
                            "Ruins of ancient Philistine Ashdod.", 31.8048, 34.6414, 100);
                    addLandmark("ashdod", "ashdod_museum", "Ashdod Art Museum",
                            "Contemporary art museum in the city.", 31.8098, 34.6467, 75);
                    addLandmark("ashdod", "ashdod_port", "Ashdod Port",
                            "The largest port in Israel by cargo volume.", 31.8284, 34.6485, 75);
                });
    }

    // ==================== ASHKELON ====================
    private static void seedAshkelon() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Ashkelon");
        city.put("country", "Israel");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");
        db.collection("cities").document("ashkelon").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("ashkelon", "ashkelon_national_park", "Ashkelon National Park",
                            "Ancient Canaanite, Philistine and Roman ruins by the sea.", 31.6658, 34.5547, 100);
                    addLandmark("ashkelon", "ashkelon_beach", "Ashkelon Beach",
                            "Beautiful Mediterranean beach near ancient ruins.", 31.6577, 34.5454, 75);
                    addLandmark("ashkelon", "cactus_farm", "African Cactus Farm",
                            "Unique cactus garden with species from around the world.", 31.6749, 34.5798, 50);
                    addLandmark("ashkelon", "barzilai_hospital", "Barzilai Medical Center",
                            "Major hospital and a landmark of modern Ashkelon.", 31.6598, 34.5728, 50);
                    addLandmark("ashkelon", "delilah_beach", "Delilah Beach",
                            "Named after the biblical Delilah from Samson's story.", 31.6601, 34.5453, 75);
                });
    }

    // ==================== MODIIN ====================
    private static void seedModiin() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Modi'in-Maccabim-Re'ut");
        city.put("country", "Israel");
        city.put("totalLandmarks", 3);
        city.put("coverImage", "");
        db.collection("cities").document("modiin").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("modiin", "hasmonean_tombs", "Hasmonean Tombs",
                            "Traditional burial site of the Maccabee family.", 31.9126, 34.9978, 100);
                    addLandmark("modiin", "modi_in_park", "Anava National Park",
                            "Beautiful nature park near Modi'in.", 31.9097, 35.0163, 75);
                    addLandmark("modiin", "modiin_amphitheater", "Modi'in Amphitheater",
                            "Open-air amphitheater used for performances.", 31.8969, 35.0107, 75);
                });
    }

    // ==================== CAESAREA ====================
    private static void seedCaesarea() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Caesarea");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("caesarea").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("caesarea", "caesarea_amphitheater", "Roman Amphitheater",
                            "Spectacular 2,000-year-old Roman amphitheater still used for concerts.", 32.4972, 34.8918, 100);
                    addLandmark("caesarea", "crusader_city", "Crusader City",
                            "Ruins of the medieval Crusader city inside the national park.", 32.5003, 34.8912, 100);
                    addLandmark("caesarea", "herodian_harbor", "Herodian Harbor",
                            "Ancient harbor built by King Herod, a marvel of engineering.", 32.5008, 34.8905, 100);
                    addLandmark("caesarea", "roman_hippodrome", "Roman Hippodrome",
                            "Ancient chariot racing track on the Mediterranean shore.", 32.4951, 34.8927, 100);
                    addLandmark("caesarea", "caesarea_aqueduct", "Roman Aqueduct",
                            "Stunning 9-km Roman aqueduct running along the beach.", 32.5198, 34.9050, 100);
                    addLandmark("caesarea", "caesarea_port_modern", "Caesarea Harbor & Restaurants",
                            "Modern harbor area with restaurants and galleries.", 32.5000, 34.8910, 75);
                });
    }

    // ==================== BEIT SHEAN ====================
    private static void seedBeitShean() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Beit She'an");
        city.put("country", "Israel");
        city.put("totalLandmarks", 4);
        city.put("coverImage", "");
        db.collection("cities").document("beit_shean").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("beit_shean", "beit_shean_national_park", "Beit She'an National Park",
                            "One of the best-preserved Roman-Byzantine cities in Israel.", 32.5017, 35.4989, 100);
                    addLandmark("beit_shean", "beit_shean_theater", "Roman Theater of Beit She'an",
                            "Magnificent 7,000-seat Roman theater from the 2nd century CE.", 32.5021, 35.4985, 100);
                    addLandmark("beit_shean", "tel_beit_shean", "Tel Beit She'an",
                            "18 layers of civilization from Neolithic to Ottoman times.", 32.5022, 35.4990, 100);
                    addLandmark("beit_shean", "hamat_gader", "Hamat Gader Hot Springs",
                            "Famous hot springs near the Jordan and Yarmouk rivers.", 32.6843, 35.6675, 100);
                });
    }

    // ==================== JORDAN VALLEY ====================
    private static void seedJordanValley() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Jordan Valley");
        city.put("country", "Israel/Palestine");
        city.put("totalLandmarks", 5);
        city.put("coverImage", "");
        db.collection("cities").document("jordan_valley").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("jordan_valley", "naharayim", "Naharayim (Peace Island)",
                            "Island at the confluence of the Jordan and Yarmouk rivers.", 32.6565, 35.5981, 100);
                    addLandmark("jordan_valley", "jordan_river_park", "Jordan River Park",
                            "Baptismal site on the Jordan River used by Christians worldwide.", 32.6609, 35.5858, 100);
                    addLandmark("jordan_valley", "beit_alpha", "Beit Alpha Synagogue",
                            "Ancient synagogue with a stunning mosaic zodiac floor.", 32.5398, 35.4283, 100);
                    addLandmark("jordan_valley", "sachne", "Gan HaShlosha (Sachne)",
                            "Beautiful natural spring pools with constant warm temperature.", 32.5314, 35.4339, 100);
                    addLandmark("jordan_valley", "belvoir_castle", "Belvoir Crusader Castle",
                            "Magnificent Crusader fortress overlooking the Jordan Valley.", 32.6021, 35.4962, 100);
                });
    }

    // ==================== GALILEE ====================
    private static void seedGalilee() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Galilee Region");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("galilee").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("galilee", "church_of_beatitudes", "Church of the Beatitudes",
                            "Where Jesus delivered the Sermon on the Mount.", 32.8817, 35.5583, 100);
                    addLandmark("galilee", "capernaum", "Capernaum (Jesus' Hometown)",
                            "Ancient fishing village — the home base of Jesus' ministry.", 32.8803, 35.5749, 100);
                    addLandmark("galilee", "tabgha", "Tabgha (Multiplication of Loaves)",
                            "Site of the miracle of the loaves and fishes.", 32.8768, 35.5581, 100);
                    addLandmark("galilee", "banias_spring", "Banias (Caesarea Philippi)",
                            "Natural spring, ancient temple ruins, and waterfalls.", 33.2491, 35.6936, 100);
                    addLandmark("galilee", "nimrod_fortress", "Nimrod Fortress",
                            "Largest surviving Crusader fortress in Israel.", 33.2460, 35.7101, 100);
                    addLandmark("galilee", "rosh_hanikra", "Rosh HaNikra Grottos",
                            "Spectacular sea grottos at the Lebanese border.", 33.0946, 35.1019, 100);
                });
    }

    // ==================== NEGEV DESERT ====================
    private static void seedNegev() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Negev Desert");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("negev").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("negev", "ramon_crater", "Ramon Crater (Makhtesh Ramon)",
                            "The world's largest erosion crater — a natural wonder.", 30.5667, 34.8167, 100);
                    addLandmark("negev", "mitzpe_ramon", "Mitzpe Ramon Observation Point",
                            "Stunning viewpoint over the Ramon Crater.", 30.6100, 34.8011, 100);
                    addLandmark("negev", "avdat", "Avdat (Oboda) Nabataean City",
                            "UNESCO Nabataean city on the Incense Route.", 30.7946, 34.7729, 100);
                    addLandmark("negev", "timna_park", "Timna Park",
                            "Ancient copper mines with stunning rock formations.", 29.7857, 34.9779, 100);
                    addLandmark("negev", "ben_gurion_tomb", "Ben-Gurion's Tomb",
                            "Burial site of Israel's first Prime Minister overlooking the Negev.", 30.8795, 34.7912, 100);
                    addLandmark("negev", "shivta", "Shivta Nabataean City",
                            "Perfectly preserved Nabataean town in the Negev desert.", 30.8802, 34.6277, 100);
                });
    }

    // ==================== GOLAN HEIGHTS ====================
    private static void seedGolanHeights() {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Golan Heights");
        city.put("country", "Israel");
        city.put("totalLandmarks", 6);
        city.put("coverImage", "");
        db.collection("cities").document("golan_heights").set(city)
                .addOnSuccessListener(u -> {
                    addLandmark("golan_heights", "mount_hermon", "Mount Hermon",
                            "The highest point in Israel with a ski resort.", 33.4151, 35.8576, 100);
                    addLandmark("golan_heights", "gamla", "Gamla Nature Reserve",
                            "Ancient Jewish fortress, Masada of the north, with griffon vultures.", 32.9006, 35.7444, 100);
                    addLandmark("golan_heights", "katzrin", "Katzrin Ancient Village",
                            "Reconstructed ancient Jewish village from the Talmudic period.", 32.9950, 35.6877, 100);
                    addLandmark("golan_heights", "quneitra_viewpoint", "Quneitra Viewpoint",
                            "Overlooks the abandoned Syrian town of Quneitra.", 33.1268, 35.8235, 75);
                    addLandmark("golan_heights", "bental_volcano", "Mount Bental Volcano",
                            "Extinct volcano with a Syrian bunker and panoramic views.", 33.1310, 35.7956, 100);
                    addLandmark("golan_heights", "majdal_shams", "Majdal Shams Village",
                            "Druze village at the foot of Mount Hermon.", 33.3879, 35.7686, 75);
                });
    }

    // ==================== FAKE USERS ====================
    private static void seedFakeUsers(final android.content.Context context) {
        createFakeUser(context, "user_mike", "explorer_mike", "mike@conquer.com", 1200, 5);
        createFakeUser(context, "user_jane", "jane_doe", "jane@conquer.com", 950, 4);
        createFakeUser(context, "user_leo", "nomad_leo", "leo@conquer.com", 600, 3);
        createFakeUser(context, "user_sarah", "wanderer_sarah", "sarah@conquer.com", 300, 2);
        createFakeUser(context, "user_john", "roamer_john", "john@conquer.com", 100, 1);
    }

    private static void createFakeUser(final android.content.Context context, String userId,
                                       String username, String email, int xp, int level) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) return; // don't overwrite existing users
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("email", email);
                    user.put("totalXP", xp);
                    user.put("level", level);
                    user.put("friends", new java.util.ArrayList<>());
                    db.collection("users").document(userId).set(user)
                            .addOnSuccessListener(u -> android.util.Log.d("SEEDER", "Fake user " + username + " seeded!"))
                            .addOnFailureListener(e -> android.util.Log.e("SEEDER", "Failed to seed " + username + ": " + e.getMessage()));
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
                .addOnSuccessListener(u -> android.util.Log.d("SEEDER", landmarkId + " added!"))
                .addOnFailureListener(e -> android.util.Log.e("SEEDER", landmarkId + " FAILED: " + e.getMessage()));
    }
}