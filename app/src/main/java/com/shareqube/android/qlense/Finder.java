package com.shareqube.android.qlense;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.shareqube.android.qlense.data.QlenseContract;
import com.shareqube.android.qlense.data.QlenseDBHelper;
import com.shareqube.android.qlense.data.QlenseProvider;


public class Finder extends ActionBarActivity  implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_TAG = Finder.class.getSimpleName();

    final static int LOADER_ID = 1 ;
    SimpleCursorAdapter msimpleAdapter ;
    SimChangeChecker checker ;
    SQLiteOpenHelper db;


    private static final String[] SETTING_COLUMN ={
            QlenseContract.SettingTable._ID,
            QlenseContract.SettingTable.COLUMN_PIN ,
            QlenseContract.SettingTable.COLUMN_SIMNUMBER

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder);

        getActionBar();

        new QlenseDBHelper(this);

        new QlenseProvider();

    }


    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    public void onClickSetting(View v) {



        String pinValue = "";
        String defaultNumberValue = "";
        String optionNumberValue = " ";


        pinValue = ((EditText) findViewById(R.id.pinValue)).getText().toString();
        defaultNumberValue = ((EditText) findViewById(R.id.defaultnumbervalue)).getText().toString();
        optionNumberValue = ((EditText) findViewById(R.id.optionNumberValue)).getText().toString();

        String simSerial =  SimChangeChecker.getPhoneSerial(getApplicationContext());

        Toast.makeText(getApplicationContext()," Serial "   + simSerial, Toast.LENGTH_LONG).show();
//
        if (!pinValue.equalsIgnoreCase("") && !defaultNumberValue.equalsIgnoreCase("")) {


             ContentValues settingValues = new ContentValues();
                  settingValues.put(QlenseContract.SettingTable.COLUMN_PIN ,pinValue);
                  settingValues.put( QlenseContract.SettingTable.COLUMN_SIMNUMBER, simSerial);

            getContentResolver().insert(QlenseContract.SettingTable.CONTENT_URI ,settingValues);

            ContentValues phonesNumber = new ContentValues();
                         phonesNumber.put(QlenseContract.DefaultSimTable.COLUMN_LINE1 , defaultNumberValue);
                        phonesNumber.put(QlenseContract.DefaultSimTable.COLUMN_LINE2 , optionNumberValue);

            getContentResolver().insert(QlenseContract.DefaultSimTable.CONTENT_URI , phonesNumber);

            Intent serviceIntent = new Intent(this , Worker.class);
            startService(serviceIntent);
            this.finish();
      } else {

            Toast.makeText(this, "Your pin or Phone Number is empty, Try again", Toast.LENGTH_LONG).show();
       }

    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri userSettingUri = QlenseContract.SettingTable.buildQlenseUri(id) ;
        return new CursorLoader(this , userSettingUri ,SETTING_COLUMN,null,null ,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        msimpleAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        msimpleAdapter.swapCursor(null ) ;

    }
}
