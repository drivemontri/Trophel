package com.example.bhurivatmontri.trophel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.bhurivatmontri.trophel.adapter.GridAdapter2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActListTrophy extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    String rg;

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    ArrayList<ArrayList<String>> name = new ArrayList<>();
    ArrayList<ArrayList<String>> uri_img_attraction = new ArrayList<>();
    //ArrayList<ArrayList<String>> region = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Integer>>> star = new ArrayList<>();
    ArrayList<ArrayList<Integer>> trophy = new ArrayList<>();
    ArrayList<ArrayList<String>> uri_img = new ArrayList<>();

    HashMap<String,Integer> regionMap = new HashMap<>();
    String[] regionName = {"Northern","Central","Northeastern","Western","Southern","Eastern"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_list_trophy);

        rg = null;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            rg = null;
        } else {
            rg = extras.getString("Region");
        }

        Toolbar toolbarActListTrophy = (Toolbar) findViewById(R.id.toolbar_act_list_trophy);
        setSupportActionBar(toolbarActListTrophy);
        getSupportActionBar().setTitle("My Trophy ("+rg+")");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        for (int i = 0; i < 6 ; i++) {
            name.add(new ArrayList<String>());
            uri_img.add(new ArrayList<String>());
            star.add(new ArrayList<ArrayList<Integer>>());
            trophy.add(new ArrayList<Integer>());
            regionMap.put(regionName[i],i);
        }

        mDatabase.child("users").child("uID").child("drive").child("attractions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot attraction : dataSnapshot.getChildren()) {
                    String reg = attraction.child("region").getValue().toString();
                    int index = regionMap.get(reg);

                    name.get(index).add(attraction.getKey());
                    uri_img.get(index).add(attraction.child("uri_img").getValue().toString());

                    ArrayList<Integer> star_attraction = new ArrayList<>();
                    int count_sub_Attrs = attraction.child("count_sub_Attrs").getValue(Integer.class);
                    int count_star_sub_Attrs = (int)attraction.child("sub_Attrs").getChildrenCount();
                    for (int j = 1; j <= 5; j++) {
                        if(j <= count_star_sub_Attrs && j <= count_sub_Attrs){
                            star_attraction.add(2);
                        }else if( j > count_star_sub_Attrs && j <= count_sub_Attrs){
                            star_attraction.add(1);
                        }else if( j > count_star_sub_Attrs && j > count_sub_Attrs){
                            star_attraction.add(0);
                        }
                    }
                    star.get(index).add(star_attraction);

                    if(count_star_sub_Attrs >= count_sub_Attrs ){
                        trophy.get(index).add(2);
                    }else{
                        trophy.get(index).add(1);
                    }

                    //init data from database
                }

                mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_act_Trophy);
                mRecyclerView.setHasFixedSize(true);

                mLayoutManager  = new GridLayoutManager(ActListTrophy.this,1);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new GridAdapter2(ActListTrophy.this,rg,name,uri_img,star,trophy);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
