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
import com.mohammad_nazieh_amro.cityconquer.model.Landmark;

public class LocationTrackingService extends Service {

    private static final String CHANNEL_FOREGROUND = "CityConquerForeground";
    private static final String CHANNEL_ALERTS = "CityConquerAlerts";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private final java.util.List<Landmark> cachedLandmarks = new java.util.ArrayList<>();
    private String lastCityNotificationName = null;
    private final java.util.Set<String> notifiedLandmarks = new java.util.HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createNotificationChannel();
        startForeground(1, buildNotification());
        
        // Cache all landmarks from Firestore once to save reads
        cacheAllLandmarks();
        
        startLocationUpdates();
    }

    private void cacheAllLandmarks() {
        FirebaseFirestore.getInstance().collection("cities")
                .get()
                .addOnSuccessListener(cities -> {
                    for (var cityDoc : cities) {
                        String cityId = cityDoc.getId();
                        FirebaseFirestore.getInstance().collection("cities")
                                .document(cityId).collection("landmarks")
                                .get()
                                .addOnSuccessListener(landmarks -> {
                                    for (var landmarkDoc : landmarks) {
                                        Double lat = landmarkDoc.getDouble("latitude");
                                        Double lng = landmarkDoc.getDouble("longitude");
                                        String name = landmarkDoc.getString("name");
                                        Long xp = landmarkDoc.getLong("xp");
                                        
                                        if (lat != null && lng != null && name != null) {
                                            Landmark landmark = new Landmark();
                                            landmark.setId(landmarkDoc.getId());
                                            landmark.setName(name);
                                            landmark.setCityId(cityId);
                                            landmark.setLatitude(lat);
                                            landmark.setLongitude(lng);
                                            landmark.setXp(xp != null ? xp.intValue() : 100);
                                            
                                            synchronized (cachedLandmarks) {
                                                boolean exists = false;
                                                for (Landmark l : cachedLandmarks) {
                                                    if (l.getId().equals(landmark.getId())) {
                                                        exists = true;
                                                        break;
                                                    }
                                                }
                                                if (!exists) {
                                                    cachedLandmarks.add(landmark);
                                                }
                                            }
                                        }
                                    }
                                });
                    }
                });
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

        android.content.SharedPreferences prefs = getSharedPreferences("CityConquerPrefs", MODE_PRIVATE);
        boolean cityAlertsEnabled = prefs.getBoolean("city_alerts", true);
        boolean landmarkAlertsEnabled = prefs.getBoolean("landmark_alerts", true);
        int captureRadius = prefs.getInt("capture_radius", 100);

        String currentCityId = null;
        String currentCityName = null;

        // Check boundaries of cities
        if (lat >= 31.7 && lat <= 31.85 && lng >= 35.1 && lng <= 35.3) {
            currentCityId = "jerusalem";
            currentCityName = "Jerusalem";
        } else if (lat >= 31.9 && lat <= 32.2 && lng >= 34.7 && lng <= 34.9) {
            currentCityId = "tel_aviv";
            currentCityName = "Tel Aviv";
        } else if (lat >= 32.75 && lat <= 32.85 && lng >= 34.95 && lng <= 35.05) {
            currentCityId = "haifa";
            currentCityName = "Haifa";
        } else if (lat >= 32.65 && lat <= 32.75 && lng >= 35.25 && lng <= 35.35) {
            currentCityId = "nazareth";
            currentCityName = "Nazareth";
        } else if (lat >= 29.45 && lat <= 29.6 && lng >= 34.85 && lng <= 35.0) {
            currentCityId = "eilat";
            currentCityName = "Eilat";
        } else if (lat >= 31.25 && lat <= 31.8 && lng >= 35.3 && lng <= 35.5) {
            currentCityId = "dead_sea";
            currentCityName = "Dead Sea";
        } else if (lat >= 32.25 && lat <= 32.38 && lng >= 34.82 && lng <= 34.88) {
            currentCityId = "netanya";
            currentCityName = "Netanya";
        } else if (lat >= 31.2 && lat <= 31.3 && lng >= 34.75 && lng <= 34.85) {
            currentCityId = "beer_sheva";
            currentCityName = "Beer Sheva";
        } else if (lat >= 31.91 && lat <= 31.93 && lng >= 35.16 && lng <= 35.19) {
            currentCityId = "ramallah";
            currentCityName = "Ramallah";
        }

        if (currentCityId != null) {
            if (cityAlertsEnabled) {
                if (!currentCityName.equals(lastCityNotificationName)) {
                    sendCityNotification(currentCityName);
                    lastCityNotificationName = currentCityName;
                }
            } else {
                lastCityNotificationName = null;
            }
            
            if (landmarkAlertsEnabled) {
                checkNearbyLandmarks(location, currentCityId, captureRadius);
            }
        } else {
            lastCityNotificationName = null;
        }
    }

    private void checkNearbyLandmarks(Location userLocation, String cityId, int captureRadius) {
        synchronized (cachedLandmarks) {
            for (Landmark landmark : cachedLandmarks) {
                if (landmark.getCityId().equals(cityId)) {
                    float[] results = new float[1];
                    Location.distanceBetween(
                            userLocation.getLatitude(),
                            userLocation.getLongitude(),
                            landmark.getLatitude(),
                            landmark.getLongitude(),
                            results);

                    float distance = results[0];
                    String landmarkId = landmark.getId();
                    float alertRadius = captureRadius * 2; // Trigger nearby alerts when within 2x capture radius

                    if (distance <= alertRadius) {
                        if (!notifiedLandmarks.contains(landmarkId)) {
                            sendLandmarkNotification(landmark.getName(), (int) distance);
                            notifiedLandmarks.add(landmarkId);
                        }
                    } else if (distance > (alertRadius + 50)) {
                        notifiedLandmarks.remove(landmarkId);
                    }
                }
            }
        }
    }

    private void sendLandmarkNotification(String landmarkName, int distance) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ALERTS)
                .setContentTitle("📍 " + landmarkName + " nearby!")
                .setContentText("You are " + distance + "m away. Go conquer it! 🏆")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        // Use a static ID to overwrite previous landmark notifications instead of flooding the notification bar
        nm.notify(10, notification);
    }

    private void sendCityNotification(String cityName) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ALERTS)
                .setContentTitle("You're in " + cityName + "! 🏛️")
                .setContentText("Landmarks nearby to conquer!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        nm.notify(2, notification);
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_FOREGROUND)
                .setContentTitle("CityConquer")
                .setContentText("Tracking your location...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    private void createNotificationChannel() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            NotificationChannel fgChannel = new NotificationChannel(
                    CHANNEL_FOREGROUND, "Background Tracking",
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(fgChannel);

            NotificationChannel alertsChannel = new NotificationChannel(
                    CHANNEL_ALERTS, "Proximity Alerts",
                    NotificationManager.IMPORTANCE_HIGH);
            alertsChannel.enableLights(true);
            alertsChannel.enableVibration(true);
            manager.createNotificationChannel(alertsChannel);
        }
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