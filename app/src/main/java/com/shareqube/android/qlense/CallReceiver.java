package com.shareqube.android.qlense;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.shareqube.android.qlense.data.QlenseContract;

public class CallReceiver extends BroadcastReceiver {

   public static String MAKE_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
    String number;
    Context context;

    public CallReceiver(Context context ) {
        this.context = context;
    }

    public CallReceiver(){};



    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(MAKE_CALL_ACTION))
        {
            Intent i = new Intent();

            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            saveLastOutCall(number, CallTrace.updateCallTime(), CallTrace.updateCallDate());
        }

    }



    public void saveLastOutCall(String phone, String time, String date)
    {
        if(Worker.mDBHelper != null && Worker.mQlenseProvider != null)
        {
            Cursor cursor = context.getContentResolver().query(QlenseContract.LastOutCallTable.CONTENT_URI,null,null,null,null);


            if(cursor != null && cursor.getCount()>0)
            {
                //table is not empty, so update table
                cursor.moveToFirst();

                String oldCaller = cursor.getString(cursor.getColumnIndex("caller"));



                ContentValues numberValues = new ContentValues();
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_CALLER, phone);
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_TIME , time);
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_DATE, date);

                int rowId = context.getContentResolver().update(QlenseContract.LastOutCallTable.CONTENT_URI ,numberValues,"caller" ,  new String[]{ String.valueOf( oldCaller )});



                 }
            else{


                ContentValues numberValues = new ContentValues();
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_CALLER, phone);
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_TIME , time);
                numberValues.put(QlenseContract.LastOutCallTable.COLUMN_DATE, date);

              context.getContentResolver().insert(QlenseContract.LastOutCallTable.CONTENT_URI , numberValues);



            }
        }
    }




}
