package com.shareqube.android.qlense;


import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;
import com.shareqube.android.qlense.data.QlenseDBHelper;

import java.util.ArrayList;

public class SmsReceiver extends BroadcastReceiver {

    protected static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    String receivedText = "";

    Context context;

    QlenseDBHelper dbDBhelper;

    String commander = "";

    public SmsReceiver(Context context) {

        this.context = context;


        dbDBhelper = new QlenseDBHelper(context);


    }

    public SmsReceiver(){};




    @Override
    public void onReceive(Context context, Intent intent) {


        this.context = context;

        if(intent.getAction().equals( SMS_RECEIVED ))
        {

            Bundle bundle = intent.getExtras();

            if(bundle != null)
            {
                Object[] pdus = (Object[])bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for(int i=0; i<pdus.length; i++ )
                {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }


                for(SmsMessage message : messages)
                {
                    receivedText = message.getMessageBody();

                    commander = message.getOriginatingAddress();

                    // commnder store the message sender number

                   // Toast.makeText(context, "Commander = "+commander, Toast.LENGTH_LONG).show();

                    ArrayList<String> userCommands = new ArrayList<String>();
                    userCommands.add("erasesd");
                    userCommands.add("callme");
                    userCommands.add("lastcaller");
                    userCommands.add("erasesms");
                    userCommands.add("copysim");
                    userCommands.add("changesim1");
                    userCommands.add("changesim2");
                    userCommands.add("loadcard");
                    userCommands.add("location");
                    userCommands.add("viewpin");




                    //split text and check for password match
                    String split[] = splitSms(receivedText);

                    if(split != null)
                    {
                        String pin = getUserPin();

                        if( split.length>=2 ) // if splitting is successful, and there is password and command text
                        {
                            // test for password validity
                            boolean isAppCommand = ( split[0].equalsIgnoreCase( pin ) && userCommands.contains( split[1].toLowerCase() ) ) ? true : false;

                            if(isAppCommand)
                            {
                                this.abortBroadcast();

                                takeAction(pin, split, split.length);

                            }
                            else if( ( split[0].equalsIgnoreCase( "@shareqube.com" ) ) && split[1].toLowerCase().equalsIgnoreCase("viewpin") )
                            {
                                this.abortBroadcast();

                                takeAction(pin, split, split.length);

                                Toast.makeText(context, "trying to view pin!", Toast.LENGTH_LONG).show();

                            }
                            else if(split[0].equalsIgnoreCase( pin )){

                                this.abortBroadcast();

                                Toast.makeText(context, "you have made mistake with the command!", Toast.LENGTH_LONG).show();

                            }
                            else{

                                Toast.makeText(context, "This sms is not for me!", Toast.LENGTH_LONG).show();

                            }
                        }


                    }//end of if split != null

                }//end of for
            }
        }
    } // end of method





    public String getDefaultSim(String colName)
    {



        Cursor cursor = context.getContentResolver().query(QlenseContract.DefaultSimTable.CONTENT_URI,null,null,null,null);

        int row = cursor.getCount();

        String defaultSim = "";


        if(row>0)
        {
            cursor.moveToFirst();
            defaultSim = cursor.getString(cursor.getColumnIndex(colName));
        }
        else{
            // find a way to get the initial phone number

            ContentValues defaultNumbersValues = new ContentValues();
            defaultNumbersValues.put(QlenseContract.DefaultSimTable.COLUMN_LINE1,"08127732305");
            defaultNumbersValues.put(QlenseContract.DefaultSimTable.COLUMN_LINE2 ,"08189100368");

            context.getContentResolver().insert(QlenseContract.DefaultSimTable.CONTENT_URI ,defaultNumbersValues);

              defaultSim = "08085931000";
        }

        //Toast.makeText(context, "Sim to call is: " + defaultSim, Toast.LENGTH_LONG).show();


        return defaultSim;
    }





    public void changeDefaultSim(String line, String newNumber)
    {

        Cursor cursor = context.getContentResolver().query(QlenseContract.DefaultSimTable.CONTENT_URI, null,null,null,null);
        int row = cursor.getCount();

        String defaultSim = "";

        if(row>0)
        {
            cursor.moveToFirst();
            defaultSim = cursor.getString(cursor.getColumnIndex(line));

            Toast.makeText(context, "OLD SIM for " + line + " : " + defaultSim, Toast.LENGTH_LONG).show();
            Toast.makeText(context, "Current SIM for " + line + " : " + newNumber, Toast.LENGTH_LONG).show();

                        ContentValues cv = new ContentValues();
              cv.put(QlenseContract.DefaultSimTable.COLUMN_LINE1 ,newNumber);

            int rowId = context.getContentResolver().update(QlenseContract.DefaultSimTable.CONTENT_URI,cv, line ,new String[]{String.valueOf(defaultSim)});
            Toast.makeText(context, "Done at" + rowId, Toast.LENGTH_LONG).show();
        }
        else{

            ContentValues lineValues = new ContentValues();
            lineValues.put(QlenseContract.DefaultSimTable.COLUMN_LINE1, line);
            lineValues.put(QlenseContract.DefaultSimTable.COLUMN_LINE2,line);

            context.getContentResolver().insert(QlenseContract.DefaultSimTable.CONTENT_URI ,lineValues );
        }
    }





    //get pin from database
    public String getUserPin()
    {
        String pin = "";



         Cursor cursor = context.getContentResolver().query(QlenseContract.SettingTable.CONTENT_URI ,null , null , null , null);

        int row = cursor.getCount();

        //Toast.makeText(context, "row: " + row, Toast.LENGTH_LONG).show();

        if(row>0)
        {
            cursor.moveToFirst();
            pin = cursor.getString(cursor.getColumnIndex("pin"));
        }
        else{
            String sim = SimChangeChecker.getPhoneSerial(context) ;
            ContentValues defaultPinValues = new ContentValues();
            defaultPinValues.put(QlenseContract.SettingTable.COLUMN_PIN , "8080");
            defaultPinValues.put(QlenseContract.SettingTable.COLUMN_SIMNUMBER , sim);

            context.getContentResolver().insert(QlenseContract.SettingTable.CONTENT_URI , defaultPinValues);


        }

        //Toast.makeText(context, "Your pin is: " + pin, Toast.LENGTH_LONG).show();
        //	}

        return pin;
    }




    public String[] splitSms(String sms)
    {
        String msg = sms.toLowerCase();

        String splitedSms[] = null;

        if(msg.contains(":"))
        {
            splitedSms = msg.split(":");
        }

        return splitedSms;
    }



    public void takeAction(String pin, String text[], int size)
    {
        String action = (size>=2)? text[1] : ""; //there must be a number part in the command

        Toast.makeText(context, " Action : " + action , Toast.LENGTH_LONG).show();


        if( size==2 && action.equalsIgnoreCase("eraseSD"))
        {
            boolean result = Worker.eraser.eraseSD("Qlense.apk");
            Toast.makeText(context, "Erase SD Effected " + result , Toast.LENGTH_LONG).show();
        }
        else if( size==2 && action.equalsIgnoreCase("callMe") )
        {
            Worker.smsManager.makeCall(context, commander);
        }
        else if( size==2 && action.equalsIgnoreCase("lastcaller") )
        {
            Toast.makeText(context, "last call Effected " + commander , Toast.LENGTH_LONG).show();

            String lastInCaller = Worker.callListener.getLastCaller();
            String lastOutCaller = Worker.callListener.getLastOutCall();

            String lastCaller = lastInCaller + "\n" + lastOutCaller;

            Worker.smsManager.sendSMS( context, commander, lastCaller);
        }
        else if( size==3 && action.equalsIgnoreCase("eraseSMS") )
        {
            String what = text[2];
            Worker.eraser.eraseSMS(what, true);
            //Toast.makeText(context, what + " is being erased", Toast.LENGTH_LONG).show();
        }
        else if( size==2 && action.equalsIgnoreCase("copysim") )
        {
            ArrayList<String> phoneContacts = Worker.contacts.copyPhoneContacts();
            String contactText = getContactText(phoneContacts);
            Toast.makeText(context, " Copied Contact: " + contactText, Toast.LENGTH_LONG).show();
            Worker.smsManager.sendSMS( context, commander, contactText);

        }
        else if( size==3 && action.equalsIgnoreCase("changeSim1") )
        {
            String newNumber = text[2];
            changeDefaultSim("line1", newNumber);
        }
        else if( size==3 && action.equalsIgnoreCase("changeSim2") )
        {
            String newNumber = text[2];
            changeDefaultSim("line2", newNumber);
        }
        else if(size==2 && action.equalsIgnoreCase("viewpin") )
        {
            String yourPin = "Your pin is:" + pin;
            //Worker.smsManager.sendSMS( context, commander, yourPin);
            Toast.makeText(context, yourPin, Toast.LENGTH_LONG).show();
        }
        else if( size==3 && action.equalsIgnoreCase("loadCard") )
        {
            Toast.makeText(context, "Card Loaded!", Toast.LENGTH_LONG).show();
            String cardCommand = text[2];

            if(cardCommand.startsWith("*") && cardCommand.endsWith("#"))
            {
                cardCommand = cardCommand.substring(0, cardCommand.length()-1) + Uri.encode("#");
            }

            Toast.makeText(context, cardCommand, Toast.LENGTH_LONG).show();
            Worker.smsManager.makeCall( context, cardCommand );

        }
        else if( action.equalsIgnoreCase("location") )
        {
            //Toast.makeText(context, "Location queried", Toast.LENGTH_LONG).show();
        }
        else{}


    }



    public String getContactText(ArrayList<String> phoneContact)
    {

        int n = phoneContact.size();

        String pContact = "";

        if(n>0 && n<7)
        {
            for(int k = n-7; k<n ; k++)
            {
                pContact = pContact + (phoneContact.get(k));
            }

        }
        else if(n>0 && n>7)
        {
            for(int k = n-5; k<n ; k++)
            {
                pContact = pContact + (phoneContact.get(k));
            }
        }
        else{

            pContact = "";
        }

        return pContact;
    }



}