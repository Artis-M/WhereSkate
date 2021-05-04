package com.artis.whereskate.ui.map;


import androidx.lifecycle.ViewModel;

import androidx.navigation.Navigation;

import android.view.View;

import com.artis.whereskate.R;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.Marker;


public class MapViewModel extends ViewModel {

    private GoogleMap mMap;

    public MapViewModel() {

    }

    public void saveMarker(Marker marker, View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_map_to_nav_markerMenu);
    }
}