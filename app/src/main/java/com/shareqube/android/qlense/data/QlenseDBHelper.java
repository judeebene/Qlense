package com.shareqube.android.qlense.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shareqube.android.qlense.data.QlenseContract.DefaultSimTable;
import com.shareqube.android.qlense.data.QlenseContract.LastCallTable;
import com.shareqube.android.qlense.data.QlenseContract.LastOutCallTable;
import com.shareqube.android.qlense.data.QlenseContract.SettingTable;
import com.shareqube.android.qlense.data.QlenseContract.SimChangeTable;
import com.shareqube.android.qlense.data.QlenseContract.SMSStatusTable ;



public class QlenseDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3 ;

    public static  final String DATABASE_NAME = "qlense.db";

    public QlenseDBHelper(Context context){

        super(context ,DATABASE_NAME ,null ,DATABASE_VERSION);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        // sql create for settingTable

        final String SQL_CREATE_SETTING_TABLE = " CREATE TABLE " + SettingTable.TABLE_NAME + " ( " +
                 SettingTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 SettingTable.COLUMN_PIN + " TEXT  , " +
                 SettingTable.COLUMN_SIMNUMBER + " TEXT   ); " ;

         db.execSQL(SQL_CREATE_SETTING_TABLE);

        // sql create for simchangeTable

        final String SQL_CREATE_SIM_CHANGE_TABLE = " CREATE TABLE " + SimChangeTable.TABLE_NAME + " ( " +
                SimChangeTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "  +
                SimChangeTable.COLUMN_DATE + " TEXT NOT NULL , " +
                SimChangeTable.COLUMN_OLDSERIAL + " TEXT NOT NULL  ," +
                SimChangeTable.COLUMN_TIME + " TEXT NOT NULL  );" ;

        db.execSQL(SQL_CREATE_SIM_CHANGE_TABLE);

        // sql create for lastcallTable

        final String SQL_CREATE_LAST_CALL_TABLE = " CREATE TABLE " + LastCallTable.TABLE_NAME + " ( " +
                LastCallTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                LastCallTable.COLUMN_CALLER + " TEXT NOT NULL ," +
                LastCallTable.COLUMN_DATE + " TEXT NOT NULL , " +
                LastCallTable.COLUMN_TIME + " TEXT NOT NULL ) ; " ;

        db.execSQL(SQL_CREATE_LAST_CALL_TABLE);

        // sql create for lastoutcalltable

        final String SQL_CREATE_LAST_OUT_CALL_TABLE = " CREATE TABLE " + LastOutCallTable.TABLE_NAME + "( " +
                LastOutCallTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                LastOutCallTable.COLUMN_CALLER + " TEXT NOT NUll ," +
                LastOutCallTable.COLUMN_DATE  + " TEXT NOT NULL ," +
                LastOutCallTable.COLUMN_TIME + "TEXT NOT NULL ) ; " ;

        db.execSQL(SQL_CREATE_LAST_OUT_CALL_TABLE);

        //sql create for defaultsim
        final String SQL_CREATE_DEFAULT_SIM_TABLE = " CREATE TABLE " + DefaultSimTable.TABLE_NAME + " ( " +
                DefaultSimTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                DefaultSimTable.COLUMN_LINE1 + " TEXT NOT NULL ," +
                DefaultSimTable.COLUMN_LINE2 + " TEXT ) ; " ;

        db.execSQL(SQL_CREATE_DEFAULT_SIM_TABLE);

        //sql for sms_status

        final String SQL_CREATE_SMS_STATUS_TABLE = " CREATE TABLE " + SMSStatusTable.TABLE_NAME + "(" +
                SMSStatusTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                SMSStatusTable.COLUMN_STATUS  + " TEXT NOT NULL ) ;" ;

        db.execSQL(SQL_CREATE_SMS_STATUS_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + SettingTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SimChangeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LastCallTable.TABLE_NAME);
        db.execSQL("DROP  TABLE IF EXISTS "  + LastOutCallTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DefaultSimTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SMSStatusTable.TABLE_NAME);

        onCreate(db);




    }
}
