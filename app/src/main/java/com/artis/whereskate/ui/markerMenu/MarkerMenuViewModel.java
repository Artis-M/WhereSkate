package com.artis.whereskate.ui.markerMenu;

import android.database.Observable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.artis.whereskate.MainActivity;
import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MarkerMenuViewModel extends ViewModel {
 private AppRepository appRepository;


private MutableLiveData<ArrayList<MarkerObject>> markers = new MutableLiveData<>();

    private String markerDescriptionString;
    private String markerNameString;

    private MarkerObject markerObject;
    public MarkerMenuViewModel() {
        appRepository = new AppRepository();
    }

    public MutableLiveData<ArrayList<MarkerObject>>  getMarkers() {
        return markers;
    }

    public void loadMarkerBuffer(MarkerObject markerObject){
        this.markerObject = markerObject;
    }

    public void saveMarker(String name, String description, View view) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        markerObject.userId = account.getId();
        markerObject.description = description;
        markerObject.name = name;
        appRepository.saveMarker(markerObject);
        Navigation.findNavController(view).navigate(R.id.action_nav_markerMenu_to_nav_map);
    }
}
