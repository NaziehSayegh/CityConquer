package com.mohammad_nazieh_amro.cityconquer.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohammad_nazieh_amro.cityconquer.R;
import com.mohammad_nazieh_amro.cityconquer.util.LevelSystem;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LandmarkActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_LOCATION = 101;
    private double maxDistanceMeters = 100.0;

    private TextView landmarkName, landmarkDescription, statusText;
    private ImageView landmarkImage;
    private Button conquestBtn;
    private TextView distanceText, radiusText, xpAmountText, distanceLabel;
    private FrameLayout xpPopupOverlay;
    private LinearLayout xpPopup;
    private TextView xpPopupAmount;
    private FrameLayout levelupPopupOverlay;
    private LinearLayout levelupPopup;
    private TextView levelupNewLevelText, levelupNextXpText;

    private String landmarkId, cityId, currentPhotoPath;
    private double landmarkLat, landmarkLng;
    private int landmarkXp = 100;
    private Uri photoUri;
    private int lastCheckedDistance = 0;

    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        maxDistanceMeters = getSharedPreferences("CityConquerPrefs", MODE_PRIVATE)
                .getInt("capture_radius", 100);

        landmarkName = findViewById(R.id.landmark_name);
        landmarkDescription = findViewById(R.id.landmark_description);
        landmarkImage = findViewById(R.id.landmark_image);
        statusText = findViewById(R.id.status_text);
        conquestBtn = findViewById(R.id.conquest_btn);
        distanceText = findViewById(R.id.distance_text);
        radiusText = findViewById(R.id.radius_text);
        xpAmountText = findViewById(R.id.xp_amount_text);
        distanceLabel = findViewById(R.id.distance_label);
        xpPopupOverlay = findViewById(R.id.xp_popup_overlay);
        xpPopup = findViewById(R.id.xp_popup);
        xpPopupAmount = findViewById(R.id.xp_popup_amount);
        levelupPopupOverlay = findViewById(R.id.levelup_popup_overlay);
        levelupPopup = findViewById(R.id.levelup_popup);
        levelupNewLevelText = findViewById(R.id.levelup_new_level_text);
        levelupNextXpText = findViewById(R.id.levelup_next_xp_text);

        landmarkId = getIntent().getStringExtra("landmarkId");
        cityId = getIntent().getStringExtra("cityId");
        landmarkLat = getIntent().getDoubleExtra("latitude", 0);
        landmarkLng = getIntent().getDoubleExtra("longitude", 0);
        landmarkXp = getIntent().getIntExtra("xp", 100);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");

        landmarkName.setText(name);
        landmarkDescription.setText(description);
        xpAmountText.setText("+" + landmarkXp);
        radiusText.setText((int) maxDistanceMeters + " m");

        // Check if already conquered
        checkIfAlreadyConquered();

        conquestBtn.setOnClickListener(v -> checkLocationAndConquer());

        // Tap XP popup to dismiss
        xpPopupOverlay.setOnClickListener(v -> dismissXpPopup());
        // Tap level-up popup to dismiss
        levelupPopupOverlay.setOnClickListener(v -> dismissLevelupPopup());
    }

    private void checkIfAlreadyConquered() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId)
                .collection("conquests").document(cityId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.get("completedLandmarks") != null) {
                        java.util.List<String> completed =
                                (java.util.List<String>) doc.get("completedLandmarks");
                        if (completed.contains(landmarkId)) {
                            statusText.setText("🏆 Already Conquered!");
                            conquestBtn.setEnabled(false);
                            conquestBtn.setText("Already Conquered ✅");

                            // Load the saved photo
                            if (doc.get("photos") != null) {
                                Map<String, String> photos = (Map<String, String>) doc.get("photos");
                                String savedPhotoUrl = photos.get(landmarkId);
                                if (savedPhotoUrl != null && !savedPhotoUrl.isEmpty()) {
                                    Glide.with(this).load(savedPhotoUrl).into(landmarkImage);
                                }
                            }

                            // Load the saved conquered distance
                            if (doc.get("conqueredDistances") != null) {
                                Map<String, Object> distances = (Map<String, Object>) doc.get("conqueredDistances");
                                Object savedDistance = distances.get(landmarkId);
                                if (savedDistance != null) {
                                    distanceLabel.setText("Conquered At");
                                    distanceText.setText(savedDistance.toString() + " m");
                                }
                            }
                        }
                    }
                });
    }

    private void checkLocationAndConquer() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(this, "Cannot get location. Try again!", Toast.LENGTH_SHORT).show();
                return;
            }

            float[] results = new float[1];
            Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                    landmarkLat, landmarkLng, results);

            int distance = (int) results[0];
            lastCheckedDistance = distance;
            distanceText.setText(distance + " m");

            if (results[0] <= maxDistanceMeters) {
                statusText.setText("✅ You're here! Take a photo to conquer!");
                openCamera();
            } else {
                statusText.setText("❌ You're " + distance + "m away. Get closer!");
            }
        });
    }

    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this,
                    "com.mohammad_nazieh_amro.cityconquer.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "LANDMARK_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Glide.with(this).load(photoUri).into(landmarkImage);
            uploadPhotoAndConquer();
        }
    }

    private void uploadPhotoAndConquer() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference ref = storage.getReference()
                .child("conquests/" + userId + "/" + landmarkId + ".jpg");

        ref.putFile(photoUri)
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(uri ->
                                markAsConquered(userId, uri.toString(), lastCheckedDistance))
                        .addOnFailureListener(e -> {
                            android.util.Log.w("CONQUEST", "Failed to get download URL, falling back to local photo reference", e);
                            markAsConquered(userId, photoUri.toString(), lastCheckedDistance);
                        }))
                .addOnFailureListener(e -> {
                    android.util.Log.w("CONQUEST", "Firebase Storage upload failed/disabled. Falling back to local reference.", e);
                    markAsConquered(userId, photoUri.toString(), lastCheckedDistance);
                });
    }

    private void markAsConquered(String userId, String photoUrl, int conqueredDistance) {
        // Mark landmark as conquered in Firestore
        db.collection("users").document(userId)
                .collection("conquests").document(cityId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("completedLandmarks", com.google.firebase.firestore.FieldValue.arrayUnion(landmarkId));
                        updates.put("photos." + landmarkId, photoUrl);
                        updates.put("conqueredDistances." + landmarkId, conqueredDistance);

                        db.collection("users").document(userId)
                                .collection("conquests").document(cityId)
                                .update(updates)
                                .addOnFailureListener(e -> {
                                    android.util.Log.e("CONQUEST", "Failed to update completed landmarks list", e);
                                    Toast.makeText(this, "Failed to update conquest list: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Map<String, Object> conquest = new HashMap<>();
                        conquest.put("completedLandmarks", Arrays.asList(landmarkId));
                        conquest.put("cityXP", 0);
                        Map<String, String> photos = new HashMap<>();
                        photos.put(landmarkId, photoUrl);
                        conquest.put("photos", photos);

                        Map<String, Integer> distances = new HashMap<>();
                        distances.put(landmarkId, conqueredDistance);
                        conquest.put("conqueredDistances", distances);

                        db.collection("users").document(userId)
                                .collection("conquests").document(cityId)
                                .set(conquest)
                                .addOnFailureListener(e -> {
                                    android.util.Log.e("CONQUEST", "Failed to set first completed landmark doc", e);
                                    Toast.makeText(this, "Failed to create conquest entry: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("CONQUEST", "Failed to check existing conquest doc", e);
                    Toast.makeText(this, "Error checking conquest state: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        // Update user total XP, check level-up, and show in-app popup
        String uid = userId;
        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {
                    int currentXP = userDoc.getLong("totalXP") != null
                            ? userDoc.getLong("totalXP").intValue() : 0;
                    int currentLevel = userDoc.getLong("level") != null
                            ? userDoc.getLong("level").intValue() : 1;

                    int newXP = currentXP + landmarkXp;
                    int newLevel = LevelSystem.getLevelForXP(newXP);

                    Map<String, Object> xpUpdate = new HashMap<>();
                    xpUpdate.put("totalXP", newXP);
                    if (newLevel > currentLevel) {
                        xpUpdate.put("level", newLevel);
                    }

                    db.collection("users").document(uid)
                            .update(xpUpdate)
                            .addOnSuccessListener(unused -> {
                                statusText.setText("🏆 Landmark Conquered!");
                                conquestBtn.setEnabled(false);
                                conquestBtn.setText("Already Conquered ✅");

                                if (newLevel > currentLevel) {
                                    // Level up! Show level-up popup first, then XP popup
                                    showLevelupPopup(newLevel, LevelSystem.getXPToNextLevel(newXP));
                                } else {
                                    showXpPopup(landmarkXp);
                                }
                            })
                            .addOnFailureListener(e -> {
                                android.util.Log.e("CONQUEST", "Failed to update XP/level", e);
                                Toast.makeText(this, "Failed to award XP: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("CONQUEST", "Failed to read user XP for level check", e);
                    // Fallback: still increment XP even if level check failed
                    db.collection("users").document(uid)
                            .update("totalXP", com.google.firebase.firestore.FieldValue.increment(landmarkXp))
                            .addOnSuccessListener(u -> showXpPopup(landmarkXp));
                });
    }

    /** Show animated in-app XP celebration popup */
    private void showXpPopup(int xp) {
        xpPopupAmount.setText("+" + xp + " XP");
        xpPopupOverlay.setVisibility(View.VISIBLE);

        // Pop in animation
        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.xp_popup_in);
        xpPopup.startAnimation(inAnim);

        // Auto-dismiss after 2.5 seconds
        new Handler().postDelayed(this::dismissXpPopup, 2500);
    }

    private void dismissXpPopup() {
        if (xpPopupOverlay.getVisibility() == View.VISIBLE) {
            Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.xp_popup_out);
            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation a) {}
                @Override public void onAnimationRepeat(Animation a) {}
                @Override public void onAnimationEnd(Animation a) {
                    xpPopupOverlay.setVisibility(View.GONE);
                }
            });
            xpPopup.startAnimation(outAnim);
        }
    }

    /** Show the level-up celebration popup, then auto-show XP popup after it's dismissed */
    private void showLevelupPopup(int newLevel, int xpToNext) {
        levelupNewLevelText.setText("You are now Level " + newLevel + "! 🎉");
        if (xpToNext > 0) {
            levelupNextXpText.setText(xpToNext + " XP to next level");
        } else {
            levelupNextXpText.setText("You've reached the maximum level! 👑");
        }
        levelupPopupOverlay.setVisibility(View.VISIBLE);
        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.levelup_popup_in);
        levelupPopup.startAnimation(inAnim);

        // After 3s, dismiss level-up and show XP popup
        new Handler().postDelayed(() -> {
            dismissLevelupPopup();
            new Handler().postDelayed(() -> showXpPopup(landmarkXp), 200);
        }, 3000);
    }

    private void dismissLevelupPopup() {
        levelupPopupOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationAndConquer();
            } else {
                Toast.makeText(this, "Location permission is required to conquer landmarks!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to take a photo!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}