package com.shareqube.android.qlense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent i = new Intent();
            i.setAction("com.shareqube.android.qlense.Worker");
            context.startService(i);



            Toast.makeText(context, "QLense System Booting!", Toast.LENGTH_LONG).show();


        }


    }
}
