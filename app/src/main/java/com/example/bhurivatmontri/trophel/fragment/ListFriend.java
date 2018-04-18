package com.example.bhurivatmontri.trophel.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bhurivatmontri.trophel.R;
import com.example.bhurivatmontri.trophel.adapter.CustomAdapter;
import com.example.bhurivatmontri.trophel.adapter.Friend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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
public class ListFriend extends Fragment implements SearchView.OnQueryTextListener {
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    //int [] icon = {R.drawable.profile_rattpong_kaewpinjai,R.drawable.profile_thatchapon_wongsri,R.drawable.profile_tananut_panyagosa,R.drawable.profile_pakin_suwannawat,R.drawable.profile_watchanan_chantapakul,R.drawable.profile_thatchai_chuaubon,R.drawable.profile_tanatat_tha,R.drawable.profile_gai_lowvapong,R.drawable.profile_aon_natjaya,R.drawable.profile_pansit_wattanaprasobsuk};
    /*String [] name = {"Rattpong Kaewpinjai","Thatchapon Wongsri","Tananut Panyagosa","Pakin Suwannawat","Watchanan Chantapakul","Thatchai_Chuaubon","Tanatat Tha","Gai Lowvapong","Aon Natjaya","Pansit Wattanaprasobsuk"};
    String [] detail = {"570610596","570610578","570610574","570610591","570610601","570610577","570610575","570610549","570610566","570610588"};
    String [] friendID = {"aaa","bbb","ccc","ddd","eee","fff","ggg","hhh","iii","jjj"};*/
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> detail = new ArrayList<>();
    ArrayList<String> friendID = new ArrayList<>();
    ArrayList<String> uriImg = new ArrayList<>();
    ArrayList<Friend> listFriend = new ArrayList<>();
    ArrayList<String> count_Star = new ArrayList<>();

    ArrayList<String> re_name = new ArrayList<>();
    ArrayList<String> re_detail = new ArrayList<>();
    ArrayList<String> re_friendID = new ArrayList<>();
    ArrayList<String> re_uriImg = new ArrayList<>();
    ArrayList<Friend> re_listFriend = new ArrayList<>();
    ArrayList<String> re_count_Star = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDatabase();
        initDataset();
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_Friend);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new CustomAdapter(getActivity(),listFriend);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        Log.d("onDataChange","yyyyyyy");
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        /*mDataset = new String[name.length];
        mDataset2 = new String[detail.length];
        mDataset3 = new int[icon.length];
        for (int i = 0; i < name.length; i++) {
            mDataset[i] = name[i];
            mDataset2[i] = detail[i];
            mDataset3[i] = icon[i];
        }*/

        int i = 0;
        int num = name.size()-1;
        Log.d("onDataChange","555555555555555555555555555555555555555555");

        for (String addName : name) {
            //listFriend.add(new Friend(addName,detail[i],friendID[i],icon[i])) ;
            listFriend.add(new Friend(name.get(num-i),detail.get(num-i),friendID.get(num-i),uriImg.get(num-i),count_Star.get(num-i))) ;
            //Log.d("onDataChange","initDataset :"+addName);
            i++;
        }
    }

    private void initDatabase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        //mDatabase.child("users").child("uID").child("drive").child("friend_id").keepSynced(true);

        mDatabase.child("users").child("uID").child("drive").child("friend_id").orderByChild("count_Star").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("onDataChange",""+dataSnapshot.getChildrenCount());
                setDataSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_friend, menu);
        /*MenuItem menuItem = menu.findItem(R.id.menu_search_friend);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);*/
        return;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Friend> newList = new ArrayList<>();
        for (Friend friend : listFriend) {
            String name = friend.getName().toLowerCase();
            if(name.contains(newText)){
                newList.add(friend);
            }
        }
        mAdapter.setFilter(newList);
        return true;
    }

    public void reListFriend(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        mDatabase.child("users").child("uID").child("drive").child("friend_id").orderByChild("count_Star").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setDataSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reDataset() {
        int i = 0;
        int num = re_name.size()-1;
        Log.d("onDataChange","555555555555555555555555555555555555555555");

        for (String addName : re_name) {
            re_listFriend.add(new Friend(re_name.get(num-i),re_detail.get(num-i),re_friendID.get(num-i),re_uriImg.get(num-i),re_count_Star.get(num-i))) ;
            i++;
        }
    }

    private void reValue(){
        re_name = new ArrayList<>();
        re_detail = new ArrayList<>();
        re_friendID = new ArrayList<>();
        re_uriImg = new ArrayList<>();
        re_listFriend = new ArrayList<>();
        re_count_Star = new ArrayList<>();
    }

    private void setDataSnapshot(DataSnapshot dataSnapshot){
        for(DataSnapshot item_friend : dataSnapshot.getChildren()){
            re_friendID.add(item_friend.getKey().toString());
            re_name.add(item_friend.child("name").getValue().toString());
            re_detail.add(item_friend.child("caption").getValue().toString());
            re_uriImg.add(item_friend.child("uri_profile").getValue().toString());
            re_count_Star.add(item_friend.child("count_Star").getValue(Integer.class).toString());
        }
        reDataset();
        Log.d("onDataChange","zzzz5555 "+re_listFriend.size());
        mAdapter = new CustomAdapter(getActivity(),re_listFriend);
        mRecyclerView.setAdapter(mAdapter);
        reValue();
    }


}
