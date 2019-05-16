package com.asif.followme.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.asif.followme.FireMessagingService;
import org.json.JSONArray;

/**
 * Created by user on 12/18/2017.
 */

public class SettingsPreferences {
//    static final String KEY = "com.asif.followme";
//    private static final String FOLLOWME_PREFS = KEY+"_prefs";
    private static final String SESSION_ID = "sid";
    private static final String SESSION_KEY = "session_key";
    private static final String KEY_COUNTRY = "key_country";
    private static final String COUNTRY_LAT = "country_lat";
    private static final String COUNTRY_LON = "country_lon";
    private static final String COUNTRY_ZOOM = "country_zoom";
    private static final String MAP_TYPE = "map_type";
    private static final String IMEI = "imei";
    private static final String OPERATOR = "operator";
    private static final String DEGREE = "degree";
    private static final String SELECTED_PUBLIC_ACCOUNT = "selectedPublicAccount";
    private static final String MY_NAVIGATION_INDEX = "myNavIndex";
    private static final String MY_NAVIGATION_NAME = "myNavName";
    private static final String MY_NAVIGATION_ID = "myNavId";
    private static final String PUBLIC_NAVIGATION_NAME = "publicNavName";
    private static final String SELECTED_DEVICE_ID = "selectedDeviceID";
    private static final String SELECTED_DEVICE_NAME = "selectedDeviceName";
    private static final String LISTED_PUBLIC_TIME = "listpublictime";
    private static final String ACCOUNT = "account";
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String PASSWORD = "password";
    private static final String IS_TRACKER = "is_tracker";
    private static final String SELECTED_MY_ACCOUNT = "selected_my_account";
    private static final String STORAGE_PERMISSION = "storagePermission";
    private static final String NOTIFICATION_DATA = "notificationData";
    private static final String ALERT_MOVE = "move_alert";
    private static final String ALERT_MINOR = "minor_alert";
    private static final String ALERT_MAJOR = "major_alert";
    private static final String ALERT_HIRE = "hire_alert";
    private static final String TRACK_ME = "trackMe";
    private static final String SELECTED_SHARED_USER = "selectedSharedUser";
    private static final String AD= "ad";
    private static final String FAV_ID= "fav_id";
    private static final String SOUND_MOVE = "move_sound";
    private static final String SOUND_MINOR = "minor_sound";
    private static final String SOUND_MAJOR = "major_sound";
    private static final String SOUND_HIRE = "hire_sound";
    private static final String SELECTED_ATOLL = "selected_atoll";
    private static final String PUBLIC_TITLE = "public_title";
    private static final String UPDATE_INTERVAL_TIME = "update_interval_time";
    private static final String TOKEN_REF = "token_ref";
    private static final String HIRE_ALERT_COUNT = "hire_alert_count";
    private static final String BOAT_HIRE_SERVICE = "boat_hire_service";
    private static final String CONTACT_VERIFIED = "mobile_verified";
    private static final String MY_TRACKER_ID = "my_tracker_id";
    private static final String TRACK_IP = "track_ip";
    private static final String TRACK_PORT = "track_port";
    private static final String VERSION_CODE = "version_code";
    private static final String SELECTED_AIRPORT_CODE = "airport_code";

    private static final String TRIP_FITER_INDEX = "trip_filter_index";
    private static final String TRIP_FITER_DATE_FROM = "trip_filter_date_from";
    private static final String TRIP_FITER_DATE_TO = "trip_filter_date_to";


    public static void setContactVerified(Context context, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(CONTACT_VERIFIED, status);
        prefEditor.commit();
    }
    public static boolean getContactVerified(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(CONTACT_VERIFIED, false);
    }
    public static void setVersionCode(Context context, int version) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(VERSION_CODE, version);
        prefEditor.commit();
    }
    public static int getVersionCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(VERSION_CODE, 0);
    }
    public static void setSelectedAirportCode(Context context, String code) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(SELECTED_AIRPORT_CODE, code);
        prefEditor.commit();
    }
    public static String getSelectedAirportCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SELECTED_AIRPORT_CODE, "");
    }
    public static void setBoatHireService(Context context, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(BOAT_HIRE_SERVICE, status);
        prefEditor.commit();
    }
    public static boolean getBoatHireService(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(BOAT_HIRE_SERVICE, false);
    }
    public static void setHireAlertCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int count = prefs.getInt(HIRE_ALERT_COUNT, 0)+1;
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(HIRE_ALERT_COUNT, count);
        prefEditor.commit();
    }
    public static int getHireAlertCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(HIRE_ALERT_COUNT, 0);
    }
    public static void clearHireAlertCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(HIRE_ALERT_COUNT, 0);
        prefEditor.commit();
    }
    public static void setCountry(Context context, String country) {
        System.out.println("Lets Set Country to: "+country+" +++++++++++++++++++++++++++++___________________+++++++++");
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(KEY_COUNTRY, country);
        prefEditor.commit();
    }
    public static String getCountry(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(KEY_COUNTRY, "");
    }
    public static void setSelectedAtoll(Context context, int atoll) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(SELECTED_ATOLL, atoll);
        prefEditor.commit();
    }
    public static int getSelectedAtoll(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(SELECTED_ATOLL, 0);
    }
    public static void setPublicTitle(Context context, String title) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(PUBLIC_TITLE, title);
        prefEditor.commit();
    }
    public static String getPublicTitle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PUBLIC_TITLE, "Public Vessels");
    }


    public static void setTripPlanFilter(Context context, int filter_index, String date_from, String date_to) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(TRIP_FITER_INDEX, filter_index);
        prefEditor.putString(TRIP_FITER_DATE_FROM,date_from);
        prefEditor.putString(TRIP_FITER_DATE_TO,date_to);
        prefEditor.commit();
    }
    public static String[] getTripPlanFilter(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] mStrings = new String[3];
 //       List<String>[] arrayOfList = new List[3];
        mStrings[0] = String.valueOf(prefs.getInt(TRIP_FITER_INDEX,0));
        mStrings[1] = prefs.getString(TRIP_FITER_DATE_FROM,"");
        mStrings[2] = prefs.getString(TRIP_FITER_DATE_TO,"");
        return mStrings;
    }

    /*    public static void setMap(Context context, int map) {
        System.out.println("Lets Set Map to: "+map+" +++++++++++++++++++++++++++++___________________+++++++++");
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(MAP_TYPE, map);
        prefEditor.commit();
    }
    public static int getMap(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(MAP_TYPE,4);
    }
*/
    public static void setCountryLat(Context context, String lat) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(COUNTRY_LAT, lat);
        prefEditor.commit();
    }
    public static String getCountryLat(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(COUNTRY_LAT,"0.0");
    }
    public static void setCountryLon(Context context, String lon) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(COUNTRY_LON, lon);
        prefEditor.commit();
    }
    public static String getCountryLon(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(COUNTRY_LON,"0.0");
    }
    public static void setCountryZoom(Context context, int zoom) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNTRY_ZOOM, zoom);
        prefEditor.commit();
    }
    public static int getCountryZoom(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(COUNTRY_ZOOM,10);
    }
    public static void setTokenRef(Context context, String ref) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(TOKEN_REF, ref);
        prefEditor.commit();
    }
    public static String getTokenRef(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(TOKEN_REF,"0");
    }
    public static void setIMEI(Context context, String imei) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(IMEI, imei);
        prefEditor.commit();
    }
/*
    public static String getIMEI(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(IMEI, null);
    }
   */
    public static String getIMEI(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String imei_id = prefs.getString(IMEI,"0");
            if(imei_id.length()<6){
                imei_id = Long.toString(System.currentTimeMillis());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString(IMEI, imei_id);
                prefEditor.commit();
            }
        return imei_id;
    }
    public static void setOperator(Context context, String operator) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(OPERATOR, operator);
        prefEditor.commit();
    }
    public static String getOperator(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(OPERATOR, null);
    }

    public static int getSelectedPublicAccount(Context context) {	//public vessels,public group
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(SELECTED_PUBLIC_ACCOUNT, 0);
    }
    public static void setSelectedPublicAccount(Context context, int id2) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(SELECTED_PUBLIC_ACCOUNT, id2);
        prefEditor.commit();
    }
    public static void setSelectedItemID(Context context, String device_id) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(SELECTED_DEVICE_ID, device_id);
        prefEditor.commit();
    }
    public static String getSelectedItemID(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(SELECTED_DEVICE_ID, "");
    }
    public static void setSelectedItemName(Context context, String device_name) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(SELECTED_DEVICE_NAME, device_name);
        prefEditor.commit();
    }
    public static String getSelectedItemName(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(SELECTED_DEVICE_NAME, "");
    }
    public static void setDegreeFormat(Context context, int deg) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(DEGREE, deg);
        prefEditor.commit();
    }
    public static int getDegreeFormat(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getInt(DEGREE, 0);
    }

    public static void setListPublicTime(Context context, long t) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putLong(LISTED_PUBLIC_TIME, t);
        prefEditor.commit();
    }
    public static long getListPublicTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS, Context.MODE_PRIVATE);
        return prefs.getLong(LISTED_PUBLIC_TIME, 0);
    }

/*    public static void setAccount(Context context, String account) {
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(ACCOUNT, account);
        prefEditor.commit();
    }
    public static String getAccount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(ACCOUNT, "public");
    }
*/

    public static void setFullName(Context context, String fullname) {
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(FULLNAME, fullname);
        prefEditor.commit();
    }
    public static String getFullName(Context context) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(FULLNAME, "");
    }
    public static void setUserName(Context context, String username) {
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(USERNAME, username);
        prefEditor.commit();
    }
    public static String getUserName(Context context) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(USERNAME, "");
    }
    public static void setPassword(Context context, String pass) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(PASSWORD, pass);
        prefEditor.commit();
    }
    public static String getPassword(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(PASSWORD, "");
    }

    public static void setSessionID(Context context, String sid) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        if(sid.length() > 6) {
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(SESSION_ID, sid);
            prefEditor.commit();
        }
    }

    public static String getSessionID(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(SESSION_ID, "");
    }

    public static void setSessionKey(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putString(SESSION_KEY, key);
            prefEditor.commit();
    }

    public static String getSessionKey(Context context) {   //this key may change. Used for attacks prevention
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SESSION_KEY, "");
    }
/*    public static void setIsTracker(Context context, Boolean yes) {	//is mobile phone added as tracker
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(IS_TRACKER, yes);
        prefEditor.commit();
        //System.out.println("TRACK_ME:"+me+",Saved");
    }
    public static Boolean getIsTracker(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(IS_TRACKER, false);
    }
*/
    public static void setMyTracker(Context context, String id) {	//is mobile phone added as tracker
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(MY_TRACKER_ID, id);
        prefEditor.commit();
        //System.out.println("TRACK_ME:"+me+",Saved");
    }
    public static String getMyTracker(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(MY_TRACKER_ID, "0");
    }
    public static void setSelectedMyAccount(Context context, int id2) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(SELECTED_MY_ACCOUNT, id2);
        prefEditor.commit();
    }
    public static int getSelectedMyAccount(Context context) {	//my account, public vessels
        @SuppressWarnings("deprecation")
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getInt(SELECTED_MY_ACCOUNT, 0);
    }

/*    public static void setMyNavigationIndex(Context context, int id) {	//my account, public vessels
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(MY_NAVIGATION_INDEX, id);
        prefEditor.commit();
    }
    public static int getMyNavigationIndex(Context context) {	//my account, public vessels
        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getInt(MY_NAVIGATION_INDEX, 0);
    }
*/
    public static void setMyNavigationName(Context context, String name) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(MY_NAVIGATION_NAME, name);
        prefEditor.commit();
    }
    public static String getMyNavigationName(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        return prefs.getString(MY_NAVIGATION_NAME, "nav_my_vessels");
    }
    public static void setMyNavigationId(Context context, int id) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(MY_NAVIGATION_ID, id);
        prefEditor.commit();
    }
    public static int getMyNavigationId(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(MY_NAVIGATION_ID, 0);
    }
    public static void setPublicNavigationName(Context context, String name) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(PUBLIC_NAVIGATION_NAME, name);
        prefEditor.commit();
    }
    public static String getPublicNavigationName(Context context) {	//my account, public vessels
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PUBLIC_NAVIGATION_NAME, "nav_public_vessels");
    }
    public static JSONArray setNotifications(Context context, String notification) {
        int size=0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        //ArrayList<String> a = new ArrayList<String>();
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        try {

            jsonArray2 = new JSONArray(prefs.getString(NOTIFICATION_DATA, "[]"));
            if(jsonArray2.length()>6){size=6;} else {size=jsonArray2.length();}
//			System.out.println("Array2"+ jsonArray2.toString());
//			System.out.println("Array2 Length:"+jsonArray2.length()+",Size:"+size);
            jsonArray1.put(0,notification);
            for (int i = 0; i < size; i++) {
                jsonArray1.put(i+1,jsonArray2.get(i));
            }
            //jsonArray2.put(0,notification);
        } catch (Exception e) {
            Log.i("Error1", e.toString());
            e.printStackTrace();
        }
//		System.out.println(jsonArray1.toString());
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(NOTIFICATION_DATA, jsonArray1.toString());
        prefEditor.commit();
        return jsonArray1;
    }

    public static boolean getStoragePermission(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(STORAGE_PERMISSION,false);
    }

    public static void setStoragePermission(Context context, boolean perm) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(STORAGE_PERMISSION, perm);
        prefEditor.commit();
    }

    public static void setAlertMove(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(ALERT_MOVE, alert);
        prefEditor.commit();
    }
    public static Boolean getAlertMove(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ALERT_MOVE, true);
    }

    public static void setAlertMinor(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(ALERT_MINOR, alert);
        prefEditor.commit();
    }
    public static void setSoundMove(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SOUND_MOVE, alert);
        prefEditor.commit();
    }
    public static Boolean getSoundMove(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SOUND_MOVE, false);
    }
    public static void setSoundMinor(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SOUND_MINOR, alert);
        prefEditor.commit();
    }
    public static Boolean getSoundMinor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SOUND_MINOR, false);
    }
    public static void setSoundMajor(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SOUND_MAJOR, alert);
        prefEditor.commit();
    }
    public static Boolean getSoundMajor(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SOUND_MAJOR, false);
    }
    public static void setSoundHire(Context context, boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(SOUND_HIRE, alert);
        prefEditor.commit();
    }
    public static Boolean getSoundHire(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(FOLLOWME_PREFS,	Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(SOUND_HIRE, false);
    }
    public static Boolean getAlertMinor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ALERT_MINOR, true);
    }

    public static Boolean getAlertMajor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ALERT_MAJOR, true);
    }

    public static void setAlertMajor(Context context, Boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(ALERT_MAJOR, alert);
        prefEditor.commit();
    }

    public static Boolean getAlertHire(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ALERT_HIRE, true);
    }

    public static void setAlertHire(Context context, Boolean alert) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putBoolean(ALERT_HIRE, alert);
        prefEditor.commit();
    }
    public static String getNotifications(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(NOTIFICATION_DATA, "[]");
    }
    public static void clearNotifications(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(NOTIFICATION_DATA, "[]");
        prefEditor.commit();
        if(FireMessagingService.r!=null){
            FireMessagingService.r.stop();
        }
    }
    public static void setTrackMe(Context context, String me) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(TRACK_ME, me);
        prefEditor.commit();
        //System.out.println("TRACK_ME:"+me+",Saved");
    }
    public static String getTrackMe(Context context) {	//is track me button clicked?
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        System.out.println("TRACK_ME:"+prefs.getString(TRACK_ME, "")+",Read");
        return prefs.getString(TRACK_ME, "");
    }
    public static void setTrackIP(Context context, String ip) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(TRACK_IP, ip);
        prefEditor.commit();
        //System.out.println("TRACK_ME:"+me+",Saved");
    }
    public static String getTrackIP(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(TRACK_IP, "followme.mv");
    }
    public static void setTrackPort(Context context, int port) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(TRACK_PORT, port);
        prefEditor.commit();
    }
    public static int getTrackPort(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(TRACK_PORT, 60005);
    }
    public static void setSelectedSharedUser(Context context, String user_id) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(SELECTED_SHARED_USER, user_id);
        prefEditor.commit();
    }
    public static String getSelectedSharedUser(Context context) {	//my account, public vessels
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SELECTED_SHARED_USER, "");
    }
    public static void setAd(Context context, String ad) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(AD, ad);
        prefEditor.commit();
    }
    public static String getAd(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(AD, null);
    }
    public static void clear(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.clear();
        prefEditor.commit();

    }
    public static int getUpdateIntervalIndex(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(UPDATE_INTERVAL_TIME, 1);
    }
    public static long getUpdateIntervalTime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int t= prefs.getInt(UPDATE_INTERVAL_TIME, 1);
        long s;
        switch(t){
            case 1:
                s=5*60*1000;	// 5 minute
                break;
            case 2:
                s=15*60*1000;	//15 minutes
                break;
            case 3:
                s=30*60*1000;	//30 minutes
                break;
            case 4:
                s=60*60*1000;	//1 hour
                break;
            default:
                s=5*60*1000;	// 5 minute
                break;
        }
        return s;

    }
}
