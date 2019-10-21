package com.tatx.userapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tatx.userapp.dataset.CountryCodePojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;



public class CountryCodeSqliteDbHelper extends SQLiteOpenHelper {

    // All Static variables
// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "CountryCode.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
    public CountryCodeSqliteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // Getting single contact
    public List<CountryCodePojo> getCountryCodeFlagList() {
        List<CountryCodePojo> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CountryCodeTable", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CountryCodePojo cont = new CountryCodePojo();
            //cont.setId(cursor.getInt(0));
            cont.setCountryName(cursor.getString(2));
            cont.setCountryCode(cursor.getString(6));
            cont.setFlag(cursor.getBlob(7));

            array_list.add(cont);
            cursor.moveToNext();

        }
        cursor.close();
        db.close();
        return array_list;
    }
    // Getting single contact
    public CountryCodePojo getDetailsByCountryCode(String countryCode)
    {




        CountryCodePojo countryCodePojo = new CountryCodePojo();


        if(countryCode == null)
        {

            return  countryCodePojo;

        }



        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM CountryCodeTable where phonecode = " + countryCode, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast())
        {

            countryCodePojo.setCountryName(cursor.getString(2));
            countryCodePojo.setCountryCode(cursor.getString(6));
            countryCodePojo.setFlag(cursor.getBlob(7));
            cursor.moveToNext();

        }

        cursor.close();

        db.close();

        return countryCodePojo;



    }

    public void CopyDataBaseFromAsset() throws IOException{

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

// Path to the just created empty db
        String outFileName = getDatabasePath();

// if the path doesn't exist fir
// st, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

// Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

// transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

// Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub

    }
}
