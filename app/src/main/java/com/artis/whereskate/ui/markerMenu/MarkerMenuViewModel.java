package com.artis.whereskate.ui.markerMenu;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.AppRepository;
import com.artis.whereskate.model.MarkerObject;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class MarkerMenuViewModel extends ViewModel {
    private AppRepository appRepository;

    private String markerDescriptionString;
    private String markerNameString;
    private String photoUrl;

    private MarkerObject markerObject;

    public MarkerMenuViewModel() {
        appRepository = new AppRepository();
    }

    public void loadMarkerBuffer(MarkerObject markerObject) {
        this.markerObject = markerObject;
    }

    public void savePicture() {

    }

    public void saveMarker(String name, String description, View view, @Nullable Bitmap image) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(view.getContext());
        markerObject.userId = account.getId();
        markerObject.description = description;
        markerObject.userName = account.getDisplayName();
        markerObject.name = name;
        if (image != null) {
            StorageReference storageReference = appRepository.getFirebaseStorage().getReference("markerPhotos");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            storageReference.child(account.getId()).putBytes(data);
            UploadTask uploadTask = storageReference.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        markerObject.imageURL = downloadUri.toString();
                        appRepository.saveMarker(markerObject);
                    } else {
                    }
                }
            });
        }
        else
            {
                markerObject.imageURL = "https://images2.minutemediacdn.com/image/upload/c_crop,h_1600,w_2378,x_11,y_0/v1579707709/shape/mentalfloss/56093-gettyimages-1171368832.jpg?itok=x5t6Tht2";
                appRepository.saveMarker(markerObject);
        }

        Navigation.findNavController(view).navigate(R.id.action_nav_markerMenu_to_nav_map);
    }
}
