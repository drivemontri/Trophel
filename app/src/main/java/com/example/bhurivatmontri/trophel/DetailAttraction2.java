package com.example.bhurivatmontri.trophel;

import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.adapter.ImgPager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailAttraction2 extends AppCompatActivity implements OnMapReadyCallback {

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    ArrayList<String> imgUrls = new ArrayList<String>();

    GoogleMap mGoogleMap;
    Marker mMarker;
    ScrollView mScrollView;

    double latitude;
    double longitude;
    ArrayList<String> sub_Attrs_name = new ArrayList<String>();
    ArrayList<Double> sub_Attrs_latitude = new ArrayList<Double>();
    ArrayList<Double> sub_Attrs_longitude = new ArrayList<Double>();

    protected SharedPreferences settings;
    protected int select_language_position;
    protected String name_language = "";
    protected String info_language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction2);

        settings = this.getSharedPreferences("Trophel",MODE_WORLD_WRITEABLE);
        select_language_position = settings.getInt("select_language_position",-1);
        Log.d("onDataChange",""+select_language_position);
        switch (select_language_position){
            case -1: name_language = "name_Eng";info_language = "info_Eng";break;
            case 0:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 1:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 2:  name_language = "name_Thai";info_language = "info_Thai";break;
        }

        String keyOfAttraction = null;

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            keyOfAttraction = null;
        } else {
            keyOfAttraction = extras.getString("keyOfAttraction");
        }

        Toolbar toolbarDetailAttr = (Toolbar) findViewById(R.id.toolbar_detail_attraction2);
        setSupportActionBar(toolbarDetailAttr);
        getSupportActionBar().setTitle("Attraction detail");

        final TextView nameDetailAttraction = (TextView) findViewById(R.id.name_detail_attraction2);
        final TextView detailDetailAttraction = (TextView) findViewById(R.id.detail_detail_attraction2);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();
        Log.d("onDataChange", "keyOfAttraction" + keyOfAttraction);

        mDatabase.child("attractions").child(keyOfAttraction).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "" + dataSnapshot.getKey().toString());
                String name = dataSnapshot.child(name_language).getValue().toString();
                String info = "      "+dataSnapshot.child(info_language).getValue().toString();

                latitude = dataSnapshot.child("latitude").getValue(Double.class);
                longitude = dataSnapshot.child("longitude").getValue(Double.class);

                DataSnapshot uri_imgs = dataSnapshot.child("uri_img_group");
                int num_img = (int) uri_imgs.getChildrenCount();
                for (int i = 1; i <= num_img; i++) {
                    Log.d("onDataChange", "num of img:" + i);
                    Log.d("onDataChange", "" + uri_imgs.child("uri_img" + i).getValue().toString());
                    String eUri = uri_imgs.child("uri_img" + i).getValue().toString();
                    imgUrls.add(eUri);
                }

                DataSnapshot sub_Attrs = dataSnapshot.child("sub_Attrs");
                //int num_sub_Attrs = (int) sub_Attrs.getChildrenCount();
                for (DataSnapshot sub_Attr : sub_Attrs.getChildren()) {
                    String name_sub = sub_Attr.child("name_Eng").getValue().toString();
                    double latitue_sub = sub_Attr.child("latitude").getValue(Double.class);
                    double longitude_sub = sub_Attr.child("longitude").getValue(Double.class);
                    sub_Attrs_name.add(name_sub);
                    sub_Attrs_latitude.add(latitue_sub);
                    sub_Attrs_longitude.add(longitude_sub);
                }

                //String uri_img = dataSnapshot.child("uri_img").getValue().toString();
                nameDetailAttraction.setText(name);
                detailDetailAttraction.setText(info);
                ViewPager viewPager = findViewById(R.id.cover_detail_attraction2);
                ImgPager adapter = new ImgPager(getApplicationContext(), imgUrls);
                viewPager.setAdapter(adapter);



                if (mGoogleMap == null) {
                    ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetailAttraction2)).getMapAsync(DetailAttraction2.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button btn_back = (Button) findViewById(R.id.button_back_detail_attraction2);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailAttraction2.this.finish();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //final double latitude = 18.791118;
        //final double longitude = 98.960624;
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mScrollView = (ScrollView) findViewById(R.id.scrollView_detail_attraction2);

        ((CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDetailAttraction2))
                .setListener(new CustomMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        //mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("4 way junction").snippet("I am coding myProject").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        for (int i = 0 ; i < sub_Attrs_name.size() ; i++){
            mMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(sub_Attrs_latitude.get(i),sub_Attrs_longitude.get(i))).title(sub_Attrs_name.get(i)).snippet("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        }
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(16).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }
}
