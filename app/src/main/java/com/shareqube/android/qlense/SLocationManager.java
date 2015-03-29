package com.shareqube.android.qlense;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class SLocationManager implements LocationListener{


    Context context;

    double lat ;
    double longi ;

    public SLocationManager(Context context)
    {
        this.context = context;
    }


    @Override
    public void onProviderDisabled(String provider){


    }


    @Override
    public void onProviderEnabled(String provider) {


        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Location loca = lm.getLastKnownLocation(lm.getProvider(LocationManager.GPS_PROVIDER).getName());


        if (loca != null) {
            lat = loca.getLatitude();
            longi = loca.getLongitude();

        }
        else{

        }
    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "Location changed" + arg0.getLatitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "status changed", Toast.LENGTH_LONG).show();

    }


}
