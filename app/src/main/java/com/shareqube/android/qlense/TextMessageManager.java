package com.shareqube.android.qlense;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;


public class TextMessageManager {


    SmsManager smsM = SmsManager.getDefault();

    String phoneNumber = "";
    String message = "";

    Intent intent = new Intent(Intent.ACTION_CALL);

    PendingIntent smsState = null;


    public boolean sendSMS(Context context, String number, String msg)
    {
        // tested
        //the code below does not select the users sim in case of a dual sim phone
        boolean status = false;

        try{

            phoneNumber = number;
            message = msg;

            if(phoneNumber.length() >=11)
            {
                smsState = PendingIntent.getBroadcast(context, 0, new Intent("DELIVERED"), 0);
                PendingIntent sendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("SENDING"), 0);

                context.registerReceiver(new smsReceivedMonitor(), new IntentFilter("DELIVERED"));
                context.registerReceiver(new smsSendMonitor(), new IntentFilter("SENDING"));

                smsM.sendTextMessage(phoneNumber, null, msg, sendingIntent, smsState);


                Toast.makeText(context, "sms sent to "+phoneNumber, Toast.LENGTH_SHORT).show();

                status = true;
            }
            else{
                Toast.makeText(context, "This number seems invalid ", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            status = false;
            Toast.makeText(context, "sms sending failed", Toast.LENGTH_SHORT).show();
        }

        return status;
    }



    public void updateSmsStatus(Context c, String newStatus)
    {



        Cursor cursor = c.getContentResolver().query(QlenseContract.SMSStatusTable.CONTENT_URI,null,null,null,null);

        int len = cursor.getCount();

        if(cursor != null && len>0){

            cursor.moveToFirst();

            String oldStatus = cursor.getString(cursor.getColumnIndex("status"));

            ContentValues statusValue = new ContentValues();
            statusValue.put(QlenseContract.SMSStatusTable.COLUMN_STATUS,oldStatus);

            c.getContentResolver().update(QlenseContract.SMSStatusTable.CONTENT_URI , statusValue ,"status" ,new String[]{oldStatus}) ;

             Toast.makeText(c, "SMS STATUS UPDATED!", Toast.LENGTH_LONG).show();
        }
    }



    public void makeCall( Context context, String phone)
    {
        intent.setData(Uri.parse("tel:" + phone));


        try{

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            //activity.finish();

        }
        catch(android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(context, "Call failed!", Toast.LENGTH_LONG).show();
        }
    }




    class smsReceivedMonitor extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context c, Intent i) {

            Toast.makeText(c, " message feedback received ", Toast.LENGTH_LONG).show();
            String smsStatus = "true";

            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    Toast.makeText(c, "SMS delivered", Toast.LENGTH_SHORT).show();
                    smsStatus = "true";
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(c, "SMS not delivered", Toast.LENGTH_SHORT).show();
                    smsStatus = "false";
                    break;
            }

            //update database
            updateSmsStatus( c, smsStatus);
        }

    }



    class smsSendMonitor extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context c, Intent i) {

            Toast.makeText(c, " message sending monitored! ", Toast.LENGTH_LONG).show();
            String smsStatus = "true";

            switch (getResultCode())
            {
                case Activity.RESULT_OK:
                    Toast.makeText(c, "SMS sent", Toast.LENGTH_SHORT).show();
                    smsStatus = "true";
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(c, "Generic failure", Toast.LENGTH_SHORT).show();
                    smsStatus = "false";
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(c, "No service", Toast.LENGTH_SHORT).show();
                    smsStatus = "false";
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(c, "Null PDU", Toast.LENGTH_SHORT).show();
                    smsStatus = "false";
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(c, "Radio off", Toast.LENGTH_SHORT).show();
                    smsStatus = "false";
                    break;
            }

            //update database
            updateSmsStatus( c, smsStatus);
        }

    }




}
