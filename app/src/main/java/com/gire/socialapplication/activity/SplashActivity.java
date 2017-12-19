package com.gire.socialapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.gire.socialapplication.R;
import com.gire.socialapplication.constants.CommonConstants;
import com.gire.socialapplication.controller.WebInterface;
import com.gire.socialapplication.controller.WebServiceController;
import com.gire.socialapplication.gpsLocation.GPSTracker;
import com.gire.socialapplication.interfaces.LocationUpdate;
import com.gire.socialapplication.service.UpdateUserLocation;
import com.gire.socialapplication.userPreference.SharedPreferencesUtil;
import com.loopj.android.http.RequestParams;

import static com.gire.socialapplication.constants.APIConstants.UPDATE_LOCATION;

/**
 * Created by girish on 6/14/2017.
 */

public class SplashActivity extends AppCompatActivity implements LocationUpdate,WebInterface {

    GPSTracker gpsTracker;
    String[] perms = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.CAMERA","android.permission.SEND_SMS","android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS","android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;
    Handler gpsHandler;
    Integer caseValue =0;
    WebServiceController webServiceController;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        gpsTracker = new GPSTracker(SplashActivity.this);
        webServiceController = new WebServiceController(SplashActivity.this,SplashActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(perms, permsRequestCode);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callGpsCheck();
                }
            },5000);

        }else {
            if(SharedPreferencesUtil.getInstance(this).getCustomerID() != null){
                caseValue = 2;
                callGpsCheck();
            }else {
                caseValue = 1;
                callGpsCheck();
            }
        }

    }

    private void callNextAction() {
        Intent intent = null;

        switch (caseValue){
            case 1:
                // redirect to login
                intent = new Intent(SplashActivity.this,LoginActivity.class);
                break;
            case 2:
                intent = new Intent(SplashActivity.this,HomeActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }

    private void callGpsCheck() {
        if (gpsTracker.canGetLocation()) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callLocationFetch();
                }
            },3000);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callLocationFetch();
                }
            },3000);

        }
    }

    private void callLocationFetch() {
        gpsHandler = new Handler();
        gpsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gpsTracker.getLatitude() == 00000){
                    gpsTracker = new GPSTracker(SplashActivity.this);
                    gpsHandler.postDelayed(this, 3000);
                }else {
                    gpsHandler.removeCallbacks(this);
                    gpsTracker.stopUsingGPS();

                    /* make a API request */
                    CommonConstants.latitude = gpsTracker.getLatitude();
                    CommonConstants.longitude = gpsTracker.getLongitude();

                    if(caseValue == 2){
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("lattitude",String.valueOf(CommonConstants.latitude));
                        requestParams.put("longitude",String.valueOf(CommonConstants.longitude));
                        requestParams.put("user_id", SharedPreferencesUtil.getInstance(SplashActivity.this).getCustomerID());

                        webServiceController.postRequest(UPDATE_LOCATION,requestParams,1);

                    }else {
                        callNextAction();
                    }
                }
            }
        },500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(permsRequestCode){

            case 200:

                boolean fineLocation = grantResults[0]== PackageManager.PERMISSION_GRANTED;

                boolean coreLocation = grantResults[1]== PackageManager.PERMISSION_GRANTED;

                boolean cameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                boolean sendSMS = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                boolean readSMS = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                boolean receiveSMS = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                boolean readStorage = grantResults[6] == PackageManager.PERMISSION_GRANTED;

                boolean writeStorage = grantResults[7] == PackageManager.PERMISSION_GRANTED;

                break;

        }
    }
    /* */
    private boolean canMakeSmores(){

        return(Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1);

    }

    private boolean hasPermission(String permission){

        if(canMakeSmores()){

            return(checkSelfPermission(permission)==PackageManager.PERMISSION_GRANTED);

        }

        return true;

    }

    public int checkSelfPermission(String permission) {
        return 1;
    }

    @Override
    public void locationUpdate() {
        callNextAction();
    }

    @Override
    public void getResponse(String response, int flag) {
        callNextAction();
    }

}
