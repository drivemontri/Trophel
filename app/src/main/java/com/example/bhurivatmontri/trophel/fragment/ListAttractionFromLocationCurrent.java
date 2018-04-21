package com.example.bhurivatmontri.trophel.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.Script;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bhurivatmontri.trophel.R;
import com.example.bhurivatmontri.trophel.adapter.EndangeredItem;
import com.example.bhurivatmontri.trophel.adapter.GridAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAttractionFromLocationCurrent extends Fragment implements GoogleApiClient.ConnectionCallbacks, LocationListener {

    GoogleApiClient googleApiClient;
    boolean chk_one_time = true;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> uriImg = new ArrayList<>();
    ArrayList<String> keyName = new ArrayList<>();
    ArrayList<EndangeredItem> listAttractions = new ArrayList<>();

    public ListAttractionFromLocationCurrent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_attraction_from_location_current, container, false);

        buildClient();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ListAttractionFromLocationCurrent);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager  = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GridAdapter(getActivity(),listAttractions);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void buildClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(getActivity(), "Map Connect!!", Toast.LENGTH_SHORT);
        LocationRequest lr = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, lr, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        //String provider = location.getProvider();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        //double altitude = location.getAltitude();
        //float accuracy = location.getAccuracy();
        //float bearing = location.getBearing();
        //float speed = location.getSpeed();
        //long time = location.getTime();
        if(chk_one_time){
            initDatabase(latitude,longitude);
        }
        chk_one_time = false;
    }

    private void initDataset() {
        int i = 0;
        Log.d("onDataChange","555555555555555555555555555555555555555555");
        for (String addName : name) {
            listAttractions.add(new EndangeredItem(addName,uriImg.get(i),keyName.get(i))) ;
            i++;
        }
    }

    private void initDatabase(final double latitude,final double longitude){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        mDatabase.child("attractions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item_attrs : dataSnapshot.getChildren()){
                    Double Olat = Math.toRadians(latitude) - Math.toRadians(item_attrs.child("latitude").getValue(Double.class));
                    Double Olong = Math.toRadians(longitude) - Math.toRadians(item_attrs.child("longitude").getValue(Double.class));
                    Double a = Math.pow((Math.sin(Olat / 2)), 2) + (Math.cos(Math.toRadians(item_attrs.child("latitude").getValue(Double.class))) * Math.cos(Math.toRadians(latitude)) * Math.pow(Math.sin(Olong / 2), 2));
                    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    Double distance = 6371e3 * c / 1000.0;
                    if(distance <= 2){
                        name.add(item_attrs.child("name_Eng").getValue().toString());
                        uriImg.add(item_attrs.child("uri_img").getValue().toString());
                        keyName.add(item_attrs.getKey().toString());
                    }
                }
                initDataset();
                mAdapter = new GridAdapter(getActivity(),listAttractions);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
