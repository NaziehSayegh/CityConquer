package com.mohammad_nazieh_amro.cityconquer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;

public class LocationTrackingService extends Service {

    private static final String CHANNEL_ID = "CityConquerLocation";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createNotificationChannel();
        startForeground(1, buildNotification());
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 60000)
                .setMinUpdateIntervalMillis(30000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    checkCityEntry(location);
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void checkCityEntry(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        if (lat >= 31.7 && lat <= 31.85 && lng >= 35.1 && lng <= 35.3) {
            sendCityNotification("Jerusalem");
            checkNearbyLandmarks(location, "jerusalem");
        }

        if (lat >= 31.9 && lat <= 32.2 && lng >= 34.7 && lng <= 34.9) {
            sendCityNotification("Tel Aviv");
            checkNearbyLandmarks(location, "tel_aviv");
        }
    }

    private void checkNearbyLandmarks(Location userLocation, String cityId) {
        FirebaseFirestore.getInstance()
                .collection("cities").document(cityId)
                .collection("landmarks")
                .get()
                .addOnSuccessListener(landmarks -> {
                    for (var doc : landmarks) {
                        Double landmarkLat = doc.getDouble("latitude");
                        Double landmarkLng = doc.getDouble("longitude");
                        String landmarkName = doc.getString("name");

                        if (landmarkLat != null && landmarkLng != null) {
                            float[] results = new float[1];
                            Location.distanceBetween(
                                    userLocation.getLatitude(),
                                    userLocation.getLongitude(),
                                    landmarkLat, landmarkLng, results);

                            if (results[0] <= 200) {
                                sendLandmarkNotification(landmarkName, (int) results[0]);
                            }
                        }
                    }
                });
    }

    private void sendLandmarkNotification(String landmarkName, int distance) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("📍 " + landmarkName + " nearby!")
                .setContentText("You are " + distance + "m away. Go conquer it! 🏆")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        nm.notify((int) System.currentTimeMillis(), notification);
    }

    private void sendCityNotification(String cityName) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("You're in " + cityName + "! 🏛️")
                .setContentText("Landmarks nearby to conquer!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        nm.notify(2, notification);
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CityConquer")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "Location Tracking",
                NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}