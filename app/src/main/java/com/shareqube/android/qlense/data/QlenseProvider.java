package com.shareqube.android.qlense.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class QlenseProvider extends ContentProvider {

    public static  final String LOG_TAG = QlenseProvider.class.getSimpleName();

    public static final int DEFAULT_SIM = 100;
    public static final int SIM_CHANGE = 200;
    public static final int LAST_CALL = 300;
    public static final int LAST_OUT_CALL = 400;
    public static final int SETTING = 500;
    public static final int SMS_STATUS = 600 ;


    QlenseDBHelper qlenseDBHelper ;
  // the uri macher useed by Qlense provider
    private static final UriMatcher sUriMatcher = buildURiMatcher() ;




    @Override
    public boolean onCreate() {

        qlenseDBHelper = new QlenseDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor  = null ;

        final int match = sUriMatcher.match(uri);

        switch (match){

            case  DEFAULT_SIM:{
                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.DefaultSimTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break;
            }
            case SIM_CHANGE:{

                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.SimChangeTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break;
            }
            case SETTING :{
                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.SettingTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break ;

            }
            case LAST_CALL:{
                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.LastCallTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break;
            }
            case LAST_OUT_CALL:{

                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.LastOutCallTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break;
            }
            case SMS_STATUS:{
                retCursor = qlenseDBHelper.getReadableDatabase().query(
                        QlenseContract.SMSStatusTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ) ;
                break;
            }


        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return  retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){

            case DEFAULT_SIM:
                return QlenseContract.DefaultSimTable.CONTENT_TYPE ;


            case SETTING:
                return QlenseContract.SettingTable.CONTENT_TYPE ;

            case SIM_CHANGE:
                return QlenseContract.SimChangeTable.CONTENT_TYPE ;

            case SMS_STATUS:
                 return QlenseContract.SMSStatusTable.CONTENT_TYPE ;

            case LAST_CALL:
                return QlenseContract.LastCallTable.CONTENT_TYPE ;

            case LAST_OUT_CALL:
                return QlenseContract.LastOutCallTable.CONTENT_TYPE ;
            default:
                throw new UnsupportedOperationException("unknown uri" + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = qlenseDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnURi;

        switch (match){
            case SETTING: {
                long _id = db.insert(QlenseContract.SettingTable.TABLE_NAME , null , values);

                Log.i(LOG_TAG , "The values of inserted row is " + _id) ;

                if(_id > 0){
                    returnURi = QlenseContract.SettingTable.buildQlenseUri(_id);

                }
                else{
                    throw  new SQLException("Fail to insert  row into " + uri);
                }

            }
            break;
            case SIM_CHANGE: {
                long _id = db.insert(QlenseContract.SimChangeTable.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnURi = QlenseContract.SimChangeTable.buildQlenseUri(_id);
                } else {
                    throw new SQLException(" Failed to insert row into " + uri);

                }
            }
            break;
            case LAST_CALL:{

                long _id = db.insert(QlenseContract.LastCallTable.TABLE_NAME , null , values);
                if(_id > 0){
                    returnURi = QlenseContract.LastCallTable.buildQlenseUri(_id);
                }
                else {
                    throw new SQLException(" Failed to insert row into " + uri);

                }
            }
            break;
            case LAST_OUT_CALL:{
                long _id = db.insert(QlenseContract.LastOutCallTable.TABLE_NAME ,null , values);
                if(_id >0){
                    returnURi = QlenseContract.LastOutCallTable.buildQlenseUri(_id);
                }
                else {
                    throw new SQLException(" Failed to insert row into " + uri);

                }
            }
            break;

            case DEFAULT_SIM:{
                long _id = db.insert(QlenseContract.DefaultSimTable.TABLE_NAME , null , values) ;
                if(_id > 0){
                    returnURi = QlenseContract.DefaultSimTable.buildQlenseUri(_id);
                                    }
                else {
                    throw new SQLException(" Failed to insert row into " + uri);

                }
            }
            break;

            default:
                throw new UnsupportedOperationException( " Unknown uri" + uri);


        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnURi;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = qlenseDBHelper.getWritableDatabase() ;
        final int match = sUriMatcher.match(uri);

        final int rowdeleted ;


        if(null == selection){
            selection = "1";
        }

        switch (match){

            case SETTING:
                rowdeleted = db.delete(QlenseContract.SettingTable.TABLE_NAME ,selection , selectionArgs);
                break;
            case SIM_CHANGE:
                rowdeleted = db.delete(QlenseContract.SimChangeTable.TABLE_NAME , selection ,selectionArgs);
                break;
            case SMS_STATUS:
                rowdeleted = db.delete(QlenseContract.SMSStatusTable.TABLE_NAME , selection , selectionArgs) ;
                break ;
            case DEFAULT_SIM:
                rowdeleted = db.delete(QlenseContract.DefaultSimTable.TABLE_NAME , selection , selectionArgs);
                break;
            case LAST_OUT_CALL:
                rowdeleted = db.delete(QlenseContract.LastOutCallTable.TABLE_NAME , selection , selectionArgs) ;
                break;
            case LAST_CALL:
                rowdeleted = db.delete(QlenseContract.LastCallTable.TABLE_NAME , selection , selectionArgs) ;
                break;
            default:
                throw  new UnsupportedOperationException("unknown Uri" + uri) ;
        }

        if(rowdeleted != 0){
            getContext().getContentResolver().notifyChange(uri , null);
        }

        db.close();

        return rowdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = qlenseDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        final int rowUpdated ;

        switch (match){
            case SETTING:
                rowUpdated = db.update(QlenseContract.SettingTable.TABLE_NAME ,values ,selection ,selectionArgs);
                break;
            case SMS_STATUS:
                rowUpdated = db.update(QlenseContract.SMSStatusTable.TABLE_NAME , values, selection, selectionArgs);
                break;
            case SIM_CHANGE:
                rowUpdated = db.update(QlenseContract.SimChangeTable.TABLE_NAME , values , selection , selectionArgs);
                break;
            case LAST_CALL:
                rowUpdated = db.update(QlenseContract.LastCallTable.TABLE_NAME , values , selection , selectionArgs);
                break;
            case LAST_OUT_CALL:
                rowUpdated = db.update(QlenseContract.LastOutCallTable.TABLE_NAME , values, selection , selectionArgs);
                break;
            case DEFAULT_SIM:
                rowUpdated = db.update(QlenseContract.DefaultSimTable.TABLE_NAME , values ,selection , selectionArgs) ;
                break;

            default:
                throw new UnsupportedOperationException("Unknow uri " + uri);

        }
        if(rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowUpdated;

    }


    static UriMatcher buildURiMatcher(){



        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority  = QlenseContract.CONTENT_AUTHORITY ;

        matcher.addURI(authority,QlenseContract.PATH_DEFAULT_SIM, DEFAULT_SIM);
        matcher.addURI(authority ,QlenseContract.PATH_LAST_CALL ,LAST_CALL);
        matcher.addURI(authority ,QlenseContract.PATH_LAST_CALL_OUT ,LAST_OUT_CALL);
        matcher.addURI(authority ,QlenseContract.PATH_SIMCHANGE,SIM_CHANGE);
        matcher.addURI(authority, QlenseContract.PATH_SETTING ,SETTING);
        matcher.addURI(authority ,QlenseContract.PATH_SMS_STATUS,SMS_STATUS);


        return  matcher;
    }



}
