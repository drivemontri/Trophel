package com.example.bhurivatmontri.trophel;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailAttraction extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction);

        String keyOfAttraction = null;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            keyOfAttraction = null;
        } else {
            keyOfAttraction = extras.getString("keyOfAttraction");
        }

        Toolbar toolbarDetailAttr = (Toolbar) findViewById(R.id.toolbar_detail_attraction);
        setSupportActionBar(toolbarDetailAttr);
        getSupportActionBar().setTitle("Attraction detail");

        final ImageView coverDetailAttraction = (ImageView) findViewById(R.id.cover_detail_attraction);
        final TextView nameDetailAttraction = (TextView) findViewById(R.id.name_detail_attraction);
        final TextView detailDetailAttraction = (TextView) findViewById(R.id.detail_detail_attraction);

        /*String src_drawable = "att_"+nameOfAttraction.toLowerCase();
        int id_cover_attraction = getResources().getIdentifier(src_drawable,"drawable",getPackageName());
        int id_detail_attraction = getResources().getIdentifier(nameOfAttraction,"string",getPackageName());

        coverDetailAttraction.setImageResource(id_cover_attraction);
        nameDetailAttraction.setText(nameOfAttraction);
        detailDetailAttraction.setText(id_detail_attraction);*/

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();
        Log.d("onDataChange","bbnnbnbnb"+keyOfAttraction);
        mDatabase.child("attractions").child(keyOfAttraction).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange","hihihi"+dataSnapshot.getKey().toString());
                String name = dataSnapshot.child("name_Eng").getValue().toString();
                String info = dataSnapshot.child("info_Eng").getValue().toString();
                String uri_img = dataSnapshot.child("uri_img").getValue().toString();

                nameDetailAttraction.setText(name);
                detailDetailAttraction.setText(info);

                Picasso.with(getApplicationContext())
                        .load(uri_img)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(coverDetailAttraction);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //String url_img1 = "attractions/Northern/Chiang Mai/"+keyOfAttraction+"/img1.jpg";


        Button btn_back = (Button) findViewById(R.id.button_back_detail_attraction);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailAttraction.this.finish();
            }
        });
    }
}
