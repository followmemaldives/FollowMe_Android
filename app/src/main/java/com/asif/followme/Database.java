package com.asif.followme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.asif.followme.PublicBoats.DataPublicGroups;
import com.asif.followme.model.DataMyBoats;
import com.asif.followme.model.DataMyTrip;
import com.asif.followme.model.DataPublicBoats;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 12/18/2017.
 */

public class Database extends SQLiteOpenHelper {
    Context con;
    private static final int DATABASE_VERSION = 26;
    private static final String DATABASE_NAME 		= 	"followme";
    private static final String TABLE_MY_LIST 		= 	"my_list";
    private static final String TABLE_TRIP_LIST 	= 	"trip_plans";
    private static final String TABLE_PUBLIC_LIST 	= 	"public_list";
    private static final String KEY_ID				= 	"ID";
    private static final String KEY_NAME 			=   "NAME";
    private static final String KEY_VALUE 			=   "VALUE";
    private static final String KEY_DESC 			=   "DESC";
    private static final String KEY_IMAGE			= 	"IMAGE";
    private static final String KEY_DATE			= 	"DATE";
    private static final String KEY_BATT			=	"BATT";
    private static final String KEY_SPEED			=	"SPEED";
    private static final String KEY_ISLANDSPEED	=	"ISLANDSPEED";
    private static final String KEY_MARKER			=	"MARKER";
    private static final String KEY_NOTICE			=	"NOTICE";
    private static final String KEY_FAV			=	"FAV";
    private static final String KEY_FAV_COUNT		=	"FAV_COUNT";
    private static final String KEY_ISLAND		    =	"ISLAND";
    private static final String KEY_TRIP_STATUS	=	"TRIP_STATUS";
    private static final String KEY_CONTACT		=	"CONTACT";
    private static final String KEY_EXPIRED		=	"IS_EXPIRED";


    public Database(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
//		String TABLE_MY_CREATE = "CREATE TABLE " + TABLE_MY_LIST + "("
//	            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+ KEY_VALUE + " TEXT," + KEY_IMAGE + " TEXT," + KEY_DATE + " TEXT,"  + KEY_BATT + " TEXT,"  + KEY_SPEED + " TEXT,"  + KEY_ISLAND + " TEXT," +  KEY_MARKER + " TEXT," +KEY_NOTICE + " TEXT" + ")";
        String TABLE_MY_CREATE = "CREATE TABLE " + TABLE_MY_LIST + "("
                + KEY_ID + " TEXT, " + KEY_NAME + " TEXT, "+ KEY_VALUE + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_DATE + " TEXT, "  + KEY_BATT + " TEXT, "  + KEY_SPEED + " TEXT,"  + KEY_ISLANDSPEED + " TEXT, " +  KEY_MARKER + " TEXT, " +KEY_NOTICE + " TEXT, " +KEY_CONTACT + " TEXT," +KEY_EXPIRED+ " TEXT"+ ")";
        String TABLE_PUBLIC_CREATE = "CREATE TABLE " + TABLE_PUBLIC_LIST + "("
                + KEY_ID + " TEXT, " + KEY_NAME + " TEXT, "+ KEY_VALUE + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_DATE + " TEXT, "  + KEY_BATT + " TEXT, "  + KEY_SPEED + " TEXT,"  + KEY_ISLANDSPEED + " TEXT, " +  KEY_MARKER + " TEXT, " + KEY_NOTICE + " TEXT, "  + KEY_FAV + " INTEGER, " +KEY_FAV_COUNT + " TEXT" + "," +KEY_ISLAND + " TEXT" + "," +KEY_CONTACT + " TEXT" + ")";
        String TABLE_TRIP_CREATE = "CREATE TABLE " + TABLE_TRIP_LIST + "("
                + KEY_ID + " TEXT, " + KEY_NAME + " TEXT, "+ KEY_DESC + "TEXT, " + KEY_IMAGE + " TEXT, " + KEY_DATE + " TEXT, "  + KEY_TRIP_STATUS + " TEXT, "  + KEY_SPEED + " TEXT,"  + KEY_ISLANDSPEED + " TEXT, " +  KEY_MARKER + " TEXT, " +KEY_NOTICE + " TEXT" + ")";

        db.execSQL(TABLE_MY_CREATE);
        db.execSQL(TABLE_PUBLIC_CREATE);
        db.execSQL(TABLE_TRIP_CREATE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        System.out.println("Lets Upgrade DB..............................................................");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLIC_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_LIST);
        onCreate(db);
    }
    public void addMy(int id,String name,String value,String image,String vdate,int batt,String speed,String islandspeed,int marker,int notice,String contact, int expired)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID,id);
        values.put(KEY_NAME,name);
        values.put(KEY_VALUE,value);
        values.put(KEY_IMAGE,image);
        values.put(KEY_DATE,vdate);
        values.put(KEY_BATT, batt);
        values.put(KEY_SPEED, speed);
        values.put(KEY_ISLANDSPEED, islandspeed);
        values.put(KEY_MARKER, marker);
        values.put(KEY_NOTICE, notice);
        values.put(KEY_CONTACT, contact);
        values.put(KEY_EXPIRED, expired);
//	    System.out.println(image+"......INSERT");
        db.insert(TABLE_MY_LIST,null, values);
        db.close();
    }
    public void addPublic(String id,String name,String value,String image,String vdate,String batt,String speed,String islandspeed,String marker,String notice,String fav,String fav_count,String island, String contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID,id);
        values.put(KEY_NAME,name);
        values.put(KEY_VALUE,value);
        values.put(KEY_IMAGE,image);
        values.put(KEY_DATE,vdate);
        values.put(KEY_BATT, batt);
        values.put(KEY_SPEED, speed);
        values.put(KEY_ISLANDSPEED, islandspeed);
        values.put(KEY_MARKER, marker);
        values.put(KEY_NOTICE, notice);
        values.put(KEY_FAV, fav);
        values.put(KEY_FAV_COUNT, fav_count);
        values.put(KEY_ISLAND,island);
        values.put(KEY_CONTACT,contact);
//	    System.out.println(image+"......INSERT");
        db.insert(TABLE_PUBLIC_LIST, null, values);
        db.close();
    }
    public void addPublicGroup(String id,String name,String count,String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID,id);
        values.put(KEY_NAME,name);
        values.put(KEY_FAV_COUNT,count);
        values.put(KEY_IMAGE,image);
        db.insert(TABLE_PUBLIC_LIST, null, values);
        db.close();
    }
    public void addTrip(String id,String name,String value,String image,String vdate,String trip_status,String speed,String islandspeed,String marker,String notice)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID,id);
        values.put(KEY_NAME,name);
        values.put(KEY_DESC,value);
        values.put(KEY_IMAGE,image);
        values.put(KEY_DATE,vdate);
        values.put(KEY_TRIP_STATUS, trip_status);
        values.put(KEY_SPEED, speed);
        values.put(KEY_ISLANDSPEED, islandspeed);
        values.put(KEY_MARKER, marker);
        values.put(KEY_NOTICE, notice);
//	    System.out.println(image+"......INSERT");
        db.insert(TABLE_TRIP_LIST,null, values);
        db.close();
    }
    public List<DataMyBoats> fetchallMyBoatsdata() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataMyBoats> array = new ArrayList<DataMyBoats>();
        Cursor cursor = db.query(TABLE_MY_LIST, new String[] {KEY_ID,KEY_NAME,KEY_VALUE,KEY_IMAGE,KEY_DATE,KEY_BATT,KEY_SPEED,KEY_ISLANDSPEED,KEY_MARKER,KEY_NOTICE,KEY_CONTACT,KEY_EXPIRED},null, null, null, null, null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataMyBoats data = new DataMyBoats();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setValue(cursor.getString(2));
                data.setImage(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setBatt(cursor.getInt(5));
                data.setSpeed(cursor.getString(6));
                data.setIsland(cursor.getString(7));
                data.setMarker(cursor.getInt(8));
                data.setNotice(cursor.getInt(9));
                data.setContact(cursor.getString(10));
                data.setIsExpired(cursor.getInt(11));

                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
/*    public List<DataMyGroups> fetchallMyGroupsData() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataMyGroups> array = new ArrayList<DataMyGroups>();
        Cursor cursor = db.query(TABLE_MY_LIST, new String[] {KEY_ID,KEY_NAME,KEY_VALUE,KEY_IMAGE,KEY_DATE,KEY_BATT,KEY_SPEED,KEY_ISLANDSPEED,KEY_MARKER,KEY_NOTICE},null, null, null, null, null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataMyGroups data = new DataMyGroups();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setValue(cursor.getString(2));
                data.setImage(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setBatt(cursor.getInt(5));
                data.setSpeed(cursor.getString(6));
                data.setIsland(cursor.getString(7));
                data.setMarker(cursor.getInt(8));
                data.setNotice(cursor.getInt(9));

                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
*/
    public List<DataPublicBoats> fetchallPublicdata() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataPublicBoats> array = new ArrayList<DataPublicBoats>();
        Cursor cursor = db.query(TABLE_PUBLIC_LIST, new String[] {KEY_ID,KEY_NAME,KEY_VALUE,KEY_IMAGE,KEY_DATE,KEY_BATT,KEY_SPEED,KEY_ISLANDSPEED,KEY_MARKER,KEY_NOTICE,KEY_FAV,KEY_FAV_COUNT,KEY_ISLAND,KEY_CONTACT},null, null, null, null, null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataPublicBoats data = new DataPublicBoats();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setValue(cursor.getString(2));
                data.setImage(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setBatt(cursor.getInt(5));
                data.setSpeed(cursor.getString(6));
                data.setIslandSpeed(cursor.getString(7));
                data.setMarker(cursor.getInt(8));
                data.setNotice(cursor.getInt(9));
                data.setFav(cursor.getInt(10));
                data.setFavCount(cursor.getString(11));
                data.setIsland(cursor.getString(12));
                data.setContact(cursor.getString(13));

                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
    public List<DataPublicGroups> fetchPublicGroups() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataPublicGroups> array = new ArrayList<DataPublicGroups>();
        Cursor cursor = db.query(TABLE_PUBLIC_LIST, new String[] {KEY_ID,KEY_NAME,KEY_FAV_COUNT,KEY_IMAGE},null, null, null, null, null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataPublicGroups data = new DataPublicGroups();
                data.setId(cursor.getInt(0));
                data.setName(cursor.getString(1));
                data.setCount(cursor.getInt(2));
                data.setImage(cursor.getString(3));

                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
    public void deleteMy()
    {
        //con.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MY_LIST, null, null);
    }
    public void deletePublic()
    {
        //con.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PUBLIC_LIST, null, null);
    }
    public void deleteTrip()
    {
        //con.deleteDatabase(DATABASE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRIP_LIST, null, null);
    }
    public List<DataPublicBoats> fetchSinglePublicdata(String device_id) throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataPublicBoats> array = new ArrayList<DataPublicBoats>();
//        Cursor cursor = db.query(TABLE_PUBLIC_LIST, new String[] {KEY_ID,KEY_NAME,KEY_VALUE,KEY_IMAGE,KEY_DATE,KEY_BATT,KEY_SPEED,KEY_ISLANDSPEED,KEY_MARKER,KEY_NOTICE,KEY_FAV,KEY_FAV_COUNT,KEY_ISLAND},null, null, null, null, null,null);
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_PUBLIC_LIST+" WHERE "+KEY_ID+" = '"+device_id+"'", null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataPublicBoats data = new DataPublicBoats();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setValue(cursor.getString(2));
                data.setImage(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setBatt(cursor.getInt(5));
                data.setSpeed(cursor.getString(6));
                data.setIslandSpeed(cursor.getString(7));
                data.setMarker(cursor.getInt(8));
                data.setNotice(cursor.getInt(9));
                data.setFav(cursor.getInt(10));
                data.setFavCount(cursor.getString(11));
                data.setIsland(cursor.getString(12));
                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
    public List<DataMyTrip> fetchallMyTripdata() throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<DataMyTrip> array = new ArrayList<DataMyTrip>();
        Cursor cursor = db.query(TABLE_TRIP_LIST, new String[] {KEY_ID,KEY_NAME,KEY_DESC,KEY_IMAGE,KEY_DATE,KEY_TRIP_STATUS,KEY_SPEED,KEY_ISLANDSPEED,KEY_MARKER,KEY_NOTICE},null, null, null, null, null,null);
        if (cursor.moveToFirst())
        {
            do
            {
                DataMyTrip data = new DataMyTrip();
                data.setId(cursor.getString(0));
                data.setName(cursor.getString(1));
                data.setValue(cursor.getString(2));
                data.setImage(cursor.getString(3));
                data.setDate(cursor.getString(4));
                data.setTripStatus(cursor.getString(5));
                data.setSpeed(cursor.getString(6));
                data.setIsland(cursor.getString(7));
                data.setMarker(cursor.getInt(8));
                data.setNotice(cursor.getInt(9));

                array.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }
}
