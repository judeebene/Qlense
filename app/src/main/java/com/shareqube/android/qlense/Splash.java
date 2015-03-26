package com.shareqube.android.qlense;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;


public class Splash extends Activity {

    public static  final long SPLASH_TIME = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {

                // pass the main Finder Activiity

                Intent intent = new Intent(getApplication() , Finder.class);
                startActivity(intent);

                Splash.this.finish();

            }
        } , SPLASH_TIME);


        ComponentName componentToDisable =
                new ComponentName("com.shareqube.android.qlense",
                        "com.shareqube.android.qlense.Splash");

        getPackageManager().setComponentEnabledSetting(
                componentToDisable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);



    }


    @Override
    protected void onPause() {
        super.onPause();

        this.finish();
    }





}
