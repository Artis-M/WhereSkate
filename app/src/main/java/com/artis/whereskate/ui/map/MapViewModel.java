package com.artis.whereskate.ui.map;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MapViewModel extends ViewModel {

    private GoogleMap mMap;
    private AppRepository appRepository;
    public ArrayList<MarkerObject> markerObjects;
    private MutableLiveData<ArrayList<MarkerObject>> markersForView = new MutableLiveData<>();
    public MapViewModel() {
        appRepository = new AppRepository();
        markerObjects = new ArrayList<>();
    }

    public void saveMarker(double latitude, double longitude, View view) {
        MarkerObject markerObject = new MarkerObject();

        markerObject.lat = latitude;
        markerObject.lang = longitude;

        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", markerObject);
        Navigation.findNavController(view).navigate(R.id.action_nav_map_to_nav_markerMenu, bundle);
    }
    public MutableLiveData<ArrayList<MarkerObject>> getMarkersForView(){
        return markersForView;
    }

    public void openMarkerDetails(MarkerObject markerObject, View view){
        Bundle bundle = new Bundle();
        bundle.putSerializable("marker", markerObject);
        Navigation.findNavController(view).navigate(R.id.action_nav_map_to_nav_markerInfo, bundle);
    }

    public void updateMarkers(DataSnapshot snapshot){
        markerObjects.clear();
        for (DataSnapshot data : snapshot.getChildren()){
            for (DataSnapshot parent : snapshot.getChildren()) {
                String userid = parent.getKey();
                DataSnapshot dataSnapshot = snapshot.child(userid);
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    markerObjects.add(child.getValue(MarkerObject.class));
                }
            }
        }
        markersForView.postValue(markerObjects);
    }
}