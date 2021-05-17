package com.artis.whereskate.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


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
                mapViewModel.updateMarkers();
                mapViewModel.getMarkersForView().observe(getViewLifecycleOwner(), new Observer<ArrayList<MarkerObject>>() {
                    @Override
                    public void onChanged(ArrayList<MarkerObject> markerObjects) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), markerObjects.toString(), Toast.LENGTH_LONG);
                        toast.show();
                        for(int i = 0; i < markerObjects.size(); i++){
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                             .position(new LatLng(markerObjects.get(i).lat, markerObjects.get(i).lang))
                             .draggable(false)
                            .title(markerObjects.get(i).name)
                    );
                        }
                    }
                });
                fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Marker newMarker = googleMap.addMarker(new MarkerOptions()
                                .position(googleMap.getCameraPosition().target)
                                .draggable(true)
                                .title("Drag the marker to the location.")
                        );
                        double lat = newMarker.getPosition().latitude;
                        double lon = newMarker.getPosition().longitude;
                        fabSave.setVisibility(View.VISIBLE);
                        fabSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                newMarker.setDraggable(false);
                                mapViewModel.saveMarker(lat, lon, view);
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
