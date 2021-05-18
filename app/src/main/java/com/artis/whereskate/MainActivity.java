package com.artis.whereskate;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.artis.whereskate.ui.map.MapViewModel;
import com.artis.whereskate.ui.markerMenu.MarkerMenuViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AppRepository appRepository;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView profilePic;
    private TextView welcomeText;
    MarkerMenuViewModel markerMenuViewModel;
    MapViewModel mapViewModel;
    ArrayList<MarkerObject> markerObjects = new ArrayList<>();


    //Literally used the nav drawer template, no need to reinvent the wheel ¯\_(ツ)_/¯
    @Override
    protected void onCreate(Bundle savedInstanceState){

        appRepository = new AppRepository();
        markerMenuViewModel = new ViewModelProvider(this).get(MarkerMenuViewModel.class);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_map)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        View headerView = navigationView.getHeaderView(0);
        profilePic = headerView.findViewById(R.id.imageViewProfile);
        welcomeText = headerView.findViewById(R.id.welcomeText);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Glide.with(this).load(account.getPhotoUrl()).circleCrop().into(profilePic);
            welcomeText.setText("Welcome back " + account.getDisplayName());
        }


//        appRepository.getDatabaseReference().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int i = 0;
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    markerObjects.add(child.getValue(MarkerObject.class));
//                    i++;
//                }
//                mapViewModel.updateMarkers(markerObjects);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                mapViewModel.updateMarkers(markerObjects);
//            }
//        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}