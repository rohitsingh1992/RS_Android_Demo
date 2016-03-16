package com.example.rohit.sendingsms;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Rohit on 2/11/2016.
 */
public class GPSTracker extends Service implements LocationListener {

    Context mContext;
    Location mLocation;

    boolean isGPSEnabled;
    boolean isNetworkEnabled;

    boolean canGetLocation = false;

    double mLatitude = 0.0;
    double mLongitude = 0.0;

    protected LocationManager mLocationManager;

    private static final int MIN_DISTANCE = 0; // 10 Meters
    private static final int MIN_TIME = 0; //  1 Second

    public GPSTracker(Context context) {
        mContext = context;
        getLocation();
    }

    public Location getLocation() {

        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled == false && isGPSEnabled == false) {
            return null;
        } else
        {
            this.canGetLocation = true;

            int status = mContext.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
                    mContext.getPackageName());

            if (status == PackageManager.PERMISSION_GRANTED)
            {
                if (isNetworkEnabled == true)
                {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null)
                    {
                        mLongitude = mLocation.getLongitude();
                        mLatitude = mLocation.getLatitude();
                    } else
                    {
                        return null;
                    }
                }
                if (isGPSEnabled == true)
                {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null)
                    {
                        mLongitude = mLocation.getLongitude();
                        mLatitude = mLocation.getLatitude();
                    } else
                    {
                        return null;
                    }
                }
            }
            else
            {
                Log.e("Tag","Permisions not granted");
                return null;
            }
        }
        return mLocation;

    }

    public boolean canGetLocation(){
        return canGetLocation;
    }

    public double getLatitude(){
        if (mLocationManager != null){
            return mLatitude;
        }
        return mLatitude;
    }

    public double getLongitude(){
        if (mLocationManager != null){
            return mLongitude;
        }
        return mLatitude;
    }


    @Override
    public void onLocationChanged(Location location) {
            if (mLocationManager != null){
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
            }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showLocationSettingDialoge(){
        final AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("Settings");
        ab.setMessage("Are you sure to switch on the GPS ?");
        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intentSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intentSettings);
                dialogInterface.cancel();
            }
        });

        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        ab.show();
    }
}
