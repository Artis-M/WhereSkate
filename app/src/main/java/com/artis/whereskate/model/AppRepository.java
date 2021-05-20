package com.artis.whereskate.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class AppRepository {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://whereskate-default-rtdb.europe-west1.firebasedatabase.app/").getReference("markers");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public void saveMarker(MarkerObject markerObject) {
        String key = databaseReference.child(markerObject.userId).push().getKey();
        markerObject.markerId = key;
        databaseReference.child(markerObject.userId).child(key).setValue(markerObject);
    }

    public DatabaseReference getDatabaseReference() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://whereskate-default-rtdb.europe-west1.firebasedatabase.app/").getReference("markers");
        return databaseReference;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public void deleteMarker(String user, String marker) {
        databaseReference.child(user).child(marker).removeValue();
    }

    public void editMarker(MarkerObject markerObject) {
        databaseReference.child(markerObject.userId).child(markerObject.markerId).setValue(markerObject);
    }
}
