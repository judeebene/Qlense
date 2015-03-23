package com.shareqube.android.qlense;

/**
 *  Trace the caller.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;

import java.util.Calendar;
import java.util.Locale;


public class CallTrace extends PhoneStateListener {

    String myphone = "";

    static String mytime = "";
    static String mydate = "";

    Context context = null;
    //Activity activity = null;

    TextView txt = null;


    public CallTrace( Context context )
    {
        this.context = context;

        Toast.makeText(context, "Call Trace Started!", Toast.LENGTH_LONG).show();
        //	this.activity = activity;

        //txt = (TextView)activity.findViewById(R.id.textView1);
    }



    public String getLastOutCall()
    {
        String lastCaller = "outgoing:NONE";

        if(Worker.mDBHelper != null && Worker.mQlenseProvider != null)
        {


            Cursor cursor = context.getContentResolver().query(QlenseContract.LastOutCallTable.CONTENT_URI,null,null ,null,null);

            if(cursor != null && cursor.getCount()>0)
            {
                //table is not empty, so update table
                cursor.moveToFirst();

                String caller = cursor.getString(cursor.getColumnIndex("caller"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                lastCaller = "outgoing call:" + caller + "[" + time + "] [" + date + "]";
                Toast.makeText(context, "" + lastCaller , Toast.LENGTH_LONG).show();
            }
        }

        return lastCaller;
    }



    public String getLastCaller()
    {
        String lastCall = "incoming call:NONE";
        // this value will come from the database
        if(Worker.mDBHelper != null && Worker.mQlenseProvider != null)
        {


            Cursor cursor = context.getContentResolver().query(QlenseContract.LastCallTable.CONTENT_URI,null ,null ,null ,null,null) ;

            if(cursor != null && cursor.getCount()>0)
            {
                //table is not empty, so update table
                cursor.moveToFirst();

                String caller = cursor.getString(cursor.getColumnIndex("caller"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                lastCall = "incoming call:" +caller+"["+time+"] ["+date + "]";
                //Toast.makeText(context, lastCall , Toast.LENGTH_LONG).show();
            }

        }

        return lastCall;
    }



    public void saveLastCall(String phone, String time, String date)// this is incoming
    {
        // save the number to database

        if(Worker.mDBHelper != null && Worker.mQlenseProvider != null)
        {


            Cursor cursor = context.getContentResolver().query(QlenseContract.LastCallTable.CONTENT_URI ,null,null,null,null) ;

            if(cursor != null && cursor.getCount()>0)
            {
                //table is not empty, so update table
                cursor.moveToFirst();

                String oldCaller = cursor.getString(cursor.getColumnIndex("caller"));

                 ContentValues last_calls_values = new ContentValues();
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_CALLER ,phone);
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_TIME,time);
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_DATE,date);

                int rowId = context.getContentResolver().update(QlenseContract.LastCallTable.CONTENT_URI ,last_calls_values,"caller" , new String[]{oldCaller}) ;


                Toast.makeText(context, "Caller :" +phone + " |Time :" + time + " |Date :" + date + " - inserted... ", Toast.LENGTH_LONG).show();

            }
            else{
                //table is empty, so insert a record

                ContentValues last_calls_values = new ContentValues();
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_CALLER ,phone);
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_TIME,time);
                last_calls_values.put(QlenseContract.LastCallTable.COLUMN_DATE,date);

                Uri callUri = context.getContentResolver().insert(QlenseContract.LastCallTable.CONTENT_URI ,last_calls_values);
            }

        }
    }



    public static String updateCallTime()
    {
        Calendar cal = Calendar.getInstance(new Locale("US"));

        int am_pm = cal.get(Calendar.AM_PM);

        String amORpm = (am_pm==0)? "AM" : "PM";

        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);

        CallTrace.mytime = hour + ":" + minute + " " + amORpm;

        return CallTrace.mytime;
    }


    public static String updateCallDate()
    {
        Calendar cal = Calendar.getInstance(new Locale("US"));

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        CallTrace.mydate = day + "/" + month + "/" + year;

        return CallTrace.mydate;
    }





    public void onCallStateChanged(int state, String incomingNumber)
    {

        //TelephonyManager.
        switch (state)
        {
            case TelephonyManager.CALL_STATE_RINGING:

                myphone = incomingNumber;

                Toast.makeText(context, "Call on : " + updateCallDate() + " at " + updateCallTime() + " From " + myphone, Toast.LENGTH_LONG).show();

                saveLastCall(myphone, updateCallTime(), updateCallDate());

                System.gc();

                break;

        }

    }

} // end of class
