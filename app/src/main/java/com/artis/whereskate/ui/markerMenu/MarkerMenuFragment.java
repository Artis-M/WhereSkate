package com.artis.whereskate.ui.markerMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.MarkerObject;



public class MarkerMenuFragment extends Fragment {

    private MarkerMenuViewModel markerMenuViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        markerMenuViewModel =
                new ViewModelProvider(this).get(MarkerMenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_marker_menu, container, false);
        EditText descriptionText = root.findViewById(R.id.markerDescriptionText);
        EditText nameText = root.findViewById(R.id.markerNameText);
        Button saveButton = root.findViewById(R.id.saveMarkerButton);
        Bundle args = getArguments();
        MarkerObject markerObject = (MarkerObject) args.getSerializable("marker");
        markerMenuViewModel.loadMarkerBuffer(markerObject);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerMenuViewModel.saveMarker(nameText.getText().toString(), descriptionText.getText().toString(), root);
            }
        });
        return root;
    }
}
