package com.shareqube.android.qlense;

/**
 *  Check the sim card if its change
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;


public class SimChangeChecker {

    ContentResolver cr  ;





    public static boolean check(Context context )
    {
        boolean isChanged = false;




            Cursor cursor  = context.getContentResolver().query(QlenseContract.SimChangeTable.CONTENT_URI, null, null, null, null);

            String changeDate = CallTrace.updateCallDate();
            String changeTime = CallTrace.updateCallTime();

            String serial = getPhoneSerial(context);

            Toast.makeText(context, "Serial to check : " + serial, Toast.LENGTH_LONG).show();

            if(cursor != null && cursor.getCount()>0)
            {
                //table is not empty, so update table
                cursor.moveToFirst();

                String oldserial = cursor.getString(cursor.getColumnIndex("oldserial"));

                isChanged = (oldserial.equalsIgnoreCase(serial))? false : true;

                Toast.makeText(context, "Old: "+ oldserial +" New: " + serial,   Toast.LENGTH_LONG).show();


                ContentValues sim_serial_values = new ContentValues();
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_OLDSERIAL,oldserial);
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_DATE, changeDate);
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_TIME , changeTime);
                int rowId = context.getContentResolver().update(QlenseContract.SimChangeTable.CONTENT_URI,sim_serial_values , QlenseContract.SimChangeTable.COLUMN_OLDSERIAL , new String[]{String.valueOf(oldserial)}) ;

            }
            else{

                isChanged = false;
                Toast.makeText(context, "New Serial Added : " + serial,   Toast.LENGTH_LONG).show();

                //table is empty, so insert a record

                ContentValues sim_serial_values = new ContentValues();
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_OLDSERIAL,serial);
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_DATE, changeDate);
                sim_serial_values.put(QlenseContract.SimChangeTable.COLUMN_TIME , changeTime);
                context.getContentResolver().insert(QlenseContract.SimChangeTable.CONTENT_URI, sim_serial_values );

            }



        return isChanged;
    }





    public static String getPhoneSerial(Context context)
    {
        TelephonyManager tele = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        int phoneType = tele.getPhoneType();

        String serial = "";

        if(phoneType == tele.PHONE_TYPE_GSM)
        {
            if(tele.getSimState() != tele.SIM_STATE_ABSENT)
            {
                serial = tele.getSimSerialNumber();
            }
        }

        return serial;
    }



    public String simState(Context context)
    {
        TelephonyManager tele = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        String state = "";

        if(tele.getPhoneType() == tele.PHONE_TYPE_GSM)
        {
            state = tele.getSimState() + "";
        }

        return state;
    }


    public String getOperator(Context context )
    {
        TelephonyManager tele = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = "";

        if(tele.getPhoneType() == tele.PHONE_TYPE_GSM)
        {
            operator = tele.getNetworkOperatorName() + "";
        }

        return operator;
    }

}
