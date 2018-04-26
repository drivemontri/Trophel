package com.example.bhurivatmontri.trophel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final String TAG = "LoginActivity";
    private ProgressBar progressBar;
    private static final int RC_SIGN_IN = 45;
    private DatabaseReference mDatabase;
    private SignInButton googlesignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri photouri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        googlesignInButton = findViewById(R.id.sign_in_button);
        googlesignInButton.setSize(SignInButton.SIZE_STANDARD);
        googlesignInButton.setOnClickListener(this);
        Button btnTrophel = (Button) findViewById(R.id.login_btnTrophel);
        final EditText edtEmail =(EditText) findViewById(R.id.login_email);
        final EditText edtPassword = (EditText) findViewById(R.id.login_password);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnTrophel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString() ;
                String password = edtPassword.getText().toString();
                /*if(isValidEmail(email) && isValidPassword(password)){
                    Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,Home.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }else{
                    edtEmail.setError("Your email is invalid type. ");
                }*/

                if(true){
                    if(true){
                        Toast.makeText(Login.this, "Processing : " + email + " / " + password , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,Home.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    }
                    else{
                        edtPassword.setError("Your password must be at least 8 characters , with at least one upper-case and one number");
                    }
                }
                else{
                    edtEmail.setError("Your email is invalid type.");
                }
            }
        });
        //facebook firebase auth
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_btnFacebook);
        loginButton.setReadPermissions("email","public_profile");
        // Other app specific specialization
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(getApplicationContext(),"Cancel.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(getApplicationContext(),"error.",Toast.LENGTH_SHORT).show();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser User = firebaseAuth.getCurrentUser();
                if(User != null)
                {
                    FirebaseDatabase.getInstance().getReference().child("users").child("uID").orderByKey().equalTo(User.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.getChildrenCount() == 0) {
                                        Log.d(TAG, "onDataChange:"+dataSnapshot.getKey()+"    "+User.getUid());
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("caption").setValue("---");
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Central").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Eastern").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Northeastern").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Northern").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Southern").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Star").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("count_Western").setValue(0);
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("id").setValue("user_"+dataSnapshot.getChildrenCount());
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("name").setValue(User.getDisplayName());
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("uri_profile").setValue(User.getPhotoUrl().toString());
                                        mDatabase.child("users").child("uID").child(User.getUid()).child("uri_background").setValue("xxx");
                                    }
                                    goHomescreen();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
            }
        };
    }
//    public void uploadphoto(String url)
//    {
//        Bitmap bitmap = null;
//        InputStream is = null;
//        BufferedInputStream bis = null;
//        try
//        {
//            Log.d(TAG, "uploadphoto: come?");
//            URLConnection conn = new URL(url).openConnection();
//            conn.connect();
//            is = conn.getInputStream();
//            bis = new BufferedInputStream(is, 8192);
//            bitmap = BitmapFactory.decodeStream(bis);
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference storageRef = storage.getReference();
//            StorageReference photoref = storageRef.child("photo.jpg");
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] data = baos.toByteArray();
//            UploadTask uploadTask = photoref.putBytes(data);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Log.d(TAG, "onFailure: unsuccess");
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    Log.d(TAG, "onSuccess: success??");
//                }
//            });
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally {
//            if (bis != null)
//            {
//                try
//                {
//                    bis.close();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            if (is != null)
//            {
//                try
//                {
//                    is.close();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    public void uploadFile(final Uri imagUri, String uid) {
//        Log.d(TAG, "uploadFile: test");
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        if (imagUri != null) {
//            Log.d(TAG, "uploadFile: imagurinotnull");
//            StorageReference photoref = storageRef.child("photo.jpg");
//            photoref.putFile(imagUri).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    Log.d(TAG, "onFailure: error??");
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                }
//            });
//
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                    signIn();
                    break;
            // ...
        }
    }
    private void signIn() {
        Log.d(TAG, "signIn: test");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        mGoogleSignInClient.revokeAccess();
        TextView textView = (TextView) googlesignInButton.getChildAt(0);
        textView.setText("Sign in");
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("test", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("test", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //uid = user.getUid();
                            TextView textView = (TextView) googlesignInButton.getChildAt(0);
                            textView.setText("Sign out");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("test", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final  static boolean isValidPassword(final String password){
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$").matcher(password).matches();
    }

    public void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("test", "Google sign in failed", e);
                // ...
            }
        }
    }
    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
    public void goHomescreen(){
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
