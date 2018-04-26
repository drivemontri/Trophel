package com.example.bhurivatmontri.trophel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhurivatmontri.trophel.adapter.GridAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static org.opencv.core.Core.addWeighted;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OpenCVCamera";
    private static final String AsyncTAG = "AsyncTAG";
    private CameraBridgeViewBase cameraBridgeViewBase;
    Mat rgba,rgbaT,rgbaF;
    private int w, h;
    Scalar RED = new Scalar(255, 0, 0);
    Scalar GREEN = new Scalar(0, 255, 0);
    FeatureDetector detector;
    DescriptorExtractor descriptor;
    DescriptorMatcher matcher;
    Mat descriptors2,descriptors1,descriptors3;
    Mat img1,img2;
    MatOfKeyPoint keypoints1,keypoints2;

    boolean isMatch = false;
    boolean firstRun = true;
    AsyncTask FrameProcessing;
    //MatOfDMatch goodMatches_chk = new MatOfDMatch();
    String title;
    TextView tv_title;
    ImageButton ib_success;
    ImageView iv_transparent;
    int numberOfMatch;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    protected String user_Id;

    protected DatabaseReference mDatabase;
    protected DatabaseReference mDatabase2;
    protected FirebaseStorage storage = FirebaseStorage.getInstance();

    String keyOfAttraction;
    String keyOfSubAttraction;
    String keyOfRegion;
    String keyOfUriImgAttraction;
    String uriImageGameRotateSubAttraction;
    int keyOfCountAttraction;
    int countOfSubAttraction;
    String nameOfAttractionEng;
    String nameOfAttractionThai;
    int protect_bug = 1;
    double count_star_to_friend;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV not loaded (Test)");
        }else{
            Log.d(TAG,"OpenCV loaded successfully (Test)");
        }
    }

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            //super.onManagerConnected(status);
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    cameraBridgeViewBase.enableView();
                    try {
                        initializeOpenCVDependencies();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    private void initializeOpenCVDependencies() throws IOException {
        cameraBridgeViewBase.enableView();
        detector = FeatureDetector.create(FeatureDetector.ORB);
        descriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        img1 = new Mat();
        AssetManager assetManager = getAssets();
        InputStream istr;
        switch (title){
            case "รูปปั้นอนุสาวรีย์สามกษัตริย์" : istr = assetManager.open("อนุสาวรีย์3กษัตริย์.JPG"); break;
            case "ป้ายหน้าภาควิชาวิศวกรรมคอมพิวเตอร์" : istr = assetManager.open("CPE_CMU.jpg"); break;
            case "ป้ายทางเข้าพิพิธภัณฑ์" : istr = assetManager.open("ศาลาธนารักษ์.JPG"); break;
            case "หอคำวิหารวัดพันเตา" : istr = assetManager.open("วัดพันเตา.jpg");break;
            case "เจดีย์หลวง" : istr = assetManager.open("วัดเจดีย์หลวง.jpg");break;
            case "Myst MAYA Chiang Mai" : istr = assetManager.open("mystmaya.jpg");break;
            case "ตึก 30 ปี" : istr = assetManager.open("ตึก 30 ปี.jpg");break;
            default: istr = assetManager.open("temple_icon");break;
        }
        //InputStream istr = assetManager.open("temple02_1.JPG");
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        Utils.bitmapToMat(bitmap, img1);
//        InputStream test1 = getResources().get(R.drawable.zzz);
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.bank20);
//        Utils.bitmapToMat(bitmap1, img1);

        Size size = new Size(800,600);
        Imgproc.resize( img1, img1, size );
        Imgproc.cvtColor(img1, img1, Imgproc.COLOR_RGB2GRAY);
        img1.convertTo(img1, 0); //converting the image to match with the type of the cameras image
        descriptors1 = new Mat();
        keypoints1 = new MatOfKeyPoint();
        detector.detect(img1, keypoints1);
        descriptor.compute(img1, keypoints1, descriptors1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //System.loadLibrary("opencv_java3");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            user_Id = user.getUid();
        }
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            title = null;
            keyOfAttraction = null;
            keyOfSubAttraction = null;
            keyOfRegion = null;
            keyOfUriImgAttraction = null;
        } else {
            title = extras.getString("keyOfSubAttraction");
            keyOfAttraction = extras.getString("keyOfAttraction");
            keyOfSubAttraction = extras.getString("keyOfSubAttraction");
            keyOfRegion = extras.getString("keyOfRegion");
            keyOfUriImgAttraction = extras.getString("keyOfUriImgAttraction");
            keyOfCountAttraction = extras.getInt("keyOfCountAttraction");
            uriImageGameRotateSubAttraction = extras.getString("uriImageGameRotateSubAttraction");
            countOfSubAttraction = extras.getInt("countOfSubAttraction");
            nameOfAttractionEng = extras.getString("nameOfAttractionEng");
            nameOfAttractionThai = extras.getString("nameOfAttractionThai");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }

        cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.camera1);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorage = storage.getReference();

        tv_title = (TextView) findViewById(R.id.title1);
        tv_title.setText("Activating via OpenCV : " + title);
        ib_success = (ImageButton) findViewById(R.id.ib_camera_success);
        iv_transparent = (ImageView) findViewById(R.id.image_transparent);
        Picasso.with(getApplicationContext())
                .load(uriImageGameRotateSubAttraction)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(iv_transparent);
        Log.d(TAG,"onCreate passed successfully");
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(cameraBridgeViewBase != null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV on Resume loaded");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            Log.d(TAG,"OpenCV on Resume is not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,baseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        rgba = new Mat(height,width, CvType.CV_8UC4);
        rgbaT = new Mat(width,width,CvType.CV_8UC4);
        rgbaF = new Mat(width,width,CvType.CV_8UC4);
        /*super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV is not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,baseLoaderCallback);
        }else{
            Log.d(TAG,"OpenCV loaded successfully");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }*/
    }

    @Override
    public void onCameraViewStopped() {
        rgba.release();
        rgbaT.release();
        rgbaF.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.d(TAG,"OpenCV_onCameraFrame");
        rgba = inputFrame.rgba();
        /*Core.transpose(rgba,rgbaT);
        Core.flip(rgbaT,rgbaT,1);
        Imgproc.resize(rgbaT,rgbaT,rgba.size());
        return rgbaT;*/

        if (firstRun && !isMatch) {
            FrameProcessing = new MatchFrame().execute();
            firstRun = false;
        }

        try{
            if(!isMatch && FrameProcessing.getStatus() != AsyncTask.Status.RUNNING){
                Log.d(AsyncTAG, "AsyncTask is executing");
                FrameProcessing = new MatchFrame().execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //img2 = img1;
        //Imgproc.resize( img1, img2, rgba.size() );
        //addWeighted(img2,0.5,rgba,0.5,0.0,rgba);
        return rgba;
    }

    public Mat recognize2(Mat aInputFrame) {
        Imgproc.cvtColor(aInputFrame, aInputFrame, Imgproc.COLOR_RGB2GRAY);
        descriptors2 = new Mat();
        keypoints2 = new MatOfKeyPoint();
        detector.detect(aInputFrame, keypoints2);
        descriptor.compute(aInputFrame, keypoints2, descriptors2);

        // Matching
        MatOfDMatch matches = new MatOfDMatch();
        if (img1.type() == aInputFrame.type()&& (!descriptors2.empty())) {
            matcher.match(descriptors1, descriptors2, matches);
        } else {
            return aInputFrame;
        }
        List<DMatch> matchesList = matches.toList();

        Double max_dist = 0.0;
        Double min_dist = 100.0;

        for (int i = 0; i < matchesList.size(); i++) {
            Double dist = (double) matchesList.get(i).distance;
            if (dist < min_dist)
                min_dist = dist;
            if (dist > max_dist)
                max_dist = dist;
        }

        LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
        for (int i = 0; i < matchesList.size(); i++) {
            if (matchesList.get(i).distance <= (1.5 * min_dist))
                good_matches.addLast(matchesList.get(i));
        }

        MatOfDMatch goodMatches = new MatOfDMatch();
        goodMatches.fromList(good_matches);
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();
        if (aInputFrame.empty() || aInputFrame.cols() < 1 || aInputFrame.rows() < 1) {
            return aInputFrame;
        }
        Features2d.drawMatches(img1, keypoints1, aInputFrame, keypoints2, goodMatches, outputImg, GREEN, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
        Imgproc.resize(outputImg, outputImg, aInputFrame.size());
        return outputImg;
    }

    public boolean recognize() {
        Mat InputFrame = rgba;
        Imgproc.cvtColor(InputFrame, InputFrame, Imgproc.COLOR_RGB2GRAY);
        descriptors2 = new Mat();
        keypoints2 = new MatOfKeyPoint();
        detector.detect(InputFrame, keypoints2);
        descriptor.compute(InputFrame, keypoints2, descriptors2);
        List<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();

        /*if (img1.type() == InputFrame.type()&& (!descriptors2.empty())) {
            matcher.knnMatch(descriptors1, descriptors2, matches, 5);
        } else {
            return InputFrame;
        }*/
        matcher.knnMatch(descriptors1, descriptors2, matches, 5);
        Mat outputImg = new Mat();
        MatOfByte drawnMatches = new MatOfByte();

        LinkedList<DMatch> good_matches = new LinkedList<>();
        for (Iterator<MatOfDMatch> iterator = matches.iterator(); iterator.hasNext();) {
            MatOfDMatch matOfDMatch = (MatOfDMatch) iterator.next();
            if (matOfDMatch.toArray()[0].distance / matOfDMatch.toArray()[1].distance < 0.9) {
                good_matches.add(matOfDMatch.toArray()[0]);
            }
        }

        // get keypoint coordinates of good matches to find homography and remove outliers using ransac
        List<Point> pts1 = new ArrayList<Point>();
        List<Point> pts2 = new ArrayList<Point>();
        for(int i = 0; i<good_matches.size(); i++){
            pts1.add(keypoints1.toList().get(good_matches.get(i).queryIdx).pt);
            pts2.add(keypoints2.toList().get(good_matches.get(i).trainIdx).pt);
        }

        // convertion of data types - there is maybe a more beautiful way
        Mat outputMask = new Mat();
        MatOfPoint2f pts1Mat = new MatOfPoint2f();
        pts1Mat.fromList(pts1);
        MatOfPoint2f pts2Mat = new MatOfPoint2f();
        pts2Mat.fromList(pts2);

        // Find homography - here just used to perform match filtering with RANSAC, but could be used to e.g. stitch images
        // the smaller the allowed reprojection error (here 15), the more matches are filtered
        Mat Homog = Calib3d.findHomography(pts1Mat, pts2Mat, Calib3d.RANSAC, 15, outputMask, 2000, 0.995);

        // outputMask contains zeros and ones indicating which matches are filtered
        LinkedList<DMatch> better_matches = new LinkedList<DMatch>();
        for (int i = 0; i < good_matches.size(); i++) {
            if (outputMask.get(i, 0)[0] != 0.0) {
                better_matches.add(good_matches.get(i));
            }
        }
        MatOfDMatch better_matches_mat = new MatOfDMatch();
        better_matches_mat.fromList(better_matches);
        numberOfMatch = better_matches.size();
        //tv_title.setText("number of match : "+better_matches.size());
        Log.d(AsyncTAG,"number of match : " + better_matches.size());
        //Features2d.drawMatches(img1, keypoints1, aInputFrame, keypoints2, better_matches_mat, outputImg, RED, RED, drawnMatches, Features2d.NOT_DRAW_SINGLE_POINTS);
        //Imgproc.resize(outputImg, outputImg, aInputFrame.size());
        if(numberOfMatch > 80){
            if(protect_bug==3) {
                protect_bug = 1;
                return true;
            }
            else protect_bug++;
        }
        return false;
    }

    private class MatchFrame extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected synchronized Boolean doInBackground(Void... voids) {
            boolean chkMatch = false;
            try {
                if (recognize())
                    chkMatch = true;
                else
                    chkMatch = false;
                this.wait(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return chkMatch;
        }

        @Override
        protected void onPostExecute(final Boolean chkMatch) {
            tv_title.setText("number of match : " + numberOfMatch);
            if (chkMatch == true){
                mDatabase.child("users").child("uID").child(user_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long chk_ever = 0;
                        try{
                            chk_ever = dataSnapshot.child("attractions").child(keyOfAttraction).child("sub_Attrs").child(keyOfSubAttraction).child("status").getValue(Long.class);
                        }catch (Exception e)
                        {
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("region").setValue(keyOfRegion);
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("count_sub_Attrs").setValue(keyOfCountAttraction);
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("uri_img").setValue(keyOfUriImgAttraction);
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("sub_Attrs").child(keyOfSubAttraction).child("status").setValue(1);
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("name_Eng").setValue(nameOfAttractionEng);
                            mDatabase.child("users").child("uID").child(user_Id).child("attractions").child(keyOfAttraction)
                                    .child("name_Thai").setValue(nameOfAttractionThai);

                            mDatabase2.child("users").child("uID").child(user_Id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    long count_star_rg = 0;
                                    long[] count_trophy_rg = {0,0,0,0,0,0};
                                    //count_star_rg = dataSnapshot.child("count_Star").getValue(Long.class);
                                    count_star_rg = 0;
                                   /* count_trophy_rg[0] = dataSnapshot.child("count_Northern").getValue(Long.class);
                                    count_trophy_rg[1] = dataSnapshot.child("count_Central").getValue(Long.class);
                                    count_trophy_rg[2] = dataSnapshot.child("count_Northeastern").getValue(Long.class);
                                    count_trophy_rg[3] = dataSnapshot.child("count_Western").getValue(Long.class);
                                    count_trophy_rg[4] = dataSnapshot.child("count_Southern").getValue(Long.class);
                                    count_trophy_rg[5] = dataSnapshot.child("count_Eastern").getValue(Long.class);*/
                                    count_trophy_rg[0] = 0;
                                    count_trophy_rg[1] = 0;
                                    count_trophy_rg[2] = 0;
                                    count_trophy_rg[3] = 0;
                                    count_trophy_rg[4] = 0;
                                    count_trophy_rg[5] = 0;

                                    String[] rg = {"Northern","Central","Northeastern","Western","Southern","Eastern"};
                                    // for find star
                                    long count_sub_attr = 0;
                                    for (DataSnapshot dataSnapshot2 : dataSnapshot.child("attractions").getChildren()) {
                                        count_sub_attr = 0;
                                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.child("sub_Attrs").getChildren()) {
                                            count_sub_attr += 1;
                                            count_star_rg += 1;
                                        }
                                        Log.d("onDataChange","me : "+dataSnapshot2.child("region"));
                                        Log.d("onDataChange","attr : "+dataSnapshot2.child("count_sub_Attrs"));

                                        String regionAttr = dataSnapshot2.child("region").getValue().toString();
                                        long countSubAttr = dataSnapshot2.child("count_sub_Attrs").getValue(Long.class);
                                        Log.d("test777","countSubAttr:"+countSubAttr);
                                        Log.d("test777","countSubAttr:"+count_sub_attr);
                                        Log.d("test777","------------:");
                                        if(regionAttr.equals(rg[0]) && countSubAttr == count_sub_attr){
                                            Log.d("onDataChange","North+++North");
                                            count_trophy_rg[0] += 1;
                                        }else if(regionAttr.equals(rg[1]) && countSubAttr == count_sub_attr){
                                            count_trophy_rg[1] += 1;
                                        }else if(regionAttr.equals(rg[2]) && countSubAttr == count_sub_attr){
                                            count_trophy_rg[2] += 1;
                                        }else if(regionAttr.equals(rg[3]) && countSubAttr == count_sub_attr){
                                            count_trophy_rg[3] += 1;
                                        }else if(regionAttr.equals(rg[4]) && countSubAttr == count_sub_attr){
                                            count_trophy_rg[4] += 1;
                                        }else if(regionAttr.equals(rg[5]) && countSubAttr == count_sub_attr){
                                            count_trophy_rg[5] += 1;
                                        }
                                    }

                                    Log.d("onDataChange", "chk_ever=2");
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Star").setValue(count_star_rg);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Northern").setValue(count_trophy_rg[0]);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Central").setValue(count_trophy_rg[1]);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Northeastern").setValue(count_trophy_rg[2]);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Western").setValue(count_trophy_rg[3]);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Southern").setValue(count_trophy_rg[4]);
                                    mDatabase.child("users").child("uID").child(user_Id).child("count_Eastern").setValue(count_trophy_rg[5]);
                                    count_star_to_friend = count_star_rg;
                                    FirebaseDatabase.getInstance().getReference("users").child("uID").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                for (DataSnapshot datasnapshot3 : dataSnapshot2.child("friend_id").getChildren()) {
                                                    if(datasnapshot3.getKey().equals(user_Id)){
                                                        mDatabase.child("users").child("uID").child(dataSnapshot2.getKey())
                                                                .child("friend_id").child(user_Id).child("count_Star").setValue(count_star_to_friend);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        Log.d(AsyncTAG, "Match is Success.");
                        isMatch = true;
                        ib_success.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Camera.this.finish();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ib_success.setVisibility(View.VISIBLE);
                iv_transparent.setVisibility(View.INVISIBLE);
            }
            this.cancel(true);
        }
    }

}
