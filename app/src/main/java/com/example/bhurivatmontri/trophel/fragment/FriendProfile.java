package com.example.bhurivatmontri.trophel.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendProfile extends Fragment {

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    protected ImageView profile;
    protected ImageView background;
    protected TextView idUser;
    protected TextView nameUser;
    protected TextView captionUser;
    protected TextView northernUser;
    protected TextView northeasternUser;
    protected TextView centralUser;
    protected TextView southernUser;
    protected TextView easternUser;
    protected TextView westernUser;
    protected TextView starUser;

    public FriendProfile() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String [] img_type = {"profile.jpg","background.jpg"};
        View view = inflater.inflate(R.layout.fragment_friend_profile, container, false);

        Bundle bundle = getArguments();
        String friendID = bundle.getString("FriendID");

        profile = (ImageView) view.findViewById(R.id.img_Profile_friend);
        background = (ImageView) view.findViewById(R.id.cover_background_friend);
        idUser = (TextView) view.findViewById(R.id.id_Profile_friend);
        nameUser = (TextView) view.findViewById(R.id.name_Profile_friend);
        captionUser = (TextView) view.findViewById(R.id.caption_Profile_friend);
        northernUser = (TextView) view.findViewById(R.id.trophel_northern_count_friend);
        northeasternUser = (TextView) view.findViewById(R.id.trophel_northeastern_count_friend);
        centralUser = (TextView) view.findViewById(R.id.trophel_central_count_friend);
        southernUser = (TextView) view.findViewById(R.id.trophel_southern_count_friend);
        easternUser = (TextView) view.findViewById(R.id.trophel_eastern_count_friend);
        westernUser = (TextView) view.findViewById(R.id.trophel_western_count_friend);
        starUser = (TextView) view.findViewById(R.id.star_count_friend);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        Toast.makeText(getActivity(),friendID,Toast.LENGTH_SHORT).show();

        mDatabase.child("users").child("uID").child(friendID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("id").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String caption = dataSnapshot.child("caption").getValue().toString();
                int countNorthern = dataSnapshot.child("count_Northern").getValue(Integer.class);
                int countNortheastern = dataSnapshot.child("count_Northeastern").getValue(Integer.class);
                int countCentral = dataSnapshot.child("count_Central").getValue(Integer.class);
                int countSouthern = dataSnapshot.child("count_Southern").getValue(Integer.class);
                int countEastern = dataSnapshot.child("count_Eastern").getValue(Integer.class);
                int countWestern = dataSnapshot.child("count_Western").getValue(Integer.class);
                int countStar = dataSnapshot.child("count_Star").getValue(Integer.class);
                idUser.setText("id:"+value);
                nameUser.setText(name);
                captionUser.setText("("+caption+")");
                northernUser.setText(" : "+countNorthern);
                northeasternUser.setText(" : "+countNortheastern);
                centralUser.setText(" : " +
                        ""+countCentral);
                southernUser.setText(" : "+countSouthern);
                easternUser.setText(" : "+countEastern);
                westernUser.setText(" : "+countWestern);
                starUser.setText(" : "+countStar);
                //idUser.setText();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String url_img1 = "img_profile/uImg/"+friendID+"/profile.png";
        mStorage.child(url_img1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("onDataChange","onSuccess");
                Picasso.with(getActivity().getApplicationContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        String url_img2 = "img_profile/uImg/"+friendID+"/background.jpg";
        mStorage.child(url_img2).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("onDataChange","onSuccess");
                Picasso.with(getActivity().getApplicationContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(background);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        //inflater.inflate(R.menu.menu_home,menu);
        /*menu.findItem(R.id.menu_search_friend).setVisible(false);
        menu.findItem(R.id.menu_add_friend).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        /*menu.findItem(R.id.menu_search_friend).setVisible(false);
        menu.findItem(R.id.menu_add_friend).setVisible(false);
        return;*/
    }

}

