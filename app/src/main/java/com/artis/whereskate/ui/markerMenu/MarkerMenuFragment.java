package com.artis.whereskate.ui.markerMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.artis.whereskate.R;
import com.artis.whereskate.ui.home.HomeViewModel;


public class MarkerMenuFragment extends Fragment {

    private MarkerMenuViewModel markerMenuViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        markerMenuViewModel =
                new ViewModelProvider(this).get(MarkerMenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_marker_menu, container, false);
        return root;
    }
}
