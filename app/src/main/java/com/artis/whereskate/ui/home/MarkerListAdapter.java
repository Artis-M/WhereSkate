package com.artis.whereskate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.artis.whereskate.R;
import com.artis.whereskate.model.MarkerObject;

import java.util.ArrayList;

public class MarkerListAdapter extends RecyclerView.Adapter<MarkerListAdapter.ViewHolder> {

    ArrayList<MarkerObject> markerObjects;

    public MarkerListAdapter(ArrayList<MarkerObject> markerObjects, String userId){
        this.markerObjects = markerObjects;
    }

    @NonNull
    @Override
    public MarkerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.marker_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkerListAdapter.ViewHolder holder, int position) {

        holder.markerObject = markerObjects.get(position);
        holder.title.setText(markerObjects.get(position).name);
    }

    @Override
    public int getItemCount() {
        return markerObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button title;
        MarkerObject markerObject = new MarkerObject();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.marker_list_item_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("marker", markerObject);
                    bundle.putBoolean("isFromHome", true);
                    Navigation.findNavController(itemView).navigate(R.id.action_nav_home_to_nav_markerInfo, bundle);
                }
            });
        }
    }
}
