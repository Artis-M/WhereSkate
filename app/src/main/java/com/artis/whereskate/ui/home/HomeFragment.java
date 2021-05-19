package com.artis.whereskate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AppRepository appRepository;
    RecyclerView recyclerView;
    private boolean isAll;
    private Button toggleButton;
    private TextView username;
    private ImageView profilePic;
    private TextView markerCount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        toggleButton = root.findViewById(R.id.toggleListButton);
        username = root.findViewById(R.id.userNameHome);
        profilePic = root.findViewById(R.id.homeProfileImage);
        markerCount = root.findViewById(R.id.totalLocationsText);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());

        Glide.with(root).load(account.getPhotoUrl()).circleCrop().into(profilePic);
        username.setText(account.getDisplayName());

        isAll = false;
        appRepository = new AppRepository();
        appRepository.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                homeViewModel.updateAllMarkers(snapshot, root);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = root.findViewById(R.id.marker_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.hasFixedSize();


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAll) {
                    toggleButton.setText("View All");
                    MarkerListAdapter markerListAdapter = new MarkerListAdapter(homeViewModel.getMyMarkersForView().getValue(), account.getId());
                    recyclerView.setAdapter(markerListAdapter);
                    isAll = false;
                } else {
                    toggleButton.setText("View Mine");
                    MarkerListAdapter markerListAdapter = new MarkerListAdapter(homeViewModel.getAllMarkersForView().getValue(), account.getId());
                    recyclerView.setAdapter(markerListAdapter);
                    isAll = true;
                }
            }
        });

        homeViewModel.getMyMarkersForView().observe(getViewLifecycleOwner(), new Observer<ArrayList<MarkerObject>>() {
            @Override
            public void onChanged(ArrayList<MarkerObject> markerObjects) {
                if (isAll) {
                    MarkerListAdapter markerListAdapter = new MarkerListAdapter(homeViewModel.getAllMarkersForView().getValue(), account.getId());
                    recyclerView.setAdapter(markerListAdapter);
                }
                if (!isAll) {
                    MarkerListAdapter markerListAdapter = new MarkerListAdapter(homeViewModel.getMyMarkersForView().getValue(), account.getId());
                    recyclerView.setAdapter(markerListAdapter);
                }
                markerCount.setText("Total Locations Shared: " + homeViewModel.getMyMarkersForView().getValue().size());
            }
        });
        return root;
    }
}