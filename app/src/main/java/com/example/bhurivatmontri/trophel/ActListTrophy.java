package com.example.bhurivatmontri.trophel;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.bhurivatmontri.trophel.adapter.GridAdapter2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    protected String user_Id;

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

    ArrayList<Integer> count_star_all = new ArrayList<>();
    ArrayList<Integer> count_trophy_all = new ArrayList<>();

    protected TextView tv_count_star_all;
    protected TextView tv_count_trophy_all;

    protected SharedPreferences settings;
    protected int select_language_position;
    protected String name_language = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_list_trophy);

        settings = this.getSharedPreferences("Trophel",MODE_WORLD_WRITEABLE);
        select_language_position = settings.getInt("select_language_position",-1);
        Log.d("onDataChange",""+select_language_position);
        switch (select_language_position){
            case -1: name_language = "name_Eng";break;
            case 0:  name_language = "name_Eng";break;
            case 1:  name_language = "name_Eng";break;
            case 2:  name_language = "name_Thai";break;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            user_Id = user.getUid();
        }

        rg = null;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            rg = null;
        } else {
            rg = extras.getString("Region");
        }

        Toolbar toolbarActListTrophy = (Toolbar) findViewById(R.id.toolbar_act_list_trophy);
        setSupportActionBar(toolbarActListTrophy);
        getSupportActionBar().setTitle("My Trophy ("+regionName[Integer.parseInt(rg) - 1]+")");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        for (int i = 0; i < 6 ; i++) {
            name.add(new ArrayList<String>());
            uri_img.add(new ArrayList<String>());
            star.add(new ArrayList<ArrayList<Integer>>());
            trophy.add(new ArrayList<Integer>());
            regionMap.put(regionName[i],i);
            count_star_all.add(0);
            count_trophy_all.add(0);
        }

        mDatabase.child("users").child("uID").child(user_Id).child("attractions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot attraction : dataSnapshot.getChildren()) {
                    String reg = attraction.child("region").getValue().toString();
                    int index = regionMap.get(reg);

                    name.get(index).add(attraction.child(name_language).getValue().toString());
                    uri_img.get(index).add(attraction.child("uri_img").getValue().toString());

                    ArrayList<Integer> star_attraction = new ArrayList<>();
                    int count_sub_Attrs = attraction.child("count_sub_Attrs").getValue(Integer.class);
                    int count_star_sub_Attrs = (int)attraction.child("sub_Attrs").getChildrenCount();
                    for (int j = 1; j <= 5; j++) {
                        if(j <= count_star_sub_Attrs && j <= count_sub_Attrs){
                            int count_star = count_star_all.get(index);
                            count_star+=1;
                            count_star_all.set(index,count_star);
                            star_attraction.add(2);
                        }else if( j > count_star_sub_Attrs && j <= count_sub_Attrs){
                            star_attraction.add(1);
                        }else if( j > count_star_sub_Attrs && j > count_sub_Attrs){
                            star_attraction.add(0);
                        }
                    }
                    star.get(index).add(star_attraction);

                    if(count_star_sub_Attrs >= count_sub_Attrs ){
                        int count_trophy = count_trophy_all.get(index);
                        count_trophy+=1;
                        count_trophy_all.set(index,count_trophy);
                        trophy.get(index).add(index);
                    }else{
                        trophy.get(index).add(6);
                    }

                    //init data from database
                }

                mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_act_Trophy);
                mRecyclerView.setHasFixedSize(true);

                mLayoutManager  = new GridLayoutManager(ActListTrophy.this,1);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new GridAdapter2(ActListTrophy.this,rg,name,uri_img,star,trophy);
                mRecyclerView.setAdapter(mAdapter);

                tv_count_star_all = (TextView) findViewById(R.id.text_sum_star_list_trophy_2);
                tv_count_trophy_all = (TextView) findViewById(R.id.text_sum_trophy_list_trophy_2);

                int region = Integer.parseInt(rg);
                tv_count_star_all.setText(""+count_star_all.get(region-1));
                tv_count_trophy_all.setText(""+count_trophy_all.get(region-1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
