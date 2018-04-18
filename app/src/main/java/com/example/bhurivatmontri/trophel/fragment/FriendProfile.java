package com.example.bhurivatmontri.trophel.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.HomeFragment;
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
public class FriendProfile extends Fragment implements View.OnClickListener {

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
    protected Button buttonNorth;
    protected Button buttonCentral;
    protected Button buttonNortheastern;
    protected Button buttonWestern;
    protected Button buttonSouthern;
    protected Button buttonEastern;
    protected Button buttonUnfollow;
    protected String friendID;
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
        friendID = bundle.getString("FriendID");

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
        buttonNorth = (Button) view.findViewById(R.id.button_northern_friend);
        buttonCentral = (Button) view.findViewById(R.id.button_central_friend);
        buttonNortheastern = (Button) view.findViewById(R.id.button_northeastern_friend);
        buttonWestern = (Button) view.findViewById(R.id.button_western_friend);
        buttonSouthern = (Button) view.findViewById(R.id.button_southern_friend);
        buttonEastern = (Button) view.findViewById(R.id.button_eastern_friend);
        buttonUnfollow = (Button) view.findViewById(R.id.button_unfollow_friend);

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

        buttonNorth.setOnClickListener(this);
        buttonCentral.setOnClickListener(this);
        buttonNortheastern.setOnClickListener(this);
        buttonWestern.setOnClickListener(this);
        buttonSouthern.setOnClickListener(this);
        buttonEastern.setOnClickListener(this);
        buttonUnfollow.setOnClickListener(this);

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

    @Override
    public void onClick(View v){
        Bundle bundle = new Bundle();
        switch (v.getId()){
            case R.id.button_northern_friend:
                bundle.putString("Region","1");
                transacFragment(bundle);
                break;
            case R.id.button_central_friend:
                bundle.putString("Region","2");
                transacFragment(bundle);
                break;
            case R.id.button_northeastern_friend:
                bundle.putString("Region","3");
                transacFragment(bundle);
                break;
            case R.id.button_western_friend:
                bundle.putString("Region","4");
                transacFragment(bundle);
                break;
            case R.id.button_southern_friend:
                bundle.putString("Region","5");
                transacFragment(bundle);
                break;
            case R.id.button_eastern_friend:
                bundle.putString("Region","6");
                transacFragment(bundle);
                break;
            case R.id.button_unfollow_friend:
                deleteFragement(bundle);
                HomeFragment.getListFriendInstance().reListFriend();
                break;
        }
    }

    public void transacFragment(Bundle bundle){
        ListTrophy listTrophy = new ListTrophy();
        listTrophy.setArguments(bundle);
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.friend_profile,listTrophy,listTrophy.getTag())
                .addToBackStack(null)
                .commit();
    }

    public void deleteFragement(Bundle bundle){
        Log.d("onDataChange","btn_unfollow");
        /*FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        manager.popBackStack();*/
        mDatabase.child("users").child("uID").child("drive").child("friend_id").child(friendID).removeValue();
        getActivity().onBackPressed();
    }

}

