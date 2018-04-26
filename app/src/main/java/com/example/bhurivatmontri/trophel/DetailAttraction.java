package com.example.bhurivatmontri.trophel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

    String keyOfSubAttraction = null;
    String keyOfAttraction = null;
    String keyOfRegion = null;
    String keyOfUriImgAttraction = null;
    String nameOfAttractionEng = null;
    String nameOfAttractionThai = null;
    String uriImageGameRotateSubAttraction = null;
    int keyOfCountAttraction ;
    int countOfSubAttraction ;

    protected SharedPreferences settings;
    protected int select_language_position;
    protected String name_language = "";
    protected String info_language = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction);

        settings = this.getSharedPreferences("Trophel",MODE_WORLD_WRITEABLE);
        select_language_position = settings.getInt("select_language_position",-1);
        Log.d("onDataChange",""+select_language_position);
        switch (select_language_position){
            case -1: name_language = "name_Eng";info_language = "info_Eng";break;
            case 0:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 1:  name_language = "name_Eng";info_language = "info_Eng";break;
            case 2:  name_language = "name_Thai";info_language = "info_Thai";break;
        }

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            keyOfSubAttraction = null;
            keyOfAttraction = null;
            keyOfRegion = null;
            keyOfUriImgAttraction = null;
        } else {
            keyOfSubAttraction = extras.getString("keyOfSubAttraction");
            keyOfAttraction = extras.getString("keyOfAttraction");
            keyOfRegion = extras.getString("keyOfRegion");
            keyOfUriImgAttraction = extras.getString("keyOfUriImgAttraction");
            keyOfCountAttraction = extras.getInt("keyOfCountAttraction");
            countOfSubAttraction = extras.getInt("countOfSubAttraction");
            nameOfAttractionEng = extras.getString("nameOfAttractionEng");
            nameOfAttractionThai = extras.getString("nameOfAttractionThai");
            uriImageGameRotateSubAttraction = extras.getString("uriImageGameRotateSubAttraction");
        }

        Toolbar toolbarDetailAttr = (Toolbar) findViewById(R.id.toolbar_detail_attraction);
        setSupportActionBar(toolbarDetailAttr);
        getSupportActionBar().setTitle("Sub_Attraction detail");

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

        //Log.d("onDataChange","bbnnbnbnb"+keyOfAttraction);
        mDatabase.child("attractions").child(keyOfAttraction).child("sub_Attrs").child(keyOfSubAttraction).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange","DetailAttraction1 : "+dataSnapshot.getKey().toString());
                String name = dataSnapshot.child(name_language).getValue().toString();
                String info = "     "+dataSnapshot.child(info_language).getValue().toString();//6 ตัวอักษรย่อหน้า
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

        Button btn_play = (Button) findViewById(R.id.button_play_detail_attraction);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAttraction.this, Camera.class);
                intent.putExtra("keyOfSubAttraction",keyOfSubAttraction);
                intent.putExtra("keyOfAttraction",keyOfAttraction);
                intent.putExtra("keyOfRegion",keyOfRegion);
                intent.putExtra("keyOfUriImgAttraction",keyOfUriImgAttraction);
                intent.putExtra("keyOfCountAttraction",keyOfCountAttraction);
                intent.putExtra("countOfSubAttraction",countOfSubAttraction);
                intent.putExtra("nameOfAttractionEng",nameOfAttractionEng);
                intent.putExtra("nameOfAttractionThai",nameOfAttractionThai);
                intent.putExtra("uriImageGameRotateSubAttraction",uriImageGameRotateSubAttraction);
                startActivity(intent);
            }
        });

        Button btn_back = (Button) findViewById(R.id.button_back_detail_attraction);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailAttraction.this.finish();
            }
        });
    }
}
