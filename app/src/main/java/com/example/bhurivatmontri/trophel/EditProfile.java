package com.example.bhurivatmontri.trophel;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private Uri mImageUriProfile;
    private Uri mImageUriBackground;

    protected DatabaseReference mDatabase;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();
    protected StorageReference  mStorage;

    protected ImageView profile;
    protected ImageView background;
    protected EditText idUser;
    protected EditText nameUser;
    protected EditText captionUser;
    protected Button button_accept;
    protected Button button_cancel;

    protected int chk_edit_img = 0;
    protected boolean chk_edit_img_profile = false;
    protected boolean chk_edit_img_background = false;
    protected boolean chk_edit_img_profile_success = true;
    protected boolean chk_edit_img_background_success = true;
    protected boolean chk_edit_info_success = false;
    protected String uri_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbarActProfile = (Toolbar) findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbarActProfile);
        getSupportActionBar().setTitle("Edit Profile");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = storage.getReference("img_user/");

        profile = (ImageView) findViewById(R.id.img_Profile_edit);
        background = (ImageView) findViewById(R.id.cover_background_edit);
        idUser = (EditText) findViewById(R.id.id_Profile_edit);
        nameUser = (EditText) findViewById(R.id.name_Profile_edit);
        captionUser = (EditText) findViewById(R.id.caption_Profile_edit);
        button_accept = (Button) findViewById(R.id.button_accept_edit_my_profile);
        button_cancel = (Button) findViewById(R.id.button_cancel_edit_my_profile);

        profile.setOnClickListener(this);
        background.setOnClickListener(this);
        idUser.setOnClickListener(this);
        nameUser.setOnClickListener(this);
        captionUser.setOnClickListener(this);
        button_accept.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        mDatabase.child("users").child("uID").child("drive").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.child("id").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String caption = dataSnapshot.child("caption").getValue().toString();
                String uri_profile_init = dataSnapshot.child("uri_profile").getValue().toString();
                String uri_background_init = dataSnapshot.child("uri_background").getValue().toString();

                idUser.setText("id:"+value);
                nameUser.setText(name);
                captionUser.setText("("+caption+")");

                Picasso.with(getApplicationContext())
                        .load(uri_profile_init)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(profile);
                Picasso.with(getApplicationContext())
                        .load(uri_background_init)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(background);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Profile_edit:
                chk_edit_img = 1;
                openFileChooser();
                break;
            case R.id.cover_background_edit:
                chk_edit_img = 2;
                openFileChooser();
                break;
            case R.id.name_Profile_edit:

                break;
            case R.id.caption_Profile_edit:

                break;
            case R.id.id_Profile_edit:

                break;
            case R.id.button_accept_edit_my_profile:
                if(chk_edit_img_profile){
                            uploadFileProfile();
                        }
                        if(chk_edit_img_background){
                            uploadFileBackground();
                        }
                uploadInfo();
                break;
            case R.id.button_cancel_edit_my_profile:
                this.finish();
                break;
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            switch (chk_edit_img){
                case 1:
                    Picasso.with(this)
                            .load(mImageUri)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(profile);
                    mImageUriProfile = mImageUri;
                    chk_edit_img_profile = true;
                    chk_edit_img_profile_success = false;
                    break;
                case 2:
                    Picasso.with(this)
                            .load(mImageUri)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .centerCrop()
                            .into(background);
                    mImageUriBackground = mImageUri;
                    chk_edit_img_background = true;
                    chk_edit_img_background_success = false;
                    break;
            }

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //function prototype
    /*private void uploadFile(){
        if(mImageUri != null){
            StorageReference fileRefference = mStorage.child(System.currentTimeMillis()+ "." +  getFileExtension(mImageUri));

            fileRefference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfile.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    Upload upload = new Upload("drive",taskSnapshot.getDownloadUrl().toString());
                    String uploadId = mDatabase.push().getKey();
                    mDatabase.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }*/

    private void uploadFileProfile(){
        if(mImageUriProfile != null){
            StorageReference fileRefference = mStorage.child("img_profile/"+"drive"+ "." +  getFileExtension(mImageUriProfile));

            fileRefference.putFile(mImageUriProfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfile.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    mDatabase.child("users").child("uID").child("drive").child("uri_profile").setValue(taskSnapshot.getDownloadUrl().toString());
                    chk_edit_img_profile_success = true;
                    uri_profile = taskSnapshot.getDownloadUrl().toString();
                    FirebaseDatabase.getInstance().getReference("users").child("uID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                for (DataSnapshot datasnapshot3 : dataSnapshot2.child("friend_id").getChildren()) {
                                    if(datasnapshot3.getKey().equals("drive")){
                                        mDatabase.child("users").child("uID").child(dataSnapshot2.getKey())
                                                .child("friend_id").child("drive").child("uri_profile").setValue(uri_profile);
                                    }
                                }
                            }
                            if(chk_edit_img_profile_success && chk_edit_img_background_success){
                                EditProfile.this.finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFileBackground(){
        if(mImageUriBackground != null){
            StorageReference fileRefference = mStorage.child("img_background/"+"drive"+ "." +  getFileExtension(mImageUriBackground));

            fileRefference.putFile(mImageUriBackground).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfile.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    mDatabase.child("users").child("uID").child("drive").child("uri_background").setValue(taskSnapshot.getDownloadUrl().toString());
                    chk_edit_img_background_success = true;
                    if(chk_edit_img_profile_success && chk_edit_img_background_success){
                        EditProfile.this.finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void uploadInfo(){
        mDatabase.child("users").child("uID").child("drive").child("name").setValue(nameUser.getText().toString());
        mDatabase.child("users").child("uID").child("drive").child("caption").setValue(captionUser.getText().toString());
        mDatabase.child("users").child("uID").child("drive").child("id").setValue(idUser.getText().toString());
        FirebaseDatabase.getInstance().getReference("users").child("uID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    for (DataSnapshot datasnapshot3 : dataSnapshot2.child("friend_id").getChildren()) {
                        if(datasnapshot3.getKey().equals("drive")){
                            mDatabase.child("users").child("uID").child(dataSnapshot2.getKey())
                                    .child("friend_id").child("drive").child("name").setValue(nameUser.getText().toString());
                            mDatabase.child("users").child("uID").child(dataSnapshot2.getKey())
                                    .child("friend_id").child("drive").child("caption").setValue(captionUser.getText().toString());
                        }
                    }
                }
                chk_edit_info_success = true;
                if(chk_edit_img_profile_success && chk_edit_img_background_success && chk_edit_info_success){
                    EditProfile.this.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
