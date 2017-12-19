package com.gire.socialapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.gire.socialapplication.R;
import com.gire.socialapplication.interfaces.ImageNotify;

import java.io.IOException;

/**
 * Created by girish on 6/17/2017.
 */

public class CustomCameraFragment extends Fragment implements Camera.PictureCallback,SurfaceHolder.Callback {

    View view;
    ImageView takeImage,flashLight;
    SurfaceView surfaceView;
    LayoutInflater controlInflater = null;
    SurfaceHolder surfaceHolder;
    private ZoomControls zoomControls ;
    private RelativeLayout pCameraLayout = null;
    Camera camera;
    private boolean mIsCapturing;
    boolean previewing = false;
    int currentZoomLevel = 0, maxZoomLevel = 0;
    Camera.Parameters params ;
    Integer countValue = 0;
    ImageNotify imageNotify;

    @SuppressLint("ValidFragment")
    public CustomCameraFragment(Object object) {
        imageNotify = (ImageNotify) object;
    }

    public CustomCameraFragment(){
        /**
         * empty constructor
         * */
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_camera_fragment,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);

        takeImage = (ImageView) view.findViewById(R.id.take_pic);
        flashLight = (ImageView) view.findViewById(R.id.flash_light);

        surfaceView = (SurfaceView) view.findViewById(R.id.my_camera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, CustomCameraFragment.this);
            }
        });

        flashLight.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                    if(countValue == 0){
                        countValue = 1;
                        //flashLight.setImageResource(R.drawable.flash_on);
                        //flashLight.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.flash_on));
                        if (camera == null || params == null) {
                            return;
                        }

                        params = camera.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(params);
                        camera.startPreview();

                    }else {
                        countValue = 0;
                        //flashLight.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.flash_off));
                        if (camera == null || params == null) {
                            return;
                        }

                        params = camera.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(params);
                        camera.startPreview();

                    }
                }else {
                    Toast.makeText(getActivity(), "Flash Light Not available in the device.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {

        Camera.CameraInfo info =
                new Camera.CameraInfo();

        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:degrees = 0;break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
        imageNotify.imageNotify(bitmap);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
                params = camera.getParameters();
                setCameraDisplayOrientation(getActivity(),1,camera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

}