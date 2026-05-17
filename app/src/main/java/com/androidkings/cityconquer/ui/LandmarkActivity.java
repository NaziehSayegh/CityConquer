package com.androidkings.cityconquer.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.androidkings.cityconquer.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandmarkActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_LOCATION = 101;
    private static final double MAX_DISTANCE_METERS = 100.0;

    private TextView landmarkName, landmarkDescription, statusText;
    private ImageView landmarkImage;
    private Button conquestBtn;

    private String landmarkId, cityId, currentPhotoPath;
    private double landmarkLat, landmarkLng;
    private Uri photoUri;

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

        landmarkName = findViewById(R.id.landmark_name);
        landmarkDescription = findViewById(R.id.landmark_description);
        landmarkImage = findViewById(R.id.landmark_image);
        statusText = findViewById(R.id.status_text);
        conquestBtn = findViewById(R.id.conquest_btn);

        landmarkId = getIntent().getStringExtra("landmarkId");
        cityId = getIntent().getStringExtra("cityId");
        landmarkLat = getIntent().getDoubleExtra("latitude", 0);
        landmarkLng = getIntent().getDoubleExtra("longitude", 0);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");

        landmarkName.setText(name);
        landmarkDescription.setText(description);

        conquestBtn.setOnClickListener(v -> checkLocationAndConquer());
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

            if (results[0] <= MAX_DISTANCE_METERS) {
                statusText.setText("✅ You're here! Take a photo to conquer!");
                openCamera();
            } else {
                statusText.setText("❌ You're " + (int) results[0] + "m away. Get closer!");
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this,
                    "com.androidkings.cityconquer.fileprovider", photoFile);
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
                                markAsConquered(userId, uri.toString())))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload failed!", Toast.LENGTH_SHORT).show());
    }

    private void markAsConquered(String userId, String photoUrl) {
        db.collection("users").document(userId)
                .collection("conquests").document(cityId)
                .update("completedLandmarks", FieldValue.arrayUnion(landmarkId))
                .addOnSuccessListener(unused -> {
                    statusText.setText("🏆 Landmark Conquered!");
                    conquestBtn.setEnabled(false);
                    Toast.makeText(this, "Conquered! +100 XP", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkLocationAndConquer();
        }
    }
}