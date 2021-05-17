package com.artis.whereskate.model;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppRepository {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://whereskate-default-rtdb.europe-west1.firebasedatabase.app/").getReference("markers");

    public void saveMarker(MarkerObject markerObject) {
        databaseReference.child(markerObject.userId).child(markerObject.name).setValue(markerObject);
    }

    public ArrayList<MarkerObject> getMarkersFromDB(){
        ArrayList<MarkerObject> markerObjects = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                Log.println(Log.ASSERT, "HElp me", "Getting stuff from db");
                for (DataSnapshot child : snapshot.getChildren()) {
                    markerObjects.add(child.getValue(MarkerObject.class));
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return markerObjects;
    }

    public DatabaseReference getDatabaseReference() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://whereskate-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        return databaseReference;
    }
}
