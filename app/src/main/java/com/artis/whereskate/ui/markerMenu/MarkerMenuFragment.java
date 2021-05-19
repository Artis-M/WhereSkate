package com.artis.whereskate.ui.markerMenu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.artis.whereskate.R;
import com.artis.whereskate.model.MarkerObject;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MarkerMenuFragment extends Fragment {

    private MarkerMenuViewModel markerMenuViewModel;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView previewImage;
    private Bitmap imageCache;
    private View rootView;
    private TextView errorLabel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        markerMenuViewModel =
                new ViewModelProvider(this).get(MarkerMenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_marker_menu, container, false);
        rootView = root;
        EditText descriptionText = root.findViewById(R.id.markerDescriptionText);
        EditText nameText = root.findViewById(R.id.markerNameText);
        Button saveButton = root.findViewById(R.id.saveMarkerButton);
        Button addImageButton = root.findViewById(R.id.addPhotoButton);
        errorLabel = root.findViewById(R.id.missingNameLabel);

        previewImage = root.findViewById(R.id.photoPreview);
        Bundle args = getArguments();
        MarkerObject markerObject = (MarkerObject) args.getSerializable("marker");
        markerMenuViewModel.loadMarkerBuffer(markerObject);

        markerMenuViewModel.getWarningText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                errorLabel.setText(s);
            }
        });

        boolean isNew = args.getBoolean("isNew");
        boolean isFromHome = args.getBoolean("isFromHome");
        if(!isNew){
            Glide.with(rootView).load(markerObject.imageURL).into(previewImage);
            nameText.setText(markerObject.name);
            descriptionText.setText(markerObject.description);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerMenuViewModel.saveMarker(nameText.getText().toString(), descriptionText.getText().toString(), root, imageCache, isNew, isFromHome);
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        return root;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageCache = imageBitmap;
            Glide.with(rootView).load(imageBitmap).into(previewImage);
            Toast toast = Toast.makeText(rootView.getContext(), "Image Taken", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
