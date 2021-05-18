package com.artis.whereskate.ui.markerInfo;

import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;

public class MarkerInfoViewModel extends ViewModel {
    private AppRepository appRepository;

    public MarkerInfoViewModel(){
        appRepository = new AppRepository();
    }
    public void deleteMarker(String user, String marker, View view){
        appRepository.deleteMarker(user, marker);
        Navigation.findNavController(view).navigate(R.id.action_nav_markerInfo_to_nav_map);
    }
}
