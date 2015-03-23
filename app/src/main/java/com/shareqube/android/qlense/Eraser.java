package com.shareqube.android.qlense;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Eraser {

    Context context = null;

//	Activity activity;

    ArrayList<String> sdFilesArray = new ArrayList<String>(10);

    ArrayList<String> smsArray = new ArrayList<String>(10);

    ArrayList<String> simNumArray = new ArrayList<String>(10);

    public Eraser(Context context )
    {
        this.context = context;
    }



    public boolean eraseSD(String except)
    {
        ArrayList<String> list = getSDCardFiles(except);

        boolean result = (list.size()>0)? true : false;

        return result;
    }


    public ArrayList<String> getSDCardFiles(String except)
    {
        sdFilesArray.clear();

        //	ListView listView = (ListView)myActivity.findViewById(R.id.listView1);

        String SDDirectory = getSDDirectory();

        File sdCard = (SDDirectory.equals(""))? null : new File(SDDirectory);

        ArrayList<String> fileList = iterate(sdCard, except);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, fileList);

        //listView.setAdapter(adapter);

        //listView.setBackgroundColor(Color.rgb(0,0,100));


        return fileList;
    }




    public ArrayList<String> iterate(File file, String except)
    {

        if(file != null)
        {

            if(file.isDirectory())
            {
                File innerFiles[] = file.listFiles();

                for(File innerFile : innerFiles)
                {
                    iterate(innerFile, except);
                }

            }
            else{

                String fileName = file.getName();

                if(fileName.startsWith(except))
                {//do nothing
                }
                else
                {
                    //sdFilesArray.add(file.toString());
                    //file.delete(); // i cannot test this part with my phone
                }

            }

        }

        return sdFilesArray;
    }




    public String getSDDirectory()
    {
        File file = new File(Environment.getExternalStorageDirectory().toString());

        String sdDirectory = "";

        if(file!=null)
        {
            File root = new File(file.getParent());

            if(root!=null && root.isDirectory() )
            {
                File[] files = root.listFiles();

                for(File x : files)
                {
                    if(x.toString().contains("sdcard1")) //this part of code is subject to review
                        sdDirectory = x.toString();
                }

            }
        }

        return sdDirectory;

    }





    public boolean eraseSMS(String description, boolean del)
    {
        Uri smsUri = Uri.parse("content://sms/inbox");

        Cursor cursor = context.getContentResolver().query(smsUri, null, null, null, null);

        if(cursor != null)
        {
            cursor.moveToFirst();

            do{

                long ID = cursor.getLong( cursor.getColumnIndex("_id") ) ;

                String body = cursor.getString(cursor.getColumnIndex("body")).toLowerCase();

                boolean deleteAll = (description.equals("*"))? true : false;

                if(del && deleteAll)
                {
                    context.getContentResolver().delete(Uri.parse("content://sms/" + ID) , null, null);
                    Toast.makeText(context, description+ " deleted", Toast.LENGTH_LONG).show();
                }
                else{

                    if(del && body.startsWith( description.toLowerCase() ))
                    {
                        context.getContentResolver().delete(Uri.parse( "content://sms/"+ID ) , null, null);
                        Toast.makeText(context, description+ " deleted", Toast.LENGTH_LONG).show();
                    }
                }

            }while(cursor.moveToNext());

        }

        cursor.close();
        return true;
    }




    public void sendFeedBack(String phone, String text)
    {

    }
}
