package com.artis.whereskate.ui.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.artis.whereskate.MapViewModel;
import com.artis.whereskate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);
        FloatingActionButton fabAdd = root.findViewById(R.id.addMarkerFab);
        FloatingActionButton fabSave = root.findViewById(R.id.saveMakerFab);
        fabSave.setVisibility(View.GONE);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Marker newMarker = googleMap.addMarker(new MarkerOptions()
                                .position(googleMap.getCameraPosition().target)
                                .draggable(true)
                                .title("Drag the marker to the location.")
                        );
                        fabSave.setVisibility(View.VISIBLE);
                        fabSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newMarker.setDraggable(false);
                                fabSave.setVisibility(View.GONE);
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
