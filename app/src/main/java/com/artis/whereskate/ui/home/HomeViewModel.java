package com.artis.whereskate.ui.home;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private ArrayList<MarkerObject> markerObjects;
    private ArrayList<MarkerObject> markerObjectsAll;
    private MutableLiveData<ArrayList<MarkerObject>> myMarkersForView = new MutableLiveData<>();
    private MutableLiveData<ArrayList<MarkerObject>> allMarkersForView = new MutableLiveData<>();
    private AppRepository appRepository;

    public HomeViewModel() {
        appRepository = new AppRepository();
        markerObjects = new ArrayList<>();
        markerObjectsAll = new ArrayList<>();
    }

    public void updateAllMarkers(DataSnapshot snapshot, View view) {
        markerObjects.clear();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        DataSnapshot dataSnapshot = snapshot.child(account.getId());
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            markerObjects.add(child.getValue(MarkerObject.class));
        }
        myMarkersForView.postValue(markerObjects);

        markerObjectsAll.clear();
        for (DataSnapshot data : snapshot.getChildren()){
            for (DataSnapshot parent : snapshot.getChildren()) {
                String userid = parent.getKey();
                DataSnapshot dataSnapshotAll = snapshot.child(userid);
                for (DataSnapshot child : dataSnapshotAll.getChildren()) {
                    markerObjectsAll.add(child.getValue(MarkerObject.class));
                }
            }
        }
        allMarkersForView.postValue(markerObjectsAll);

    }

    public MutableLiveData<ArrayList<MarkerObject>> getMyMarkersForView() {
        return myMarkersForView;
    }
    public MutableLiveData<ArrayList<MarkerObject>> getAllMarkersForView() {
        return allMarkersForView;
    }


}