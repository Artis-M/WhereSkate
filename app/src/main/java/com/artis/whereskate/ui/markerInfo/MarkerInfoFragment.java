package com.artis.whereskate.ui.markerInfo;

import android.media.Image;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.artis.whereskate.R;
import com.artis.whereskate.model.MarkerObject;
import com.artis.whereskate.ui.markerMenu.MarkerMenuViewModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.w3c.dom.Text;

public class MarkerInfoFragment extends Fragment {
    private MarkerInfoViewModel markerInfoViewModel;
    private MarkerObject currentMarker;

    private TextView name;
    private TextView description;
    private ImageView locationPhoto;
    private TextView username;
    private Button deleteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        markerInfoViewModel =
                new ViewModelProvider(this).get(MarkerInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_marker_info, container, false);
        Bundle args = getArguments();
        currentMarker = (MarkerObject) args.getSerializable("marker");
        deleteButton = root.findViewById(R.id.markerInfoDelete);
        name = root.findViewById(R.id.markerInfoTitle);
        description = root.findViewById(R.id.markerInfoDescription);
        locationPhoto = root.findViewById(R.id.markerInfoImage);
        username = root.findViewById(R.id.markerInfoUserName);
        name.setText(currentMarker.name);
        description.setText(currentMarker.description);
        username.setText("Location added by: " + currentMarker.userName);
        Glide.with(root).load(currentMarker.imageURL).into(locationPhoto);
        deleteButton.setVisibility(View.GONE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());
        if(currentMarker.userId.equals(account.getId())){
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                markerInfoViewModel.deleteMarker(currentMarker.userId, currentMarker.markerId, root);
                }
            });
        }
        return root;
    }
}
