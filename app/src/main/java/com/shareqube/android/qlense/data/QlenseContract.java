package com.shareqube.android.qlense.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;


public class QlenseContract {


    // content authority is a name for the entire content provider

    public  static final String CONTENT_AUTHORITY  ="com.shareqube.android.qlense" ;


    //use content authority to create base uri

    public static  final Uri BASE_CONTENT_URI  = Uri.parse("content://"+CONTENT_AUTHORITY) ;


    // adding database table name

    public static final String PATH_SETTING = "setting" ;

    public static final String PATH_SIMCHANGE = "simchange" ;

    public static final String PATH_LAST_CALL = "lastcall" ;

    public static final String PATH_LAST_CALL_OUT = "lastOutCall" ;

    public static final String PATH_DEFAULT_SIM  = "defaultsim" ;

    public static final String PATH_SMS_STATUS = "smsstatus" ;


    public static long  normalizeDate(long date){

        Time time = new Time();
        time.setToNow();

        int julianDay = Time.getJulianDay(date ,time.gmtoff);
        return time.setJulianDay(julianDay);

    }


    public static final class SettingTable  implements BaseColumns {

        // building the setting path in the uri

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SETTING).build();

        // resolving te content to content type . i.e a folder type

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETTING ;

        //resolving the content to file type

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SETTING ;






        //tablename

        public static final String TABLE_NAME = "setting" ;


        public  static final String COLUMN_PIN = "pin";



        public static final String COLUMN_SIMNUMBER = "simnumber";


        // building uris for query

        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


    public static final class SimChangeTable implements BaseColumns{


        public static final  Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SIMCHANGE).build();


        // resolve the uri to content and item type ;

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + " /" + CONTENT_AUTHORITY + "/" + PATH_SIMCHANGE ;



        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + " /" + CONTENT_AUTHORITY + "/" +  PATH_SIMCHANGE ;

        // table name

        public static  final String TABLE_NAME ="simchange";

        public static final String COLUMN_TIME = "changetime" ;


        public static final String COLUMN_OLDSERIAL = "oldserial" ;

        public static final String COLUMN_DATE = "changedate";




        //building uris for query

        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class LastCallTable implements  BaseColumns{

        //build the path for lastCall

        public static final  Uri CONTENT_URI  = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LAST_CALL).build();

        // resolve the uri to folder and item type

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LAST_CALL;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +  "/" + PATH_LAST_CALL ;



        public static final String TABLE_NAME = "lastcall" ;

        public static  final String COLUMN_CALLER =  "caller" ;

        public static final String COLUMN_TIME  = "time";

        public static final String COLUMN_DATE = "date" ;


        // building uris for query
        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class LastOutCallTable implements  BaseColumns{

        //  build the uri for lastoutcall table
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LAST_CALL_OUT).build();

        // resolve the uri into folder and item

        public static final String  CONTENT_TYPE  = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LAST_CALL_OUT;

        public static final String CONTENT_ITEM_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LAST_CALL_OUT ;


// table name

        public static final  String TABLE_NAME = "lastOutCall" ;

        public static final String COLUMN_CALLER ="caller" ;

        public static final  String COLUMN_DATE = "date" ;

        public static final String COLUMN_TIME = "time" ;


        // building Uris for query
        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



    }

    public static final class DefaultSimTable implements  BaseColumns{


        // build the uri for defaultsim table

        public static final  Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEFAULT_SIM).build();

        // resolve the path to folder and item

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEFAULT_SIM ;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEFAULT_SIM ;
        public static final String TABLE_NAME = "defaultsim";

        public static final  String COLUMN_LINE1 = "line1" ;

        public static final String COLUMN_LINE2 = "line2";

        // building uris for query
        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class SMSStatusTable implements BaseColumns{

        // bullding uri for the sms status table

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SMS_STATUS).build();

        // resolve the content into folde and item type

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SMS_STATUS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SMS_STATUS ;
        public static final String TABLE_NAME = "smsstatus" ;

        public static final String COLUMN_STATUS = "status" ;


        // build

        public static Uri buildQlenseUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



    }





}
