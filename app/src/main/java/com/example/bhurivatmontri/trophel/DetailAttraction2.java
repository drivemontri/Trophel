package com.example.bhurivatmontri.trophel;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.adapter.ImgPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailAttraction2 extends AppCompatActivity {

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

   /* private String[] imgUrls = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/trophel-application.appspot.com/o/attractions%2FNorthern%2FChiang%20Mai%2F%E0%B8%AD%E0%B8%99%E0%B8%B8%E0%B8%AA%E0%B8%B2%E0%B8%A7%E0%B8%A3%E0%B8%B5%E0%B8%A2%E0%B9%8C%E0%B8%AA%E0%B8%B2%E0%B8%A1%E0%B8%81%E0%B8%A9%E0%B8%B1%E0%B8%95%E0%B8%A3%E0%B8%B4%E0%B8%A2%E0%B9%8C%2Fimg1.jpg?alt=media&token=e1258b1f-3970-44b5-96ea-2bd32f140f7b",
            "https://firebasestorage.googleapis.com/v0/b/trophel-application.appspot.com/o/attractions%2FNorthern%2FChiang%20Mai%2F%E0%B8%84%E0%B8%93%E0%B8%B0%E0%B8%A7%E0%B8%B4%E0%B8%A8%E0%B8%A7%E0%B8%81%E0%B8%A3%E0%B8%A3%E0%B8%A1%E0%B8%A8%E0%B8%B2%E0%B8%AA%E0%B8%95%E0%B8%A3%E0%B9%8C%20%E0%B8%A1%E0%B8%AB%E0%B8%B2%E0%B8%A7%E0%B8%B4%E0%B8%97%E0%B8%A2%E0%B8%B2%E0%B8%A5%E0%B8%B1%E0%B8%A2%E0%B9%80%E0%B8%8A%E0%B8%B5%E0%B8%A2%E0%B8%87%E0%B9%83%E0%B8%AB%E0%B8%A1%E0%B9%88%2Fimg1.jpg?alt=media&token=1c21fe8f-0222-4620-b115-e61dd8576e0d",
            "https://firebasestorage.googleapis.com/v0/b/trophel-application.appspot.com/o/attractions%2FNorthern%2FChiang%20Mai%2F%E0%B8%84%E0%B8%93%E0%B8%B0%E0%B8%A7%E0%B8%B4%E0%B8%A8%E0%B8%A7%E0%B8%81%E0%B8%A3%E0%B8%A3%E0%B8%A1%E0%B8%A8%E0%B8%B2%E0%B8%AA%E0%B8%95%E0%B8%A3%E0%B9%8C%20%E0%B8%A1%E0%B8%AB%E0%B8%B2%E0%B8%A7%E0%B8%B4%E0%B8%97%E0%B8%A2%E0%B8%B2%E0%B8%A5%E0%B8%B1%E0%B8%A2%E0%B9%80%E0%B8%8A%E0%B8%B5%E0%B8%A2%E0%B8%87%E0%B9%83%E0%B8%AB%E0%B8%A1%E0%B9%88%2Fsub_attrs%2F%E0%B8%95%E0%B8%B6%E0%B8%81%2030%20%E0%B8%9B%E0%B8%B5%2F%E0%B8%95%E0%B8%B6%E0%B8%81%2030%20%E0%B8%9B%E0%B8%B51.jpg?alt=media&token=5a044f5f-fb3c-45d2-89a9-a3397b41775b"
    };*/

    ArrayList<String> imgUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction2);

        String keyOfAttraction = null;

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
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
        Log.d("onDataChange","keyOfAttraction"+keyOfAttraction);

        mDatabase.child("attractions").child(keyOfAttraction).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange",""+dataSnapshot.getKey().toString());
                String name = dataSnapshot.child("name_Eng").getValue().toString();
                String info = dataSnapshot.child("info_Eng").getValue().toString();
                DataSnapshot uri_imgs = dataSnapshot.child("uri_img_group");
                int num_img = (int)uri_imgs.getChildrenCount();
                for (int i = 1; i <= num_img ; i++) {
                    Log.d("onDataChange","num of img:"+i);
                    Log.d("onDataChange",""+uri_imgs.child("uri_img"+i).getValue().toString());
                    String eUri = uri_imgs.child("uri_img"+i).getValue().toString();
                    imgUrls.add(eUri);
                }

                //String uri_img = dataSnapshot.child("uri_img").getValue().toString();
                nameDetailAttraction.setText(name);
                detailDetailAttraction.setText(info);
                ViewPager viewPager = findViewById(R.id.cover_detail_attraction2);
                ImgPager adapter = new ImgPager(getApplicationContext(),imgUrls);
                viewPager.setAdapter(adapter);
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
}
