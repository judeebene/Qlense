package com.shareqube.android.qlense;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsCopy {

    Context context;

    ArrayList<String> phoneContactsArray = new ArrayList<String>(10);
    ArrayList<String> simContactsArray = new ArrayList<String>(20);


    public ContactsCopy( Context context )
    {
        this.context = context;
    }


    public ArrayList<String> copyPhoneContacts()
    {
        phoneContactsArray.clear();

        Uri numberUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; //phone contacts uri

        Cursor cursor = context.getContentResolver().query(numberUri, null, null, null, null);

//		ListView listView = (ListView)activity.findViewById(R.id.listView1);

        if(cursor!=null){

            cursor.moveToFirst();

            int numberIndex=0;

            do
            {
                numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                phoneContactsArray.add( cursor.getString(nameIndex) + "::" + cursor.getString(numberIndex));

            }while( cursor.moveToNext() );

            //		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, phoneContactsArray);

            //	listView.setAdapter(adapter);

            //listView.setBackgroundColor(Color.rgb(0,0,100));

        }

        return phoneContactsArray;

    }





    public ArrayList<String> copySimContacts()
    {
        simContactsArray.clear();

        //ListView listView = (ListView)activity.findViewById(R.id.listView1);

        Uri simUri = ContactsContract.Contacts.CONTENT_URI;// sim contacts uri

        simUri = Uri.parse("content://icc/adn");

        Cursor cursor = context.getContentResolver().query(simUri, null, null, null, null);

        if(cursor!=null){

            Toast.makeText(context, simUri.toString(), Toast.LENGTH_LONG).show();

            cursor.moveToFirst();

            int nameIndex=0;
            int numberIndex=0;

            do
            {

                numberIndex = cursor.getColumnIndex("number");
                nameIndex = cursor.getColumnIndex("name");

                simContactsArray.add( cursor.getString( nameIndex ) + "::" +  cursor.getString(numberIndex));

            }while( cursor.moveToNext() );


            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, simContactsArray);

            //listView.setAdapter(adapter);

            //listView.setBackgroundColor(Color.rgb(0,0,100));


        }

        return simContactsArray;
    }




}
