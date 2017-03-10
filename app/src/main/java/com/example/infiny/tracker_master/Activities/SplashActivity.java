package com.example.infiny.tracker_master.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.infiny.tracker_master.R;
import com.yayandroid.locationmanager.LocationBaseActivity;
import com.yayandroid.locationmanager.LocationConfiguration;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.constants.LogType;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity  extends LocationBaseActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onResume() {
        super.onResume();
        nextScreen();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return null;
    }

    @Override
    public void onLocationFailed(int failType) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    private boolean checkPermission() {
        int r1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int r2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int r3 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int r4 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int r5 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int r6 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return r1 == PackageManager.PERMISSION_GRANTED && r2 == PackageManager.PERMISSION_GRANTED &&  r3 == PackageManager.PERMISSION_GRANTED &&  r4 == PackageManager.PERMISSION_GRANTED && r5 == PackageManager.PERMISSION_GRANTED && r6== PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA,RECORD_AUDIO,WRITE_EXTERNAL_STORAGE,ACCESS_COARSE_LOCATION,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean recordAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
//                    boolean coarseAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
//                    boolean readAccepted = grantResults[5] == PackageManager.PERMISSION_GRANTED;

//                    if (locationAccepted && cameraAccepted && recordAccepted && writeAccepted && coarseAccepted && readAccepted ){
                        LocationManager.setLogType(LogType.GENERAL);
                        getLocation();
                        nextScreen();

                    }
                else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)||shouldShowRequestPermissionRationale(CAMERA)||shouldShowRequestPermissionRationale(RECORD_AUDIO)||shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)||shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{ACCESS_FINE_LOCATION, CAMERA,RECORD_AUDIO,WRITE_EXTERNAL_STORAGE,ACCESS_COARSE_LOCATION,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }else{
                                new AlertDialog.Builder(SplashActivity.this)

                                        .setMessage("You have denied all permissions,You would not be able to access the application")
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
//                }


                break;
        }
    }

    private void nextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    requestPermission();
                } else {
                    startActivity(new Intent(SplashActivity.this,Home.class));
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .show();
    }
}
