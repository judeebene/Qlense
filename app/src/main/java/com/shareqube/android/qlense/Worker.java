package com.shareqube.android.qlense;

/**
 * maain background service
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;
import com.shareqube.android.qlense.data.QlenseDBHelper;
import com.shareqube.android.qlense.data.QlenseProvider;

public class Worker extends Service {


    static QlenseDBHelper mDBHelper;
    static PhoneStateUpdater cellListener;
    static CallTrace callListener;
    static IntentFilter smsIntent;
    static QlenseProvider mQlenseProvider;
    static SmsReceiver smsReceiver;
    static SimChangeChecker simChangeChecker;
    static TextMessageManager smsManager;
    static ContactsCopy contacts;
    static CallReceiver call;
    static Eraser eraser;
    static ContactsCopy copy;




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        //----------------- Delay ------------------------------------------------
        Handler delay = new Handler();
        //------------------------------------------------------------------------


        delay.postDelayed(new Runnable()
        {
            public void run()
            {


                Toast.makeText(getApplicationContext(), "QLense Started!", Toast.LENGTH_LONG).show();





                //--------------------- sms manager ----------------------------------------------------
                smsManager = new TextMessageManager();
                //--------------------------------------------------------------------------------------


                // ------------------- SMS LISTENER ----------------------------------------------------
                smsIntent = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                smsReceiver = new SmsReceiver(Worker.this.getApplicationContext());
                registerReceiver(smsReceiver, smsIntent);
                // -------------------------------------------------------------------------------------


                // --------------------- CALL LISTENER -------------------------------------------------
                callListener = new CallTrace( getApplicationContext());

                String callService = Context.TELEPHONY_SERVICE;
                TelephonyManager callTelephonyManager = (TelephonyManager)getSystemService(callService);
                callTelephonyManager.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
                // -------------------------------------------------------------------------------------


                //-------------------- outgoing call listener ------------------------------------------
                IntentFilter callIntent = new IntentFilter();
                callIntent.addAction(CallReceiver.MAKE_CALL_ACTION);
                call = new CallReceiver(Worker.this.getApplicationContext());
                registerReceiver(call, callIntent);

                //--------------------------------------------------------------------------------------


                // ------------------- CELL ID LISTENER ------------------------------------------------
                cellListener = new PhoneStateUpdater( getApplicationContext());

                String cellService = Context.TELEPHONY_SERVICE;
                TelephonyManager cellTelephonyManager = (TelephonyManager)getSystemService(cellService);
                cellTelephonyManager.listen(cellListener, PhoneStateListener.LISTEN_CELL_LOCATION);
                // -------------------------------------------------------------------------------------



                //----------------------- Sim Change ---------------------------------------------------
                simChangeChecker = new SimChangeChecker();
                //--------------------------------------------------------------------------------------
                Toast.makeText(Worker.this.getApplicationContext(), simChangeChecker.simState(Worker.this.getApplicationContext()), Toast.LENGTH_LONG).show();
                // ----------------------- Erase -------------------------------------------------------
                eraser = new Eraser( Worker.this.getApplicationContext() );
                //--------------------------------------------------------------------------------------


                // ------------------------- Contacts Copy ---------------------------------------------
                contacts = new ContactsCopy( Worker.this.getApplicationContext() );
                // -------------------------------------------------------------------------------------

                boolean simCheck = simChangeChecker.check(Worker.this.getApplicationContext());
                //======================================================================================


                String defaultNumber = smsReceiver.getDefaultSim("line1");
               //Toast.makeText(Worker.this.getApplicationContext(), "  Default Number: " + defaultNumber, Toast.LENGTH_LONG).show(); // we assume this phone has been stolen

                String sms = "Your Phone sim card has been Change,  save the following details::" +
                        "jh" +
                        " " + smsReceiver.getUserPin() + "::" + simChangeChecker.getOperator( Worker.this.getApplicationContext() );

                if(simCheck){

                    //get sim pin
                    //get sim operator

                    smsManager.sendSMS(Worker.this.getApplicationContext(), defaultNumber, sms);
                   // Toast.makeText(Worker.this.getApplicationContext(), " There is a change in sim card ", Toast.LENGTH_LONG).show(); // we assume this phone has been stolen
                }
                else{
                    sendSMS(defaultNumber, sms);
                   // Toast.makeText(Worker.this.getApplicationContext(), " There is NO change in sim card ", Toast.LENGTH_LONG).show(); // we assume this phone has been stolen
                }





            }
        }, 50000 );




        return START_STICKY;
    }


    @Override
    public void onDestroy()
    {

        cellListener = null;
        callListener = null;
        smsIntent = null;

        System.gc();

        super.onDestroy();
    }



    public String getSmsStatus()
    {


        Cursor cursor =  this.getApplicationContext().getContentResolver().query(QlenseContract.SMSStatusTable.CONTENT_URI , null,null,null,null);

        int row = cursor.getCount();

        String status = "";
        Toast.makeText(this.getApplicationContext(), " SMS STatus queried" , Toast.LENGTH_LONG).show();

        //Toast.makeText(context, "row: " + row, Toast.LENGTH_LONG).show();

        if(row>0)
        {
            cursor.moveToFirst();

            status = cursor.getString(cursor.getColumnIndex("status"));
        }
        else{
            Toast.makeText(this.getApplicationContext(), " SMS STatus is empty", Toast.LENGTH_LONG).show();

            //dbProvider.insert(SettingsTable.tableName, new String[]{"pin", }, new String[]{"-", "-"});
        }


        return status;
    }



    public void sendSMS(String number, String sms)
    {
        //if last sms sending was not successfull, try to send is again
        String smsStatus = getSmsStatus();
        Toast.makeText(this.getApplicationContext(), " SMS STATUS : " + smsStatus , Toast.LENGTH_LONG).show();

        if(smsStatus.equals("false"))
        {
            smsManager.sendSMS(this.getApplicationContext(), number, sms);
        }

        //Toast.makeText(getApplicationContext(), "Your sim properties has not changed", Toast.LENGTH_LONG).show(); // we assume this phone has been stolen
    }

}
