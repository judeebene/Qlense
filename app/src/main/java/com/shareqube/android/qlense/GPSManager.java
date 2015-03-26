package com.shareqube.android.qlense;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GPSManager {

    Intent gps = new Intent("android.location.GPS_ENABLED_CHANGE");

    String location = "";




    public Intent enableGPS(Context context)
    {
        try{

            gps.putExtra("enabled", true);

            context.sendBroadcast(gps);
        }
        catch(Exception e){ Toast.makeText(context, "An error has occured", Toast.LENGTH_SHORT).show(); }

        return gps;
    }


    public Intent disableGPS(Context context)
    {

        try{

            gps.putExtra("enabled", false);

            context.sendBroadcast(gps);

            //gps.removeExtra("enabled");
        }
        catch(Exception e){ Toast.makeText(context, "An error has occured", Toast.LENGTH_SHORT).show(); }

        return gps;
    }



    public void updateLocation()
    {

        location = "longitude , "+" latitude.";
    }

}
