package com.example.bhurivatmontri.trophel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static android.Manifest.permission.CAMERA;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OpenCVCamera";
    private CameraBridgeViewBase cameraBridgeViewBase;
    Mat rgba;
    Mat rgbaT;

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            //super.onManagerConnected(status);
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                    cameraBridgeViewBase.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV not loaded (Test)");
        }else{
            Log.d(TAG,"OpenCV loaded successfully (Test)");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        String title;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            title = null;
        } else {
            title = extras.getString("Title");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }

        cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.camera1);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        TextView tv_title = (TextView) findViewById(R.id.title1);
        tv_title.setText("Activating via OpenCV : " + title);
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
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Log.d(TAG,"OpenCV_onCameraFrame");
        rgba = inputFrame.rgba();
        /*Core.transpose(rgba,rgbaT);
        Core.flip(rgbaT,rgbaT,1);
        Imgproc.resize(rgbaT,rgbaT,rgba.size());
        return rgbaT;*/
        return rgba;
        //return inputFrame.rgba();
    }
}
