package com.shareqube.android.qlense.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;


public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(QlenseDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
//        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(QlenseContract.SettingTable.TABLE_NAME);
        tableNameHashSet.add(QlenseContract.SMSStatusTable.TABLE_NAME);
        tableNameHashSet.add(QlenseContract.DefaultSimTable.TABLE_NAME);
        tableNameHashSet.add(QlenseContract.LastOutCallTable.TABLE_NAME);
        tableNameHashSet.add(QlenseContract.LastCallTable.TABLE_NAME);
        tableNameHashSet.add(QlenseContract.SimChangeTable.TABLE_NAME);

        mContext.deleteDatabase(QlenseDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new QlenseDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
//
//        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

//        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));

        } while (c.moveToNext());

//        // if this fails, it means that your database doesn't contain all the tables

            assertTrue("Error: Your database was created without all the tables",
                    tableNameHashSet.isEmpty());
//
//        // now, do our tables contain the correct columns?
            c = db.rawQuery("PRAGMA table_info(" + QlenseContract.DefaultSimTable.TABLE_NAME + ")",
                    null);
//
            assertTrue("Error: This means that we were unable to query the database for table information.",
                    c.moveToFirst());
//
            // Build a HashSet of all of the column names we want to look for
            final HashSet<String> defaultSimColumnHashSet = new HashSet<String>();
            defaultSimColumnHashSet.add(QlenseContract.DefaultSimTable._ID);
            defaultSimColumnHashSet.add(QlenseContract.DefaultSimTable.COLUMN_LINE1);
            defaultSimColumnHashSet.add(QlenseContract.DefaultSimTable.COLUMN_LINE2);

            int columnNameIndex = c.getColumnIndex("name");
            do {
                String columnName = c.getString(columnNameIndex);
                defaultSimColumnHashSet.remove(columnName);
            } while (c.moveToNext());

//        // if this fails, it means that your database doesn't contain all of the required location
//        // entry columns
            assertTrue("Error: The database doesn't contain all of the required location entry columns",
                    defaultSimColumnHashSet.isEmpty());
            db.close();
        }


    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.  Return
        the rowId of the inserted location.
    */
    public long testLocationTable() {
        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)

        // Insert ContentValues into database and get a row ID back

        // Query the database and receive a Cursor back

        // Move the cursor to a valid database row

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database

        // Return the rowId of the inserted location, or "-1" on failure.
        return -1L;
    }


    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.

        // We return the rowId of the inserted location in testLocationTable, so
        // you should just call that function rather than rewriting it

        // First step: Get reference to writable database

        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)

        // Insert ContentValues into database and get a row ID back

        // Query the database and receive a Cursor back

        // Move the cursor to a valid database row

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database
    }
}
