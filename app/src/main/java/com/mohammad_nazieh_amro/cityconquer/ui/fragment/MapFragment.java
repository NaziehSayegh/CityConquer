package com.mohammad_nazieh_amro.cityconquer.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohammad_nazieh_amro.cityconquer.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                } else {
                    LatLng jerusalem = new LatLng(31.7767, 35.2345);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jerusalem, 13));
                }
            });
        } else {
            LatLng jerusalem = new LatLng(31.7767, 35.2345);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jerusalem, 13));
        }

        loadLandmarkMarkers();
    }

    private void loadLandmarkMarkers() {
        db.collection("cities")
                .get()
                .addOnSuccessListener(cities -> {
                    for (var cityDoc : cities) {
                        db.collection("cities")
                                .document(cityDoc.getId())
                                .collection("landmarks")
                                .get()
                                .addOnSuccessListener(landmarks -> {
                                    for (var landmarkDoc : landmarks) {
                                        Double lat = landmarkDoc.getDouble("latitude");
                                        Double lng = landmarkDoc.getDouble("longitude");
                                        String name = landmarkDoc.getString("name");
                                        if (lat != null && lng != null) {
                                            LatLng position = new LatLng(lat, lng);
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(position)
                                                    .title(name));
                                        }
                                    }
                                });
                    }
                });
    }
}