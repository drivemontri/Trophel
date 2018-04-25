package com.example.bhurivatmontri.trophel.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.Camera;
import com.example.bhurivatmontri.trophel.DetailAttraction;
import com.example.bhurivatmontri.trophel.Home;
import com.example.bhurivatmontri.trophel.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_WORLD_WRITEABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, LocationListener {

    GoogleMap mGoogleMap;
    Marker mMarker;
    Marker mmMarker;
    MapView mMapView;
    View mView;
    GoogleApiClient googleApiClient;
    boolean chkFirstTime = true;
    int checktime = 20;
    boolean statusOk = false;

    HashMap<String,String> keyOfAttractionMap = new HashMap<>();
    HashMap<String,String> keyOfSubAttractionMap = new HashMap<>();
    HashMap<String,String> keyOfRegionMap = new HashMap<>();
    HashMap<String,String> keyOfUriImgAttractionMap = new HashMap<>();
    HashMap<String,Integer> keyOfCountAttractionMap = new HashMap<>();
    HashMap<String,Integer> countOfSubAttraction = new HashMap<>();
    HashMap<String,String> nameOfAttractionEngMap = new HashMap<>();
    HashMap<String,String> nameOfAttractionThaiMap = new HashMap<>();
    HashMap<String,String> nameOfSubAttractionMap = new HashMap<>();
    //HashMap<String,String> nameOfSubAttractionEngMap = new HashMap<>();
    //HashMap<String,String> nameOfSubAttractionThaiMap = new HashMap<>();
    HashMap<String,Double> latitudeOfSubAttractionMap = new HashMap<>();
    HashMap<String,Double> longitudeOfSubAttractionMap = new HashMap<>();
    HashMap<String,String> everOfSubAttractionMap = new HashMap<>();
    HashMap<String,String> markerIconOfSubAttractionMap = new HashMap<>();

    ArrayList<String> allKeySubAttraction;

    protected SharedPreferences settings;
    protected int select_language_position;
    protected String name_language = "";
    protected String info_language = "";

    double latitude;
    double longitude;

    public Map() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        settings = this.getActivity().getSharedPreferences("Trophel",MODE_WORLD_WRITEABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker arg0) {
                if(arg0.getTitle().equals("Location Current"))
                    Toast.makeText(getContext(),arg0.getTitle(),Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(getContext(),arg0.getId(),Toast.LENGTH_SHORT).show();
                    arg0.showInfoWindow();
                }
                return true;
            }
        });
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DetailAttraction.class);
                intent.putExtra("keyOfSubAttraction",keyOfSubAttractionMap.get(marker.getTitle()));
                intent.putExtra("keyOfAttraction",keyOfAttractionMap.get(marker.getTitle()));
                intent.putExtra("keyOfRegion",keyOfRegionMap.get(marker.getTitle()));
                intent.putExtra("keyOfUriImgAttraction",keyOfUriImgAttractionMap.get(marker.getTitle()));
                intent.putExtra("keyOfCountAttraction",keyOfCountAttractionMap.get(marker.getTitle()));
                intent.putExtra("countOfSubAttraction",countOfSubAttraction.get(marker.getTitle()));
                intent.putExtra("nameOfAttractionEng",nameOfAttractionEngMap.get(marker.getTitle()));
                intent.putExtra("nameOfAttractionThai",nameOfAttractionThaiMap.get(marker.getTitle()));
                startActivity(intent);
            }
        });
        buildClient();
        reMapAttraction();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        return;
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
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (chkFirstTime == true) {
            CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(45).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
            mmMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Location Current").snippet("I am coding myProject").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            chkFirstTime = false;
        }
        mmMarker.remove();
        mmMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Location Current").snippet("I am coding myProject").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        try {
            FirebaseDatabase.getInstance().getReference().child("users").child("uID").child("drive").child("attractions").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("statusOk","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.child("sub_Attrs").getChildren()){
                            everOfSubAttractionMap.put(snapshot2.getKey(),snapshot2.getKey());
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }
        Log.d("statusOk","ieie2");
        try {
            if(statusOk) {
                for(int i = 0; i < allKeySubAttraction.size() ; i++){
                    Log.d("statusOk",""+allKeySubAttraction.get(i));
                    Double Olat = Math.toRadians(latitude) - Math.toRadians(latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                    Double Olong = Math.toRadians(longitude) - Math.toRadians(longitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                    Double a = Math.pow((Math.sin(Olat / 2)), 2) + (Math.cos(Math.toRadians(latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)))) * Math.cos(Math.toRadians(latitude)) * Math.pow(Math.sin(Olong / 2), 2));
                    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    Double distance = 6371e3 * c / 1000.0;

                    if ( checktime%20==0){
                        mGoogleMap.clear();
                    }
                    Log.d("statusOk","MyEverSubAttraction : "+everOfSubAttractionMap.get(allKeySubAttraction.get(i)) + ",Size = "+everOfSubAttractionMap.size());
                    if(distance <= 6 ){
                        if(everOfSubAttractionMap.get(keyOfSubAttractionMap.get(allKeySubAttraction.get(i))) != null){
                            Log.d("statusOk","latitude of "+allKeySubAttraction.get(i) + " : " + latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                            Log.d("statusOk","longitude of "+allKeySubAttraction.get(i) + " : "+ longitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                            Log.d("statusOk","addMarker : "+allKeySubAttraction.get(i));
                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)), longitudeOfSubAttractionMap.get(allKeySubAttraction.get(i))))
                                    .icon(BitmapDescriptorFactory.fromAsset(markerIconOfSubAttractionMap.get(allKeySubAttraction.get(i))+"_checkmark"+".png"))
                                    .snippet("")
                                    .title(nameOfSubAttractionMap.get(allKeySubAttraction.get(i))));
                        }else{
                            Log.d("statusOk","latitude of "+allKeySubAttraction.get(i) + " : " + latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                            Log.d("statusOk","longitude of "+allKeySubAttraction.get(i) + " : "+ longitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)));
                            Log.d("statusOk","addMarker : "+allKeySubAttraction.get(i));
                            mMarker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitudeOfSubAttractionMap.get(allKeySubAttraction.get(i)), longitudeOfSubAttractionMap.get(allKeySubAttraction.get(i))))
                                    .icon(BitmapDescriptorFactory.fromAsset(markerIconOfSubAttractionMap.get(allKeySubAttraction.get(i))+".png"))
                                    .snippet("")
                                    .title(nameOfSubAttractionMap.get(allKeySubAttraction.get(i))));
                        }
                    }
                }
            }
        }catch (Exception e){

        }

        checktime++;
        if(checktime == 2000)
            checktime = 20;

    }

    public void reMapAttraction(){
        select_language_position = settings.getInt("select_language_position",-1);
        Log.d("onDataChange",""+select_language_position);
        switch (select_language_position){
            case -1: name_language = "name_Eng";info_language = "info_Eng";break;
            case 0:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 1:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 2:  name_language = "name_Thai";info_language = "info_Thai";break;
        }
        clearValue();
        FirebaseDatabase.getInstance().getReference("attractions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.child("sub_Attrs").getChildren()){
                        String keyname = snapshot2.child(name_language).getValue().toString();
                        keyOfAttractionMap.put(keyname,snapshot1.getKey());
                        keyOfSubAttractionMap.put(keyname,snapshot2.getKey());
                        keyOfRegionMap.put(keyname,snapshot1.child("region_Eng").getValue().toString());
                        keyOfCountAttractionMap.put(keyname,(int)snapshot1.child("sub_Attrs").getChildrenCount());
                        keyOfUriImgAttractionMap.put(keyname,snapshot1.child("uri_img").getValue().toString());
                        countOfSubAttraction.put(keyname,(int)snapshot1.child("sub_Attrs").getChildrenCount());
                        nameOfAttractionEngMap.put(keyname,snapshot1.child("name_Eng").getValue().toString());
                        nameOfAttractionThaiMap.put(keyname,snapshot1.child("name_Thai").getValue().toString());
                        nameOfSubAttractionMap.put(keyname,snapshot2.child(name_language).getValue().toString());
                        //nameOfSubAttractionEngMap.put(keyname,snapshot2.child("name_Eng").getValue().toString());
                        //nameOfSubAttractionThaiMap.put(keyname,snapshot2.child("name_Thai").getValue().toString());
                        latitudeOfSubAttractionMap.put(keyname,(double)snapshot2.child("latitude").getValue());
                        longitudeOfSubAttractionMap.put(keyname,(double)snapshot2.child("longitude").getValue());
                        markerIconOfSubAttractionMap.put(keyname,snapshot2.child("marker_icon").getValue().toString());
                        Log.d("statusOk","ieie");
                        allKeySubAttraction.add(keyname);
                    }
                }
                Log.d("statusOk","statusOk");
                Log.d("statusOk","countOfSub : "+keyOfSubAttractionMap.size());
                statusOk = true;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void clearValue(){
        statusOk = false;
        mGoogleMap.clear();
        keyOfAttractionMap.clear();
        keyOfSubAttractionMap.clear();
        keyOfRegionMap.clear();
        keyOfCountAttractionMap.clear();
        keyOfUriImgAttractionMap.clear();
        countOfSubAttraction.clear();
        nameOfAttractionEngMap.clear();
        nameOfAttractionThaiMap.clear();
        nameOfSubAttractionMap.clear();
        //nameOfSubAttractionEngMap.put(keyname,snapshot2.child("name_Eng").getValue().toString());
        //nameOfSubAttractionThaiMap.put(keyname,snapshot2.child("name_Thai").getValue().toString());
        latitudeOfSubAttractionMap.clear();
        longitudeOfSubAttractionMap.clear();
        markerIconOfSubAttractionMap.clear();
        allKeySubAttraction = new ArrayList<String>();
        everOfSubAttractionMap.clear();
    }

}
