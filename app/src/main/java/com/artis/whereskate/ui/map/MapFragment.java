package com.artis.whereskate.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MapFragment extends Fragment {

    private static final int PERMISSIONS_FINE_LOCATION = 1337;
    private MapViewModel mapViewModel;
    private AppRepository appRepository;
    private Button fabAdd;
    private Button fabSave;
    private Button viewMoreDetailsButton;
    private MarkerObject inFocusMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private Boolean isCreatingNew;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);

        isCreatingNew = false;

        fabAdd = root.findViewById(R.id.addMarkerButton);
        fabSave = root.findViewById(R.id.saveMakerButton);
        fabSave.setVisibility(View.GONE);

        viewMoreDetailsButton = root.findViewById(R.id.viewMoreInfoButton);
        viewMoreDetailsButton.setVisibility(View.GONE);
        inFocusMarker = new MarkerObject();

        appRepository = new AppRepository();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(root.getContext());



        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if(ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED){
                    fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) root.getContext(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                            }

                        }
                    });
                }
                else{
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                        }, PERMISSIONS_FINE_LOCATION);
                    }
                    fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) root.getContext(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));
                            }
                        }
                    });
                }



                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        inFocusMarker = (MarkerObject) marker.getTag();
                        if(!isCreatingNew){
                            viewMoreDetailsButton.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        viewMoreDetailsButton.setVisibility(View.GONE);
                    }
                });
                viewMoreDetailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapViewModel.openMarkerDetails(inFocusMarker, root);
                    }
                });
                appRepository.getDatabaseReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mapViewModel.updateMarkers(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                appRepository.getDatabaseReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mapViewModel.updateMarkers(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mapViewModel.getMarkersForView().observe(getViewLifecycleOwner(), new Observer<ArrayList<MarkerObject>>() {
                    @Override
                    public void onChanged(ArrayList<MarkerObject> markerObjects) {
                        googleMap.clear();
                        for (int i = 0; i < markerObjects.size(); i++) {
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(markerObjects.get(i).lat, markerObjects.get(i).lang))
                                    .draggable(false)
                                    .title(markerObjects.get(i).name)
                                    .snippet(markerObjects.get(i).description)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
                            );
                            marker.setTag(markerObjects.get(i));
                        }
                    }
                });

                // without this the draggable marker does not update it's location
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }
                });

                fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isCreatingNew = true;
                        Marker newMarker = googleMap.addMarker(new MarkerOptions()
                                .position(googleMap.getCameraPosition().target)
                                .draggable(true)
                                .title("Drag the marker to the location.")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        fabSave.setVisibility(View.VISIBLE);
                        fabAdd.setVisibility(View.GONE);
                        fabSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newMarker.setDraggable(false);
                                double lat = newMarker.getPosition().latitude;
                                double lon = newMarker.getPosition().longitude;
                                mapViewModel.saveMarker(lat, lon, view);
                                isCreatingNew = false;
                                fabSave.setVisibility(View.GONE);
                                fabAdd.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }
        });
        return root;
    }

    ;

}
