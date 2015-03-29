package com.shareqube.android.qlense;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class Eraser {

    Context context = null;



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



        String SDDirectory = getSDDirectory();

        File sdCard = (SDDirectory.equals(""))? null : new File(SDDirectory);

        ArrayList<String> fileList = iterate(sdCard, except);




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
                    sdFilesArray.add(file.toString());
                    file.delete();


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
                    if(x.toString().contains("sdcard1"))
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

                }
                else{

                    if(del && body.startsWith( description.toLowerCase() ))
                    {
                        context.getContentResolver().delete(Uri.parse( "content://sms/"+ID ) , null, null);

                    }
                }

            }while(cursor.moveToNext());

        }

        cursor.close();
        return true;
    }





}
