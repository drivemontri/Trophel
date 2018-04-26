package com.example.bhurivatmontri.trophel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddFriend extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    protected String user_Id;

    protected DatabaseReference mDatabase;
    protected DatabaseReference mDatabase2;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();
    protected boolean chk = false;
    protected String uId = "";
    protected String caption , id , name , uri_profile ;
    protected long count_Star;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Toolbar toolbarAddFriend = (Toolbar) findViewById(R.id.toolbar_add_friend);
        setSupportActionBar(toolbarAddFriend);
        getSupportActionBar().setTitle("Add to Following");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_Id = user.getUid();
            // User is signed in
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        final EditText edtFriendId =(EditText) findViewById(R.id.edit_add_freind);
        Button btnAddFriend = (Button) findViewById(R.id.bottom_add_friend);
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_friend = edtFriendId.getText().toString();

                mDatabase.child("users").child("uID").orderByChild("id").equalTo(id_friend).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("onDataChange","sizeOfAddFriend"+dataSnapshot.getChildrenCount());
                        //String uId = dataSnapshot.getChildren().toString();
                        Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();
                        for (DataSnapshot contact : contactChildren) {
                            uId = contact.getKey();
                        }
                        Log.d("onDataChange",uId);
                        if(dataSnapshot.getChildrenCount() == 1){
                            Log.d("onDataChange",dataSnapshot.getValue().toString());
                            //Log.d("onDataChange",dataSnapshot.child(uId).child("caption").toString());
                            caption = dataSnapshot.child(uId).child("caption").getValue().toString();
                            //count_Star = dataSnapshot.child(uId).child("count_Star").getValue().toString();
                            count_Star = dataSnapshot.child(uId).child("count_Star").getValue(Long.class);
                            id = dataSnapshot.child(uId).child("id").getValue().toString();
                            name = dataSnapshot.child(uId).child("name").getValue().toString();
                            uri_profile = dataSnapshot.child(uId).child("uri_profile").getValue().toString();
                            AddFriendObj friendObj = new AddFriendObj(caption,count_Star,id,name,uri_profile);
                            Log.d("onDataChange",caption);
                            Log.d("onDataChange",""+count_Star);
                            Log.d("onDataChange",id);
                            Log.d("onDataChange",name);
                            Log.d("onDataChange",uri_profile);
                            chk = true;
                            mDatabase2.child("users").child("uID").child(user_Id).child("friend_id").child(uId)
                                    .child("caption").setValue(caption);
                            mDatabase2.child("users").child("uID").child(user_Id).child("friend_id").child(uId)
                                    .child("count_Star").setValue(count_Star);
                            mDatabase2.child("users").child("uID").child(user_Id).child("friend_id").child(uId)
                                    .child("id").setValue(id);
                            mDatabase2.child("users").child("uID").child(user_Id).child("friend_id").child(uId)
                                    .child("name").setValue(name);
                            mDatabase2.child("users").child("uID").child(user_Id).child("friend_id").child(uId)
                                    .child("uri_profile").setValue(uri_profile);

                            HomeFragment.getListFriendInstance().reListFriend();
                            AddFriend.this.finish();
                        }else{

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("onDataChange","Not have id in Application");
                    }
                });

                if(chk == true){

                }
            }
        });
    }

    @IgnoreExtraProperties
    public class AddFriendObj {

        public String caption;
        public long count_Star;
        public String id;
        public String name;
        public String uri_profile;

        public AddFriendObj(String caption, long count_Star,String id,String name,String uri_profile) {
            this.caption = caption;
            this.count_Star = count_Star;
            this.id = id;
            this.name = name;
            this.uri_profile = uri_profile;
        }
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
