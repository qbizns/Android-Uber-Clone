package com.tatx.partnerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.tatx.partnerapp.dataset.CardDetails;
import com.tatx.partnerapp.dataset.LoginData;
import com.tatx.partnerapp.dataset.NotificationDataSet;
import com.tatx.partnerapp.dataset.TripDetails;
import com.tatx.partnerapp.dataset.WayLocationsLog;


public class SqliteDB extends SQLiteOpenHelper {


    private static final Object FILE_DIR = "TATX_DB";
    //* Database name
    private static String DBNAME = "MYDB.db";

    //* Version number of the database
    private static int VERSION = 2;

    // * A constant, stores the the table name
    public static final String MY_PROFILE_TABLE_NAME = "UserProfile";
    public static final String PUSHNOTIFICCATION_TABLE_NAME_GENERAL = "NotificationGeneral";
    public static final String COUNTRY_CODE_TABLE = "CountryTable";
    public static final String CONFIGURATION_TABLE = "ConfigurationTable";
    public static final String COLOR_TABLE="ColorTable";
    public static final String CARD_TABLE="CardTable";
    public static final String WAY_LOCATIONS_LOG_TABLE="WayLocationsLog";
    public static final String TRIP_TABLE="TripTable";


    public static final String FIELD_ROW_ID = "id";
    public static final String MOBILE = "mobile";

    //USER PROFILE
    public static final String USERID_ID = "userID";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String DOB = "dob";
    public static final String GENDER = "gender";
    public static final String IMAGE_PATH="image_path";


    //Country code table
    public static final String COUNTRY_CODE = "country_code";
    public static final String PHONE_CODE = "phone_code";
    public static final String COUNTRY_NAME = "country_name";

    //Push notification data
    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String MESSAGE = "message";
    public static final String DESCRIPTION = "description";
    public static final String READ_UNREAD = "read_unread";
    public static final String DATE_TIME = "date_time";
    public static final String CURRENT_LOCATION = "current_location";

    public static final String PARAM_NAME="param_name";
    public static final String PARAM_VALUE="param_value";
    public static final String PARAM_DESCRIPTION="description";

    public static final String ACCOUNT_HOLDER_NAME="acc_holder_name";
    public static final String CADR_NUMBER="card_number";
    public static final String EXPIRY_DATE="expiry_date";

    public static final String LATITUDE_CURR = "latitude_curr";
    public static final String LONGITUDE_CURR = "longitude_curr";
    public static final String LATITUDE_SRC="latitude_src";
    public static final String LONGITUDE_SRC="longitude_src";
    public static final String LATITUDE_DEST="latitude_dest";
    public static final String LONGITUDE_DEST="longitude_dest";
    public static final String WAY_DETAILS_FLAG="flag";
    public static final String TOTAL_DISTANCE = "total_distance";
    public static final String LOCATION_UPDATED_DATE = "updatedOn";

    public static final String ORDER_ID="orderid";
    public static final String TRIP_ID="tripid";
    public static final String CUSTOMER_NAME="customername";
    public static final String POLYLINES = "polylines";



    // * An instance variable for SQLiteDatabase
    private SQLiteDatabase mDB;

    // Constructor
    public SqliteDB(Context context) {
        super(context, DBNAME, null, VERSION);
       /* super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator + DBNAME, null, VERSION);*/
        this.mDB = getWritableDatabase();
    }


    /**
     * This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
     * provided the database does not exists
     */
    @Override
    public void onCreate(SQLiteDatabase db) {


        String sqlPushGeneral = "create table " + PUSHNOTIFICCATION_TABLE_NAME_GENERAL + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                CODE + " text , " +
                DATA + " text , " +
                MESSAGE + " text , " +
                DESCRIPTION + " text , " +
                READ_UNREAD + " text , " +
                DATE_TIME + " text " +
                " ) ";


        String sqlCountrycode = "create table " + COUNTRY_CODE_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                COUNTRY_CODE + " text , " +
                PHONE_CODE + " text , " +
                COUNTRY_NAME + " text " +
                " ) ";
        String sqlConfigTable = "create table " + CONFIGURATION_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                PARAM_NAME + " text , " +
                PARAM_VALUE + " text , " +
                PARAM_DESCRIPTION + " text " +
                " ) ";



        String sqlCardTable = "create table " + CARD_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                ACCOUNT_HOLDER_NAME + " text , " +
                CADR_NUMBER + " integer , " +
                EXPIRY_DATE + " text " +
                " ) ";
        String sqlWaylocatonsLog = "create table " + WAY_LOCATIONS_LOG_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                LATITUDE_CURR + " text , " +
                LONGITUDE_CURR + " text , " +
                WAY_DETAILS_FLAG + " text , " +
                LOCATION_UPDATED_DATE + " text , " +
                TOTAL_DISTANCE + " text " +

                " ) ";

        String sqlForTrip = "create table " + TRIP_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                LATITUDE_SRC + " text , " +
                LONGITUDE_SRC + " text , " +
                LATITUDE_DEST + " text , " +
                LONGITUDE_DEST + " text , " +
                ORDER_ID + " text , " +
                TRIP_ID + " text , " +
                MOBILE + " text , " +
                POLYLINES + " text , " +
                CUSTOMER_NAME + " text " +
                " ) ";


        db.execSQL(
                "create table UserProfile " +
                        "(id integer primary key, userID text,firstName text,lastName text, email text,mobile text,dob text,gender text,image_path text)"
        );

        db.execSQL(sqlPushGeneral);
        db.execSQL(sqlCountrycode);
        db.execSQL(sqlConfigTable);
        db.execSQL(sqlCardTable);
        db.execSQL(sqlWaylocatonsLog);
        db.execSQL(sqlForTrip);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + " " + MY_PROFILE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + " " + PUSHNOTIFICCATION_TABLE_NAME_GENERAL);
        db.execSQL("DROP TABLE IF EXISTS" + " " + COUNTRY_CODE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS" + " " + CONFIGURATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS" + " " + COLOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS" + " " + CARD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS" + " " + WAY_LOCATIONS_LOG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS" + " " + TRIP_TABLE);
        onCreate(db);
    }


    public boolean insertUserProfile(String userId, String firstName, String lastName, String email, String mobile, String dob, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERID_ID, userId);
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(EMAIL, email);
        contentValues.put(MOBILE, mobile);
        contentValues.put(DOB, dob);
        contentValues.put(GENDER, gender);
        contentValues.put(IMAGE_PATH,"");
        db.insert(MY_PROFILE_TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateUserProfile(Integer id, String firstName, String lastName, String email, String mobile, String dob, String gender, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_NAME, firstName);
        contentValues.put(LAST_NAME, lastName);
        contentValues.put(EMAIL, email);
        contentValues.put(MOBILE, mobile);
        contentValues.put(DOB, dob);
        contentValues.put(GENDER, gender);
        contentValues.put(IMAGE_PATH, imagePath);
        db.update(MY_PROFILE_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public boolean updateUserProfilePic(Integer id, String image_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, image_path);
        db.update(MY_PROFILE_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
    public Integer deleteUserProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MY_PROFILE_TABLE_NAME, USERID_ID ,null);

    }

    /**
     * Returns all the user details from the table
     */
   public List<LoginData> getUserProfile() {
        List<LoginData> array_list = new ArrayList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MY_PROFILE_TABLE_NAME + "", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            LoginData result = new LoginData();
            result.setUserid(res.getString(1));
            result.setFirst_name(res.getString(2));
            result.setLast_name(res.getString(3));
            result.setEmail(res.getString(4));
            result.setPhone_number(res.getString(5));
            result.setImage_path(res.getString(8));

            array_list.add(result);
            res.moveToNext();
        }
        return array_list;

    }

    public boolean insertCardDetails(String account_holder_name, int card_number, String exp_date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCOUNT_HOLDER_NAME, account_holder_name);
        contentValues.put(CADR_NUMBER, card_number);
        contentValues.put(EXPIRY_DATE, exp_date);
        db.insert(CARD_TABLE, null, contentValues);

        return true;
    }

    public List<CardDetails> getSavedCardDetails() {
        List<CardDetails> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CARD_TABLE + "", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            CardDetails result = new CardDetails();
            result.setAccount_holder_name(res.getString(1));
            result.setCard_number(res.getInt(2));
            result.setExp_date(res.getString(3));
            array_list.add(result);
            res.moveToNext();
        }

        return array_list;

    }








/*

    */
/**
     * Inserts a new location to the table locations
     *//*

    public long insert(ContentValues contentValues) {
        return mDB.insert(DATABASE_TABLE, null, contentValues);

    }
    */
/**
     * Deletes all locations from the table
     *//*

    public int del() {
        return mDB.delete(DATABASE_TABLE, null, null);
    }

    public void updateMyLocationName(Integer locid, String locationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_LOCATION_NAME, locationName);
        db.update(DATABASE_TABLE, contentValues, FIELD_LOCATION_LOCID + " = " + locid, null);
        db.close();

    }

    public Integer deleteLocationFromDb(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TABLE,
                "locid = ? ",
                new String[]{Integer.toString(id)});

    }

    */
/**
     * Returns all the locations from the table
     *//*

    public Cursor getAllLocations() {
        return mDB.query(DATABASE_TABLE, new String[]{FIELD_ROW_ID, FIELD_LAT, FIELD_LNG, FIELD_ZOOM, FIELD_LOCATION_NAME, FIELD_LOCATION_LOCID, FIELD_LOCATION_ADDRESS}, null, null, null, null, null);
    }
*/

    /**
     * Returns all the locations from the table
     */
   /* public List<UserLocations> getAllLocationsFromDb() {
        List<UserLocations> array_list = new ArrayList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE + "", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            UserLocations result = new UserLocations();
            result.setLocLat(res.getString(2));
            result.setLocLong(res.getString(1));
            result.setZoomLevel(res.getString(3));
            result.setLocName(res.getString(4));
            result.setID(res.getString(5));
            result.setLocAddr(res.getString(6));
            array_list.add(result);
            res.moveToNext();
        }

        return array_list;

    }
    public List<UserLocations> getAllLocationsAcceptedFromDb() {
        List<UserLocations> array_list = new ArrayList<UserLocations>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE + " where "+ACCEPTED_STATUS +" = "+"1",null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            UserLocations result = new UserLocations();
            result.setLocLat(res.getString(2));
            result.setLocLong(res.getString(1));
            result.setZoomLevel(res.getString(3));
            result.setLocName(res.getString(4));
            result.setID(res.getString(5));
            result.setLocAddr(res.getString(6));
            array_list.add(result);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserLocations> getLocationsFromDbByLocID(String locId) {
        List<UserLocations> array_list = new ArrayList<UserLocations>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE + " where "+ACCEPTED_STATUS +" = " + "1 AND "+FIELD_LOCATION_LOCID+" = "+locId +" limit 1" ,null);
        res.moveToFirst();

        while (!res.isAfterLast()) {

            UserLocations result = new UserLocations();
            result.setLocLong(res.getString(1));
            result.setLocLat(res.getString(2));
            result.setZoomLevel(res.getString(3));
            result.setLocName(res.getString(4));
            result.setID(res.getString(5));
            result.setLocAddr(res.getString(6));
            array_list.add(result);
            res.moveToNext();
        }
        return array_list;
    }

    // Unread Location Counts
    public int getUnreadLocatiosCount(){
        int unreadMsg=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE + " where "+READ_UNREAD+" = " + "1 ", null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            res.moveToFirst();
        }
        unreadMsg=res.getCount();
        return unreadMsg;
    }

    public void updateLocationAcceptedStatus(Integer Locid, String acceptedStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCEPTED_STATUS, acceptedStatus);
        db.update(DATABASE_TABLE, contentValues, FIELD_LOCATION_LOCID + "=" + Locid, null);
        db.close();

    }

    public List<UserLocations> getAllLocationsPendingFromDb() {
        List<UserLocations> array_list = new ArrayList<UserLocations>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_TABLE + " where "+ACCEPTED_STATUS +" = "+"0",null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserLocations result = new UserLocations();
            result.setLocLat(res.getString(2));
            result.setLocLong(res.getString(1));
            result.setZoomLevel(res.getString(3));
            result.setLocName(res.getString(4));
            result.setID(res.getString(5));
            result.setLocAddr(res.getString(6));
            array_list.add(result);
            res.moveToNext();
        }
        return array_list;
    }

    *//*methods for fnf table*//*
    public boolean insertContact(String id,String fafname, String fafuserid, String status, String trackablestatus, String mobile, String onlinestatus, String Date,String acceptedStatus,String image_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_ROW_ID, id);
        contentValues.put(FNF_Name, fafname);
        contentValues.put(Fnfuserid_ID, fafuserid);
        contentValues.put(Status, status);
        contentValues.put(Trackablestatus, trackablestatus);
        contentValues.put(MOBILE, mobile);
        contentValues.put(Onlinestatus, onlinestatus);
        contentValues.put(LOCATION_UPDATED_DATE, Date);
        contentValues.put(ACCEPTED_STATUS,acceptedStatus);
        contentValues.put(IMAGE_PATH,image_path);
        db.insert(TABLE_NAME, null, contentValues);

        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);

    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact(Integer id, String fafname, String fafuserid, String status, String trackablestatus, String mobile, String onlinestatus, String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FNF_Name, fafname);
        contentValues.put(Fnfuserid_ID, fafuserid);
        contentValues.put(Status, status);
        contentValues.put(Trackablestatus, trackablestatus);
        contentValues.put(MOBILE, mobile);
        contentValues.put(Onlinestatus, onlinestatus);
        contentValues.put(LOCATION_UPDATED_DATE, Date);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }
    public boolean updateFNFNameByMobile(String mobile, String fafname, String Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FNF_Name, fafname);
        contentValues.put(LOCATION_UPDATED_DATE, Date);
        db.update(TABLE_NAME, contentValues, MOBILE + "=" +mobile,null);
        db.close();
        return true;
    }

    public void updateTrackingStatus(Integer fafuserid, String trackablestatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Trackablestatus, trackablestatus);
        db.update(TABLE_NAME, contentValues, Fnfuserid_ID + "=" + fafuserid, null);
        db.close();

    }

    public void updateBlockUnblock(Integer fafuserid, String blockUnblock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Status, blockUnblock);
        db.update(TABLE_NAME, contentValues, Fnfuserid_ID + "=" + fafuserid, null);
        db.close();

    }

    public boolean updateFNFPicByMobile(String mobile, String image_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_PATH, image_path);
        db.update(TABLE_NAME, contentValues, MOBILE + "=" +mobile,null);
        db.close();
        return true;
    }

    public Integer deleteContact(Integer id, String fnfid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                FIELD_ROW_ID+" = "+id +" AND "+Fnfuserid_ID+" = "+fnfid+" AND "+ACCEPTED_STATUS+" = 0",null);

    }

    public List<UserFNFContacts> getAllCotacts() {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));
            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserFNFContacts> getAllFnfContactsAccepted() {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where "+ACCEPTED_STATUS +" = "+"1",null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));

            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserFNFContacts> getAllFnfContactsStatusPenging() {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where "+ACCEPTED_STATUS +" = "+"0", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));

            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserFNFContacts> getFnfContactsStatusPenging(String userid) {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where "+ACCEPTED_STATUS +" = "+"0 " +  "AND "+ Fnfuserid_ID+" = "+userid, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));

            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserFNFContacts> getFnfUserNameById(String fnfuserid) {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where "+FIELD_ROW_ID +" = "+fnfuserid +  " OR "+ Fnfuserid_ID+" = "+fnfuserid, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));

            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }
    public List<UserFNFContacts> getFnfDetailsByMobileNo(String mobile) {
        List<UserFNFContacts> array_list = new ArrayList<UserFNFContacts>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where "+MOBILE +" = "+mobile , null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {

            UserFNFContacts userFNFContacts = new UserFNFContacts();
            userFNFContacts.setId(res.getString(0));
            userFNFContacts.setUsername(res.getString(1));
            userFNFContacts.setFafuserid(res.getString(2));
            userFNFContacts.setStatus(res.getString(3));
            userFNFContacts.setTrackablestatus(res.getString(4));
            userFNFContacts.setMobile(res.getString(5));
            userFNFContacts.setOnlinestatus(res.getString(6));

            userFNFContacts.setAcceptstatus(res.getString(8));
            userFNFContacts.setImage_path(res.getString(9));
            array_list.add(userFNFContacts);
            res.moveToNext();
        }
        return array_list;
    }

    public void updateFNFAcceptedStatus(String relid, String acceptedStatus, String fnfid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACCEPTED_STATUS, acceptedStatus);
        db.update(TABLE_NAME, contentValues, FIELD_ROW_ID + "=" + relid +" AND "+Fnfuserid_ID + "=" + fnfid+" AND "+ACCEPTED_STATUS + "=0", null);
        db.close();

    }

    public int checkDuplicateFnf(String fnfid) {
        int value = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where fafuserid=" + fnfid + "", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            // value +=value;
            res.moveToNext();
        }
        value = res.getCount();
        Log.d("Anill", "Count" + value);
        return value;
    }*/

  /*  public boolean updateMasterTrackableStatus(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MASTER_TRACKABLE_STATUS, status);
        db.update(MY_PROFILE_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(1)});
        return true;
    }
    public boolean updateMasterMuteStatus(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MASTER_MUTE_STATUS, status);
        db.update(MY_PROFILE_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(1)});
        return true;
    }

    public List<UserAccountDetails> getUserPofile() {
        List<UserAccountDetails> array_list = new ArrayList<UserAccountDetails>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + MY_PROFILE_TABLE_NAME + "", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            UserAccountDetails userAccountDetails=new UserAccountDetails();
            userAccountDetails.setFirstname(res.getString(2));
            userAccountDetails.setLastname(res.getString(3));
            userAccountDetails.setEmail(res.getString(4));
            userAccountDetails.setMobile(res.getString(5));
            userAccountDetails.setDob(res.getString(6));
            userAccountDetails.setGender(res.getString(7));
            userAccountDetails.setImagepath(res.getString(8));
            userAccountDetails.setMasterTrackabllity(res.getInt(9));
            userAccountDetails.setMasterMute(res.getInt(10));
            array_list.add(userAccountDetails);
            res.moveToNext();
        }
        return array_list;
    }

    public Integer deleteUserProfile(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    *//*Pushnotification General*/
    public boolean insertGeneralNotification(String code, String data, String message, String description, String readStatus, String date_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CODE, code);
        contentValues.put(DATA, data);
        contentValues.put(MESSAGE, message);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(READ_UNREAD, readStatus);
        contentValues.put(DATE_TIME, date_time);
        db.insert(PUSHNOTIFICCATION_TABLE_NAME_GENERAL, null, contentValues);
        return true;
    }

    public List<NotificationDataSet> getGeneralNotification() {
        List<NotificationDataSet> array_list = new ArrayList<NotificationDataSet>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PUSHNOTIFICCATION_TABLE_NAME_GENERAL + " order by "+FIELD_ROW_ID+" desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            NotificationDataSet notificationDataSet = new NotificationDataSet();
            notificationDataSet.setId(res.getString(0));
            notificationDataSet.setCode(res.getString(1));
            notificationDataSet.setData(res.getString(2));
            notificationDataSet.setMessages(res.getString(3));
            notificationDataSet.setDescription(res.getString(4));
            notificationDataSet.setReadstatus(res.getString(5));
            notificationDataSet.setTimedate(res.getString(6));
            array_list.add(notificationDataSet);
            res.moveToNext();
        }
        return array_list;
    }
    public Integer deleteRejectedByUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PUSHNOTIFICCATION_TABLE_NAME_GENERAL,
                FIELD_ROW_ID+" = "+id, null);
    }
    public void updateReadStatus(Integer id, String readStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(READ_UNREAD, readStatus);
        db.update(PUSHNOTIFICCATION_TABLE_NAME_GENERAL, contentValues, FIELD_ROW_ID + "=" + id, null);
        db.close();

    }
    public int getReadUnreadCount(){
        int unreadMsg=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PUSHNOTIFICCATION_TABLE_NAME_GENERAL + " where "+READ_UNREAD+" =" + "1" + "", null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            res.moveToFirst();
        }
        unreadMsg=res.getCount();
        return unreadMsg;
    }
/*
    // insert fnf Ways Details
    public boolean insertWayDetails(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(GETWAYDETAILS_TABLE, null, contentValues);
        return true;
    }
    public void updateWayStatusActive(String wayid,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WAY_STATUS, "1");
        contentValues.put(START_DATE_TIME, date);
        db.update(GETWAYDETAILS_TABLE, contentValues, WAY_ID + " = " + wayid, null);
        db.close();

    }
    public void updateWayStatusCompleted(String wayid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WAY_STATUS, "2");
        db.update(GETWAYDETAILS_TABLE, contentValues, WAY_ID + " = " + wayid, null);
        db.close();

    }
    public void updateWayPolylines(String wayid,String polylines) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POLYLINES, polylines);
        db.update(GETWAYDETAILS_TABLE, contentValues, WAY_ID + " = " + wayid, null);
        db.close();

    }
    public void updateHistoryPolylines(String wayid,String polylines) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WAY_HISTORY_POLYLINES, polylines);
        db.update(GETWAYDETAILS_TABLE, contentValues, WAY_ID + " = " + wayid, null);
        db.close();

    }
    public void deleteWayDeatilsZeroStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GETWAYDETAILS_TABLE,
                WAY_STATUS+" = 0", null);
    }
    public List<GetWayDetailsByID> getWayDetailsList() {
        List<GetWayDetailsByID> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GETWAYDETAILS_TABLE + " ", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
            getWayDetailsByID.setWayID(res.getString(1));
            getWayDetailsByID.setWayName(res.getString(2));
            getWayDetailsByID.setWaySource(res.getString(3));
            getWayDetailsByID.setWayDest(res.getString(4));
            getWayDetailsByID.setVehiclenumber(res.getString(5));
            getWayDetailsByID.setVehicleimage(res.getString(6));
            getWayDetailsByID.setVehicleqrcode(res.getString(7));
            getWayDetailsByID.setOwnerName(res.getString(8));
            getWayDetailsByID.setWayStartDate(res.getString(9));
            getWayDetailsByID.setWayEndDate(res.getString(10));
            getWayDetailsByID.setWayCreatedDate(res.getString(11));
            getWayDetailsByID.setFlag(res.getString(12));
            getWayDetailsByID.setWayStatus(res.getString(13));
            getWayDetailsByID.setPolylines(res.getString(14));
            getWayDetailsByID.setSelectedFNFIDS(res.getString(16));

            array_list.add(getWayDetailsByID);
            res.moveToNext();
        }
        return array_list;
    }
    public List<GetWayDetailsByID> getWayDetailsListById(String wayid) {
        List<GetWayDetailsByID> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GETWAYDETAILS_TABLE + " where "+WAY_ID +" = " + wayid, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
            getWayDetailsByID.setWayID(res.getString(1));
            getWayDetailsByID.setWayName(res.getString(2));
            getWayDetailsByID.setWaySource(res.getString(3));
            getWayDetailsByID.setWayDest(res.getString(4));
            getWayDetailsByID.setVehiclenumber(res.getString(5));
            getWayDetailsByID.setVehicleimage(res.getString(6));
            getWayDetailsByID.setVehicleqrcode(res.getString(7));
            getWayDetailsByID.setOwnerName(res.getString(8));
            getWayDetailsByID.setWayStartDate(res.getString(9));
            getWayDetailsByID.setWayEndDate(res.getString(10));
            getWayDetailsByID.setWayCreatedDate(res.getString(11));
            getWayDetailsByID.setFlag(res.getString(12));
            getWayDetailsByID.setWayStatus(res.getString(13));
            getWayDetailsByID.setPolylines(res.getString(14));
            getWayDetailsByID.setHistoryPolylines(res.getString(15));
            getWayDetailsByID.setSelectedFNFIDS(res.getString(16));
            array_list.add(getWayDetailsByID);
            res.moveToNext();
        }
        return array_list;
    }
    public List<GetWayDetailsByID> getWayDetailsActive() {
        List<GetWayDetailsByID> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GETWAYDETAILS_TABLE + " where "+WAY_STATUS +" = " + "1", null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
            getWayDetailsByID.setWayID(res.getString(1));
            getWayDetailsByID.setWayName(res.getString(2));
            getWayDetailsByID.setWaySource(res.getString(3));
            getWayDetailsByID.setWayDest(res.getString(4));
            getWayDetailsByID.setVehiclenumber(res.getString(5));
            getWayDetailsByID.setVehicleimage(res.getString(6));
            getWayDetailsByID.setVehicleqrcode(res.getString(7));
            getWayDetailsByID.setOwnerName(res.getString(8));
            getWayDetailsByID.setWayStartDate(res.getString(9));
            getWayDetailsByID.setWayEndDate(res.getString(10));
            getWayDetailsByID.setWayCreatedDate(res.getString(11));
            getWayDetailsByID.setFlag(res.getString(12));
            getWayDetailsByID.setWayStatus(res.getString(13));
            getWayDetailsByID.setPolylines(res.getString(14));
            getWayDetailsByID.setSelectedFNFIDS(res.getString(16));
            array_list.add(getWayDetailsByID);
            res.moveToNext();
        }
        return array_list;
    }

    public List<GetWayDetailsByID> getWayDetailsFnfActive() {
        List<GetWayDetailsByID> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GETWAYDETAILS_TABLE + " where "+WAY_STATUS +" = " + "3", null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
            getWayDetailsByID.setWayID(res.getString(1));
            getWayDetailsByID.setWayName(res.getString(2));
            getWayDetailsByID.setWaySource(res.getString(3));
            getWayDetailsByID.setWayDest(res.getString(4));
            getWayDetailsByID.setVehiclenumber(res.getString(5));
            getWayDetailsByID.setVehicleimage(res.getString(6));
            getWayDetailsByID.setVehicleqrcode(res.getString(7));
            getWayDetailsByID.setOwnerName(res.getString(8));
            getWayDetailsByID.setWayStartDate(res.getString(9));
            getWayDetailsByID.setWayEndDate(res.getString(10));
            getWayDetailsByID.setWayCreatedDate(res.getString(11));
            getWayDetailsByID.setFlag(res.getString(12));
            getWayDetailsByID.setWayStatus(res.getString(13));
            getWayDetailsByID.setPolylines(res.getString(14));

            getWayDetailsByID.setSelectedFNFIDS(res.getString(16));
            array_list.add(getWayDetailsByID);
            res.moveToNext();
        }
        return array_list;
    }
    public List<GetWayDetailsByID> getWayHistoryByDate() {
        List<GetWayDetailsByID> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + GETWAYDETAILS_TABLE + " where "+WAY_STATUS +" = " + "2 order by " + START_DATE_TIME, null);

        res.moveToFirst();
        while (!res.isAfterLast()) {
            GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
            getWayDetailsByID.setWayID(res.getString(1));
            getWayDetailsByID.setWayName(res.getString(2));
            getWayDetailsByID.setWaySource(res.getString(3));
            getWayDetailsByID.setWayDest(res.getString(4));
            getWayDetailsByID.setVehiclenumber(res.getString(5));
            getWayDetailsByID.setVehicleimage(res.getString(6));
            getWayDetailsByID.setVehicleqrcode(res.getString(7));
            getWayDetailsByID.setOwnerName(res.getString(8));
            getWayDetailsByID.setWayStartDate(res.getString(9));
            getWayDetailsByID.setWayEndDate(res.getString(10));
            getWayDetailsByID.setWayCreatedDate(res.getString(11));
            getWayDetailsByID.setFlag(res.getString(12));
            getWayDetailsByID.setWayStatus(res.getString(13));
            getWayDetailsByID.setPolylines(res.getString(14));

            getWayDetailsByID.setSelectedFNFIDS(res.getString(16));
            array_list.add(getWayDetailsByID);
            res.moveToNext();
        }
        return array_list;
    }
    *//* // insert current location
     public boolean insertCurrentLocation(ContentValues contentValues) {
         SQLiteDatabase db = this.getWritableDatabase();
         db.insert(CUURENT_lOCATION_TABLE, null, contentValues);
         return true;
     }

     public List<CurrentLocationPoints> getCurrentLocationPoints() {
         List<CurrentLocationPoints> array_list = new ArrayList<>();
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor res = db.rawQuery("select * from " + CUURENT_lOCATION_TABLE + " ", null);
         res.moveToFirst();
         while (!res.isAfterLast()) {
             CurrentLocationPoints location=new CurrentLocationPoints();
             location.setRowId(res.getString(0));
             location.setNTTD(res.getString(1));
             location.setODTD(res.getString(2));
             location.setNDTD(res.getString(3));
             location.setTTD(res.getString(4));
             location.setID(res.getString(5));
             location.setOTTD(res.getString(6));
             location.setWayID(res.getString(7));
             location.setDTD(res.getString(8));
             location.setODTD(res.getString(9));
             location.setPOR(res.getString(10));
             location.setCurrentLocation(res.getString(11));
             location.setLatitude(res.getString(12));
             location.setLongitude(res.getString(13));
             location.setFlag(res.getString(14));
             array_list.add(location);

             res.moveToNext();
         }
         return array_list;
     }


     public List<CurrentLocationPoints> getCurrentLocationPointsOwner() {
         List<CurrentLocationPoints> array_list = new ArrayList<>();
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor res = db.rawQuery("select * from " + CUURENT_lOCATION_TABLE + " where "+WAY_DETAILS_FLAG +" = "+"0 order by "+WAY_DETAILS_ID + " desc limit 1", null);
         res.moveToFirst();
         while (!res.isAfterLast()) {
             CurrentLocationPoints location=new CurrentLocationPoints();
             location.setRowId(res.getString(0));
             location.setNTTD(res.getString(1));
             location.setODTD(res.getString(2));
             location.setNDTD(res.getString(3));
             location.setTTD(res.getString(4));
             location.setID(res.getString(5));
             location.setOTTD(res.getString(6));
             location.setWayID(res.getString(7));
             location.setDTD(res.getString(8));
             location.setODTD(res.getString(9));
             location.setPOR(res.getString(10));
             location.setCurrentLocation(res.getString(11));
             location.setLatitude(res.getString(12));
             location.setLongitude(res.getString(13));
             location.setFlag(res.getString(14));
             array_list.add(location);

             res.moveToNext();
         }
         return array_list;
     }

     public List<CurrentLocationPoints> getCurrentLocationPointsFNF() {
         List<CurrentLocationPoints> array_list = new ArrayList<>();
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor res = db.rawQuery("select * from " + CUURENT_lOCATION_TABLE + " where "+WAY_DETAILS_FLAG +" = "+"1 order by "+WAY_DETAILS_ID + " desc limit 1", null);
         res.moveToFirst();
         while (!res.isAfterLast()) {
             CurrentLocationPoints location=new CurrentLocationPoints();
             location.setRowId(res.getString(0));
             location.setNTTD(res.getString(1));
             location.setODTD(res.getString(2));
             location.setNDTD(res.getString(3));
             location.setTTD(res.getString(4));
             location.setID(res.getString(5));
             location.setOTTD(res.getString(6));
             location.setWayID(res.getString(7));
             location.setDTD(res.getString(8));
             location.setODTD(res.getString(9));
             location.setPOR(res.getString(10));
             location.setCurrentLocation(res.getString(11));
             location.setLatitude(res.getString(12));
             location.setLongitude(res.getString(13));
             location.setFlag(res.getString(14));
             array_list.add(location);

             res.moveToNext();
         }
         return array_list;
     }
 */
    // insert Ways Details
    public long insertWayDetailsLog(String latitude, String longitude, String flag, String time, String totalDistance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(LATITUDE_CURR,latitude);
        contentValues.put(LONGITUDE_CURR,longitude);
        contentValues.put(WAY_DETAILS_FLAG,flag);
        contentValues.put(LOCATION_UPDATED_DATE,time);
        contentValues.put(TOTAL_DISTANCE,totalDistance);
        return db.insert(WAY_LOCATIONS_LOG_TABLE, null, contentValues);
    }

    public List<WayLocationsLog> getWayLocationsLogList() {
        List<WayLocationsLog> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + WAY_LOCATIONS_LOG_TABLE , null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            WayLocationsLog location=new WayLocationsLog();
            location.setId(res.getString(0));
            location.setLatitude(res.getString(1));
            location.setLongitude(res.getString(2));
            location.setFlag(res.getString(3));
            location.setTime(res.getString(4));
            location.setTotaldistance(res.getString(5));
            array_list.add(location);

            res.moveToNext();
        }
        return array_list;
    }

    public List<WayLocationsLog> getWayLocationsLogListToEncodePolyline(String row_id) {
        List<WayLocationsLog> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + WAY_LOCATIONS_LOG_TABLE + " where "+ FIELD_ROW_ID+ " = "+ row_id , null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            WayLocationsLog location=new WayLocationsLog();
            location.setId(res.getString(0));
            location.setLatitude(res.getString(4));
            location.setLongitude(res.getString(5));

            array_list.add(location);
            res.moveToNext();
        }
        return array_list;
    }
    public Integer deleteWayLocationLogs() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WAY_LOCATIONS_LOG_TABLE, null ,null);

    }

    // insert Ways Details
    public boolean insertTripDetails(String latitude_src, String longitude_src, String latitude_dest, String longitude_dest, String order_id, String trip_id, String mobile, String polyline, String customer_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(LATITUDE_SRC,latitude_src);
        contentValues.put(LONGITUDE_SRC,longitude_src);
        contentValues.put(LATITUDE_DEST,latitude_dest);
        contentValues.put(LONGITUDE_DEST,longitude_dest);
        contentValues.put(ORDER_ID,order_id);
        contentValues.put(TRIP_ID,trip_id);
        contentValues.put(MOBILE,mobile);
        contentValues.put(POLYLINES,polyline);
        contentValues.put(CUSTOMER_NAME,customer_name);
        db.insert(TRIP_TABLE, null, contentValues);

        return true;
    }
    public List<TripDetails> getTripDetails() {
        List<TripDetails> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TRIP_TABLE +" order by "+FIELD_ROW_ID + " desc limit 1", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            TripDetails location=new TripDetails();
            location.setLatitude_src(res.getString(1));
            location.setLongitude_src(res.getString(2));
            location.setLatitude_dest(res.getString(3));
            location.setLongitude_dest(res.getString(4));
            location.setOrderid(res.getString(5));
            location.setTripid(res.getString(6));
            location.setMobile(res.getString(7));
            location.setPolyline(res.getString(8));
            location.setCustomername(res.getString(9));
            array_list.add(location);

            res.moveToNext();
        }
        return array_list;
    }

    public void updateDestination(int trip_id, String latitude_dest, String longitude_dest)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LATITUDE_DEST, latitude_dest);
        contentValues.put(LONGITUDE_DEST, longitude_dest);
        db.update(TRIP_TABLE, contentValues, TRIP_ID + " = " + trip_id, null);
    }

    public void updatePolylines(int trip_id,String polylines) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POLYLINES, polylines);
        db.update(TRIP_TABLE, contentValues, TRIP_ID + " = " + trip_id, null);
        db.close();

    }

   /* public List<WayLocationsLog> getWayLocationsLogListOwner() {
        List<WayLocationsLog> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + WAY_LOCATIONS_LOG + " where "+WAY_DETAILS_FLAG +" = "+"0 order by "+FIELD_ROW_ID + " desc limit 1", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            WayLocationsLog location=new WayLocationsLog();
            location.setId(res.getString(0));
            location.setWayid(res.getString(1));
            location.setCallapi(res.getString(2));
            location.setBatterylevel(res.getString(3));
            location.setLatitude(res.getString(4));
            location.setLongitude(res.getString(5));
            location.setFlag(res.getString(6));
            location.setTime(res.getString(7));
            location.setDistancetravelled(res.getString(8));
            location.setTimeelapsed(res.getString(9));
            location.setTimeestimated(res.getString(10));
            array_list.add(location);

            res.moveToNext();
        }
        return array_list;
    }*/
    /*methods for insert colors code*//*
    public boolean insertColors(String colorCode,int wayid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOR_CODE, colorCode);
        contentValues.put(WAY_DETAILS_WayID,wayid);
        db.insert(COLOR_TABLE, null, contentValues);

        return true;
    }
    // Getting single contact
    public List<CountryCodePojo> getCountryCodeFlagList() {
        List<CountryCodePojo> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+COLOR_TABLE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CountryCodePojo cont = new CountryCodePojo();
            cont.setID(cursor.getInt(0));
            cont.setColorCode(cursor.getString(1));
            cont.setStatus(cursor.getInt(2));

            array_list.add(cont);
            cursor.moveToNext();

        }
        cursor.close();
        db.close();
        return array_list;
    }

    public List<CountryCodePojo> getColorId() {
        List<CountryCodePojo> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + COLOR_TABLE + " where "+WAY_DETAILS_WayID +" = "+"0 order by "+FIELD_ROW_ID + " asc limit 1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CountryCodePojo cont = new CountryCodePojo();
            cont.setID(cursor.getInt(0));
            cont.setColorCode(cursor.getString(1));
            cont.setStatus(cursor.getInt(2));

            array_list.add(cont);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return array_list;
    }
    public CountryCodePojo getColorCodeByWayID(int wayid) {
        List<CountryCodePojo> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + COLOR_TABLE + " where "+WAY_DETAILS_WayID +" = "+wayid+" order by "+FIELD_ROW_ID + " asc limit 1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CountryCodePojo cont = new CountryCodePojo();
            cont.setID(cursor.getInt(0));
            cont.setColorCode(cursor.getString(1));
            cont.setStatus(cursor.getInt(2));

            return cont;
        }
        cursor.close();
        db.close();
        return null;
    }
    public void updateWayIdInColorTbl(int id,String wayid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WAY_DETAILS_WayID, wayid);
        db.update(COLOR_TABLE, contentValues, FIELD_ROW_ID + " = " + id, null);
        db.close();

    }

    *//*methods for insert insertToggleDetails *//*
    public boolean insertToggleDetails(String swtchName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SWITCH_NAME, swtchName);
        contentValues.put(SWITCH_STATUS,1);
        db.insert(TOGGLE_SWITCH_TABLE, null, contentValues);

        return true;
    }
    public void updateToggleDetails(int id,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SWITCH_STATUS, status);
        db.update(TOGGLE_SWITCH_TABLE, contentValues, FIELD_ROW_ID + " = " + id, null);
        db.close();

    }
    public List<ToggleSwitchStatus> getToggleDetails() {
        List<ToggleSwitchStatus> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TOGGLE_SWITCH_TABLE , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ToggleSwitchStatus cont = new ToggleSwitchStatus();
            cont.setId(cursor.getInt(0));
            cont.setSwitchName(cursor.getString(1));
            cont.setSwitchStatus(cursor.getInt(2));

            array_list.add(cont);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return array_list;
    }

    *//*Pushnotification ActiveWays*//*
    public boolean insertActiveWaysNotification(String code, String data, String message, String description, String readStatus, String date_time, String wayid, String currentLocation, String ownerid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CODE, code);
        contentValues.put(DATA, data);
        contentValues.put(MESSAGE, message);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(READ_UNREAD, readStatus);
        contentValues.put(DATE_TIME, date_time);
        contentValues.put(WAY_DETAILS_WayID, wayid);
        contentValues.put(CURRENT_LOCATION, currentLocation);
        contentValues.put(OWNER_ID, ownerid);
        db.insert(PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS, null, contentValues);
        return true;
    }

    public List<NotificationDataSet> getActiveWaysNotification() {
        List<NotificationDataSet> array_list = new ArrayList<NotificationDataSet>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS + " order by "+FIELD_ROW_ID+" desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {

            NotificationDataSet notificationDataSet = new NotificationDataSet();
            notificationDataSet.setId(res.getString(0));
            notificationDataSet.setCode(res.getString(1));
            notificationDataSet.setData(res.getString(2));
            notificationDataSet.setMessages(res.getString(3));
            notificationDataSet.setDescription(res.getString(4));
            notificationDataSet.setReadstatus(res.getString(5));
            notificationDataSet.setTimedate(res.getString(6));
            notificationDataSet.setWayid(res.getString(7));
            notificationDataSet.setCurrentlocation(res.getString(8));
            array_list.add(notificationDataSet);
            res.moveToNext();
        }
        return array_list;
    }
    public Integer deleteRejectedActiveWaysPushByUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS,
                FIELD_ROW_ID+" = "+id, null);
    }
    public void updateReadActiveWaysStatus(Integer id, String readStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(READ_UNREAD, readStatus);
        db.update(PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS, contentValues, FIELD_ROW_ID + "=" + id, null);
        db.close();

    }
    public int getReadUnreadCountActiveWaysAll(){
        int unreadMsg=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS + " where "+READ_UNREAD+" = " + "1" + "", null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            res.moveToFirst();
        }
        unreadMsg=res.getCount();
        return unreadMsg;
    }
    public int getReadUnreadCountActiveWaysById(String wayid){
        int unreadMsg=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PUSHNOTIFICCATION_TABLE_NAME_ACTIVE_WAYS + " where "+READ_UNREAD+" = " + "1 AND " + WAY_DETAILS_WayID+" = "+wayid, null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            res.moveToFirst();
        }
        unreadMsg=res.getCount();
        return unreadMsg;
    }
    public boolean insertSocketConnParams() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String paramName = Constants.SKT_SESSION_ID;
        String paramValue = null;
        for (int i = 0; i < 2; i++) {
            contentValues.put(SKT_PARAM_NAME, paramName);
            contentValues.put(SKT_PARAM_VALUE, paramValue);
            db.insert(TABLE_SOCKET_CONNECTION_STATUS, null, contentValues);
            paramName = Constants.SKT_LAST_HB_TIMESTAMP;
            paramValue = "0";
        }
        return true;
    }
    public void updateSocketConnParam(String paramName, String paramValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SKT_PARAM_VALUE, paramValue);
        db.update(TABLE_SOCKET_CONNECTION_STATUS, contentValues, SKT_PARAM_NAME + "= '" + paramName+"'", null);
        db.close();
    }
    public String getSocketConnParam(String paramName){
        String paramValue = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select "+SKT_PARAM_VALUE+" from " + TABLE_SOCKET_CONNECTION_STATUS + " where "+SKT_PARAM_NAME+" = '" + paramName+"'", null);
        res.moveToFirst();
        if (!res.isAfterLast()){
            paramValue = res.getString(0);
        }
        return paramValue;
    }*/
}
