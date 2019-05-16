package com.asif.followme.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import com.asif.followme.BoatHire.DataBids;
import com.asif.followme.BoatHire.Operator.DataMyHire;
import com.asif.followme.BoatHire.Public.DataPublicHire;
import com.asif.followme.Database;
import com.asif.followme.Flight.DataAirports;
import com.asif.followme.Flight.DataFlightSchedule;
import com.asif.followme.MyAccount.DataMyGroups;
import com.asif.followme.MyAccount.DataMyShare;
import com.asif.followme.TripPlan.*;
import com.asif.followme.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by user on 12/18/2017.
 */

public class AppUtils {
    static Context con;
    String perm;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static NotificationManager mManager1,mManager2;

/*
    public String getImeiNumber(Context context) {
        String deviceId = "0";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();

        }
        return deviceId;
    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(LoadingActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Read IMEI Permission is required")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(AppUtils.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(R.drawable.onlinlinew_warning_sign)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(AppUtils.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .setIcon(R.drawable.onlinlinew_warning_sign)
                .show();
    }

    */

    public static void savePublicVessels(JSONObject jObj, Context context){
        JSONArray alldata;
        //String now_date = null;
        int accountType = SettingsPreferences.getSelectedPublicAccount(context);
        System.out.println("This is savePublicVessel");
        try {
            alldata = jObj.getJSONArray("aaData");
//			if(alldata.length()>0){
//				now_date=jObj.getString("now");
//			}
            //	SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, owner,imageName,timeAgo,batt,speed,islandspeed,marker,notice,fav,fav_count, island, contact;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            owner = new ArrayList<String>();
            timeAgo = new ArrayList<String>();
            batt  = new ArrayList<String>();
            speed  = new ArrayList<String>();
            islandspeed  = new ArrayList<String>();
            marker = new ArrayList<String>();
            notice = new ArrayList<String>();
            fav = new ArrayList<String>();
            fav_count = new ArrayList<String>();
            island = new ArrayList<String>();
            contact = new ArrayList<String>();
 //           String my_imei=SettingsPreferences.getIMEI(context);
//			int acc = SettingsPreferences.getSelectedAccount(context);
            String imei_found="";
            if(alldata.length()>0){
                for (int i = 0; i < alldata.length(); i++) {

                    JSONArray jsonArray = alldata.getJSONArray(i);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        String ss = jsonArray.getString(j);
                        if (j==0) {
                            id.add(ss);
                        }
                        if (j==1) {
                            name.add(ss);
                        }
                        if (j==2) {
                            owner.add(ss);
                        }
                        if (j==3) {
                            imageName.add(ss);
                        }
                        if (j==4) {
                            timeAgo.add(ss);
                        }
                        if(j==5){
                            batt.add(ss);
                        }
                        if(j==6){
                            speed.add(ss);
                        }
                        if(j==7){
                            islandspeed.add(ss);
                        }
                        if(j==8){
                            marker.add(ss);
                        }
                        if(j==9){
                            notice.add(ss);
                        }
                        if (j==10){
                            fav.add(ss);
                            //	if(ss.equalsIgnoreCase(my_imei)){
                            //		imei_found="ok";
                        }
                        if(j==11){
                            fav_count.add(ss);
                        }
                        if(j==12){
                            island.add(ss);
                        }
                        if(j==13){
                            contact.add(ss);
                        }


                        //}

                    }
                }
            } else {
                //Toast.makeText(context, "List is empty. Please goto Preferences and check your Country", Toast.LENGTH_LONG).show();

            }
            Database db = new Database(context);
            db.deletePublic();
            for (int i = 0; i < alldata.length(); i++) {
//				System.out.println(imageName.get(i)+".........NAME");
                String  imageUrl=AppConstants.IMAGE_SMALL_URL+imageName.get(i);
                //imageLoader.DisplayImage(imageUrl,new ImageView(this), new ProgressBar(this));
                //					imageLoader.DisplayImage(MainActivity.files[i],new ImageView(con));
                //db.add(id.get(i), name.get(i), owner.get(i),String.valueOf(fileCache.getFile(imageUrl)));
                //	String s="";
                //	if(accountType!=2){
                //		v = AppUtils.getTimeAgo(vDate.get(i),now_date);
                //	}
                db.addPublic(id.get(i),
                        name.get(i),
                        owner.get(i),
                        imageUrl,
                        timeAgo.get(i),
                        batt.get(i),
                        speed.get(i),
                        islandspeed.get(i),
                        marker.get(i),
                        notice.get(i),
                        fav.get(i),
                        fav_count.get(i),
                        island.get(i),
                        contact.get(i));

            }

            //	SettingsPreferences.setTrackMe(context, imei_found);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


//		if (statuss.equalsIgnoreCase(statuspass)) {


        //	}else if (statuss.equalsIgnoreCase(statusfailed)) {

    }
    public static void savePublicGroups(JSONObject jObj, Context context){
        JSONArray alldata;
        String id, name, count, image;
        String image_url = AppConstants.IMAGE_SMALL_URL;
        try {
            alldata = jObj.getJSONArray("aaData");
            Database db = new Database(context);
            db.deletePublic();
            if(alldata.length()>0){
                for (int i = 0; i < alldata.length(); i++) {
                    id =alldata.getJSONArray(i).getString(0);
                    name =alldata.getJSONArray(i).getString(1);
                    count = alldata.getJSONArray(i).getString(2);
                    image = alldata.getJSONArray(i).getString(3);
                    String  imageUrl=image_url+image;
                    //System.out.println("Image:"+imageUrl);
                    db.addPublicGroup(id, name, count, imageUrl);
                }
            } else {
                //Toast.makeText(context, "List is empty. Please goto Preferences and check your Country", Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latitude >= 0 ? "N" : "S";
            String lonDegrees = longitude >= 0 ? "E" : "W";

            return  Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree +", "+ Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {

            return ""+ String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude) ;
        }

    }

    public static void showAlertDialog(Context context,String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static void showConfirmDialog(Context context, String title, String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




    public static List<DataMyBoats> drawMyVessel(JSONObject jObj, Context context){
 //       public static void saveMyVessel(JSONObject jObj,Context context){
        JSONArray alldata;
        List<DataMyBoats> array = new ArrayList<DataMyBoats>();
        try {
            alldata = jObj.getJSONArray("aaData");
            Database db = new Database(context);
            db.deleteMy();

            for (int i = 0; i < alldata.length(); i++) {
                DataMyBoats data = new DataMyBoats();
                JSONArray jsonArray = alldata.getJSONArray(i);
                data.setId(jsonArray.getString(0));
                data.setName(jsonArray.getString(1));
                data.setValue(jsonArray.getString(2));
                data.setImage(jsonArray.getString(3));
                data.setDate(jsonArray.getString(4));
                data.setBatt(jsonArray.getInt(5));
                data.setSpeed(jsonArray.getString(6));
                data.setIsland(jsonArray.getString(7));
                data.setMarker(jsonArray.getInt(8));
                data.setNotice(jsonArray.getInt(9));
                data.setContact(jsonArray.getString(10));
                data.setIsExpired(jsonArray.getInt(11));

                db.addMy(jsonArray.getInt(0),
                        jsonArray.getString(1),
                        jsonArray.getString(2),
                        jsonArray.getString(3),
                        jsonArray.getString(4),
                        jsonArray.getInt(5),
                        jsonArray.getString(6),
                        jsonArray.getString(7),
                        jsonArray.getInt(8),
                        jsonArray.getInt(9),
                        jsonArray.getString(10),
                        jsonArray.getInt(11));
                array.add(data);
            }



        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }

/*    public static void drawMyGroups(JSONObject jObj,Context context){
        JSONArray alldata;
        //String now_date = null;
      //  int accountType = SettingsPreferences.getSelectedMyAccount(context);
        try {
            alldata = jObj.getJSONArray("aaData");
            List<String> id, name, count,icon;
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            count = new ArrayList<String>();
            icon = new ArrayList<String>();
            String my_imei=SettingsPreferences.getIMEI(context);
            String imei_found="";

            for (int i = 0; i < alldata.length(); i++) {

                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        id.add(ss);
                    }
                    if (j==1) {
                        name.add(ss);
                    }
                    if (j==2) {
                        count.add(ss);
                    }
                    if (j==3) {
                        icon.add(ss);
                    }

                }
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
*/

    public static List<DataMyGroups> drawMyGroups(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataMyGroups> array = new ArrayList<DataMyGroups>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, description,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            description = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataMyGroups data = new DataMyGroups();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setCount(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }

    public static ArrayList<Data_vtypes> setVesselTypes(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        //Add countries
        try {
            vdata = jObj.getJSONArray("typeData");
            for (int i = 0; i < vdata.length(); i++) {
                JSONArray jsonArray = vdata.getJSONArray(i);
                //	System.out.println(jsonArray);
                vtypeList.add(new Data_vtypes(i,jsonArray.getInt(0),jsonArray.getString(1)));
                //   countryList.add(new Data_vtypes("1", "India"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return vtypeList;
        //fill data in spinner
        //       ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(context, android.R.layout.simple_spinner_item, countryList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        return adapter;
        //  spinner_country.setAdapter(adapter);
    }
    public static ArrayList<DataBoats> setBoatData(JSONArray data,Context context) {   //setTripBoats
        ArrayList<DataBoats> boatList = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONArray jsonArray = data.getJSONArray(i);
                boatList.add(new DataBoats(i,jsonArray.getInt(0),jsonArray.getString(1),jsonArray.getInt(2)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return boatList;
    }
    public static ArrayList<DataRouteInfo> setRouteData(JSONArray data, Context context) {   //setTripBoats
        ArrayList<DataRouteInfo> routeList = new ArrayList<>();
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONArray jsonArray = data.getJSONArray(i);
               // String a1 = jsonArray.getString(2);
               // JSONArray dd= new JSONArray(a1);
               // JSONObject rObj = dd.getJSONObject(0);

                routeList.add(new DataRouteInfo(jsonArray.getInt(0),jsonArray.getString(1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeList;
    }
    public static ArrayList<Data_vtypes> setBookingsFromSpinner(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONArray("legData");
            for (int i = 0; i < vdata.length()-1; i++) {    //Removed last island, as cannot start from this island
                JSONArray jsonArray = vdata.getJSONArray(i);
                vtypeList.add(new Data_vtypes(i,jsonArray.getInt(1),jsonArray.getString(2)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<Data_vtypes> setBookingsToSpinner(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONArray("legData");
            for (int i = 1; i < vdata.length(); i++) {    //Removed first island, as cannot go to this island
                JSONArray jsonArray = vdata.getJSONArray(i);
                vtypeList.add(new Data_vtypes(i,jsonArray.getInt(1),jsonArray.getString(2)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<Data_vtypes> setBookingsStatusSpinner(JSONObject jObj,Context context) {
        JSONObject vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONObject("bsData");    //booking status data
            Iterator<String> iter = vdata.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                int i = Integer.parseInt(key);
                try {
                    //Object value = vdata.get(key);
                    String v = vdata.get(key).toString();
                    vtypeList.add(new Data_vtypes(i,i,v));
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<Data_vtypes> setBookingsFromSpinnerAny(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONArray("legData");
            vtypeList.add(new Data_vtypes(0,0,"Any"));
            for (int i = 0; i < vdata.length()-1; i++) {    //Removed last island, as cannot start from this island
                JSONArray jsonArray = vdata.getJSONArray(i);
                vtypeList.add(new Data_vtypes(i+1,jsonArray.getInt(1),jsonArray.getString(2)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<Data_vtypes> setBookingsToSpinnerAny(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONArray("legData");
            vtypeList.add(new Data_vtypes(0,0,"Any"));
            for (int i = 1; i < vdata.length(); i++) {    //Removed first island, as cannot go to this island
                JSONArray jsonArray = vdata.getJSONArray(i);
                vtypeList.add(new Data_vtypes(i,jsonArray.getInt(1),jsonArray.getString(2)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<Data_vtypes> setBookingsStatusSpinnerAny(JSONObject jObj,Context context) {
        JSONArray vdata;
        ArrayList<Data_vtypes> vtypeList = new ArrayList<>();
        try {
            vdata = jObj.getJSONArray("bsData");    //booking status data
            for (int i = 0; i < vdata.length(); i++) {
                JSONArray jsonArray = vdata.getJSONArray(i);
                vtypeList.add(new Data_vtypes(i,i,jsonArray.getString(1)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vtypeList;
    }
    public static ArrayList<DataETA> setETA(JSONObject jObj, Context context) {
        JSONArray etaData;
        ArrayList<DataETA> etaList = new ArrayList<>();
        try {
            etaData = jObj.getJSONArray("etaData");
            for (int i = 0; i < etaData.length(); i++) {
                JSONArray jsonArray = etaData.getJSONArray(i);
                etaList.add(new DataETA(i,jsonArray.getInt(0),jsonArray.getString(1),jsonArray.getString(2),jsonArray.getString(3)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return etaList;
    }
    public static ArrayList<NameValuePairs> setNameValuePairs(JSONObject jObj, Context context) {
        JSONArray pairs;
        ArrayList<NameValuePairs> pairList = new ArrayList<>();
        //Add countries
        try {
            pairs = jObj.getJSONArray("pairs");
            for (int i = 0; i < pairs.length(); i++) {
                JSONArray jsonArray = pairs.getJSONArray(i);
                //	System.out.println(jsonArray);
                pairList.add(new NameValuePairs(i,jsonArray.getInt(0),jsonArray.getString(1)));
                //   countryList.add(new Data_vtypes("1", "India"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pairList;
        //fill data in spinner
        //       ArrayAdapter<Data_vtypes> adapter = new ArrayAdapter<Data_vtypes>(context, android.R.layout.simple_spinner_item, countryList);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        return adapter;
        //  spinner_country.setAdapter(adapter);
    }
    public static ArrayList<NameValuePairs> setPairs(JSONArray pairs, Context context) {
        ArrayList<NameValuePairs> pairList = new ArrayList<>();
        //Add countries
        try {
            for (int i = 0; i < pairs.length(); i++) {
                JSONArray jsonArray = pairs.getJSONArray(i);
                //	System.out.println(jsonArray);
                pairList.add(new NameValuePairs(i,jsonArray.getInt(0),jsonArray.getString(1)));
                //   countryList.add(new Data_vtypes("1", "India"));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pairList;
    }

/*    public static ArrayList<DataBoats> setTripBoats(JSONArray pairs, Context context) {
        ArrayList<DataBoats> pairList = new ArrayList<>();
        try {
            for (int i = 0; i < pairs.length(); i++) {
                JSONArray jsonArray = pairs.getJSONArray(i);
                pairList.add(new DataBoats(jsonArray.getInt(0),jsonArray.getString(1)));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pairList;
    }
    */
    public static List<DataMyShare> drawSharedUserList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataMyShare> array = new ArrayList<DataMyShare>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, owner,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            owner = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataMyShare data = new DataMyShare();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setValue(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }

    public static List<DataLogs> drawTripList(JSONObject jObj,Context context){
        JSONArray alldata;
        List<DataLogs> array = new ArrayList<DataLogs>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataLogs data = new DataLogs();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {	//boat name
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==1) {	//time in hh:mm
                        data.setTime(ss);
                        //name.add(ss);
                    }
                    if (j==2) {	//status in/out
                        data.setStatus(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {	//fence name
                        data.setDesc(ss);
                        //imageName.add(ss);
                    }
                    if(j==4){data.setIsHeader(Integer.parseInt(ss));}
                }
                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
    public static List<DataLogs> drawAlarmList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataLogs> array = new ArrayList<DataLogs>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataLogs data = new DataLogs();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0){
                        data.setName(ss);
                    }
                    if (j==1) {	//time
                        data.setTime(ss);
                    }
                    if (j==2) {	//alarm
                        data.setDesc(ss);
                    }
                    if(j==3){
                        data.setIsHeader(Integer.parseInt(ss));
                    }

                }
                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }

    public static List<DataPax> drawPaxList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataPax> array = new ArrayList<DataPax>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataPax data = new DataPax();
                JSONArray jsonArray = alldata.getJSONArray(i);
                data.setPaxId(jsonArray.getInt(0));
                data.setTripId(jsonArray.getInt(1));
                data.setName(jsonArray.getString(2));
                data.setFrom(jsonArray.getString(3));
                data.setTO(jsonArray.getString(4));
                data.setContact(jsonArray.getString(5));
                data.setBookingStatusInt(jsonArray.getInt(6));
                data.setbookingStatusStr(jsonArray.getString(7));
                data.setImage(jsonArray.getString(8));
                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
    public static List<DataLegInfo> drawLegInfo(JSONObject jObj, Context context){
        JSONArray paxData;
        List<DataLegInfo> array = new ArrayList<DataLegInfo>();
        try {
            paxData = jObj.getJSONArray("paxData");

            for (int i = 0; i < paxData.length(); i++) {
                DataLegInfo data = new DataLegInfo();
                JSONArray jsonArray = paxData.getJSONArray(i);
                data.setIslandFrom(jsonArray.getString(1));
                data.setIslandTo(jsonArray.getString(2));
                data.setPaxQty(jsonArray.getInt(3));
                data.setLegName(jsonArray.getString(1)+" > "+jsonArray.getString(2));
                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
    public static List<DataGuardLogs> drawGuardList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataGuardLogs> array = new ArrayList<DataGuardLogs>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataGuardLogs data = new DataGuardLogs();
                JSONArray jsonArray = alldata.getJSONArray(i);
                int isHeader = jsonArray.getInt(0);
                data.setIsHeader(isHeader);
                if(isHeader==1){    //a header title
                    data.setTitle(jsonArray.getString(1));
                } else {
                    data.setTimeIn(jsonArray.getString(1));
                    data.setStatus(jsonArray.getString(2));
                    data.setLocationName(jsonArray.getString(3));
                }

                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }

    public static List<DataSmartLogs> drawSmartLogs(JSONObject jObj){
        JSONArray alldata;
        List<DataSmartLogs> array = new ArrayList<DataSmartLogs>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
            for (int i = 0; i < alldata.length(); i++) {
                DataSmartLogs data = new DataSmartLogs();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    data.setS(j, jsonArray.get(j).toString());
                }
                /*
                data.setS0(jsonArray.get(0).toString());
                data.setS1(jsonArray.get(1).toString());
                data.setS2(jsonArray.get(2).toString());
                data.setS3(jsonArray.get(3).toString());
                data.setS4(jsonArray.get(4).toString());
                data.setS5(jsonArray.get(5).toString());
                data.setS6(jsonArray.get(6).toString());
                data.setS7(jsonArray.get(7).toString());
                data.setS8(jsonArray.get(8).toString());
                data.setS9(jsonArray.get(9).toString());
                data.setS10(jsonArray.get(10).toString());
                */
                array.add(data);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }

    public static List<DataGuardLogs> drawGuardSmartList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataGuardLogs> array = new ArrayList<DataGuardLogs>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataGuardLogs data = new DataGuardLogs();
                JSONArray jsonArray = alldata.getJSONArray(i);
                int isHeader = jsonArray.getInt(0);
                data.setIsHeader(isHeader);
                if(isHeader==1){    //a header title
                    data.setTitle(jsonArray.getString(1));
                } else {
                    data.setTimeIn(jsonArray.getString(1));
                    data.setStatus(jsonArray.getString(2));
                    data.setLocationName(jsonArray.getString(3));
                }

                array.add(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }
    public static List<DataMy> drawFleetDeviceList(JSONObject jObj,Context context){
//	public static List<Data> drawFleetDeviceList(JSONObject jObj,Context context){
        JSONArray alldata;
        List<DataMy> array = new ArrayList<DataMy>();
        try {
//			System.out.print(jObj);
//			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, owner,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            owner = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataMy data = new DataMy();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setValue(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
//						data.setImage(ss);
                        //imageName.add(ss);
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public static void saveMyTrips(JSONObject jObj,Context context){
        JSONArray alldata;
        //String now_date = null;
     //   int accountType = SettingsPreferences.getSelectedMyAccount(context);
        try {
            alldata = jObj.getJSONArray("aaData");
//			if(alldata.length()>0){
//				now_date=jObj.getString("now");
//			}
            //	SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, owner,imageName,vDate,tstatus,speed,island,marker,notice;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            owner = new ArrayList<String>();
            vDate = new ArrayList<String>();
            tstatus  = new ArrayList<String>();
            speed  = new ArrayList<String>();
            island  = new ArrayList<String>();
            marker = new ArrayList<String>();
            notice = new ArrayList<String>();
            String my_imei=SettingsPreferences.getIMEI(context);
//			int acc = SettingsPreferences.getSelectedAccount(context);
            String imei_found="";

            for (int i = 0; i < alldata.length(); i++) {

                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        id.add(ss);
                    }
                    if (j==1) {
                        name.add(ss);
                    }
                    if (j==2) {
                        owner.add(ss);
                    }
                    if (j==3) {
                        imageName.add(ss);
                    }
                    if (j==4) {
                        vDate.add(ss);
                    }
                    if(j==5){
                        tstatus.add(ss);
                    }
                    if(j==6){
                        speed.add(ss);
                    }
                    if(j==7){
                        island.add(ss);
                    }
                    if(j==8){
                        marker.add(ss);
                    }
                    if(j==9){
                        notice.add(ss);
                    }
                    //if (j==4){
                    //	if(ss.equalsIgnoreCase(my_imei)){
                    //		imei_found="ok";
                    //	}
                    //}

                }
            }
            Database db = new Database(context);
            db.deleteTrip();
            for (int i = 0; i < alldata.length(); i++) {
//				System.out.println(imageName.get(i)+".........NAME");
                String  imageUrl=AppConstants.IMAGE_SMALL_URL+imageName.get(i);
                //imageLoader.DisplayImage(imageUrl,new ImageView(this), new ProgressBar(this));
                //					imageLoader.DisplayImage(MainActivity.files[i],new ImageView(con));
                //db.add(id.get(i), name.get(i), owner.get(i),String.valueOf(fileCache.getFile(imageUrl)));
                //	String s="";
                //	if(accountType!=2){
                //		v = AppUtils.getTimeAgo(vDate.get(i),now_date);
                //	}
                db.addTrip(id.get(i),
                        name.get(i),
                        owner.get(i),
                        imageUrl,
                        vDate.get(i),
                        tstatus.get(i),
                        speed.get(i),
                        island.get(i),
                        marker.get(i),
                        notice.get(i));
            }

            //	SettingsPreferences.setTrackMe(context, imei_found);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public static List<DataPublicHire> drawPublicBoatHireList1(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataPublicHire> array = new ArrayList<DataPublicHire>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, description,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            description = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataPublicHire data = new DataPublicHire();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setDescription(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }
                    if(j==4){
                        data.setBids(ss);

                    }
                    if(j==5){
                        data.setHireStatus(Integer.parseInt(ss));
                    }
                    if(j==6){
                        data.setStatusText(ss);
                    }
                    if(j==7){
                        data.setHireDate(ss);
                    }
                    if(j==8){
                        data.setBidStatusText(ss);
                    }
                    if(j==9){
                        data.setBidStatus(Integer.parseInt(ss));
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataTripPlans> drawTripPlanList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataTripPlans> array = new ArrayList<DataTripPlans>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
            for (int i = 0; i < alldata.length(); i++) {
                DataTripPlans data = new DataTripPlans();
                JSONArray jsonArray = alldata.getJSONArray(i);
                data.setTripId(jsonArray.getInt(0));
                data.setName(jsonArray.getString(1));
                data.setRoute(jsonArray.getString(2));
                data.setImage(AppConstants.IMAGE_SMALL_URL+jsonArray.getString(3));
                data.setTripDate(jsonArray.getString(4));
                data.setTripStatusText(jsonArray.getString(5));
                data.setDeviceId(jsonArray.getString(6));
                data.setTripAccess(jsonArray.getInt(7));
                data.setCaptain(jsonArray.getInt(8));
                data.setPaxTotalCount(jsonArray.getInt(9));
                data.setPaxBookedCount(jsonArray.getInt(10));
                data.setPaxConfirmedCount(jsonArray.getInt(11));
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataRouteInfo> drawTemplateList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataRouteInfo> array = new ArrayList<DataRouteInfo>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
            for (int i = 0; i < alldata.length(); i++) {
                JSONArray jsonArray = alldata.getJSONArray(i);
                DataRouteInfo data = new DataRouteInfo(jsonArray.getInt(0),jsonArray.getString(1));
                data.setRouteState(jsonArray.getString(2));
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataPublicHire> drawPublicBoatHireList2(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataPublicHire> array = new ArrayList<DataPublicHire>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, description,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            description = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataPublicHire data = new DataPublicHire();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setDescription(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }
                    if(j==4){
                        data.setBids(ss);
                    }
                    if(j==5){
                        data.setHireStatus(Integer.parseInt(ss));
                    }
                    if(j==6){
                        data.setStatusText(ss);
                    }
                    if(j==7){
                        data.setTimeAgo(ss);
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }

    public static List<DataBids> drawPublicBidList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataBids> array = new ArrayList<DataBids>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
/*            List<String> id, name, description,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            description = new ArrayList<String>();
*/
            for (int i = 0; i < alldata.length(); i++) {
                DataBids data = new DataBids();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                    }
                    if (j==2) {
                        data.setPrice(ss);
                    }
                    if (j==3) {
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                    }
                    if(j==4){
                        data.setContact(ss);
                    }
                    if(j==5){
                        data.setIslandSpeed(ss);
                    }
                    if(j==6){
                        data.setMarker(Integer.parseInt(ss));
                    }
                    if(j==7){
                        data.setTimeAgo(ss);
                    }
                    if(j==8){
                        data.setDeviceID(ss);
                    }
                    if(j==9){
                        data.setTenderStatus(Integer.parseInt(ss));
                    }
                    if(j==10){
                        data.setBidStatus(Integer.parseInt(ss));
                    }
                    if(j==11){
                        data.setIsExpired(Integer.parseInt(ss));
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataWallet> drawWalletList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataWallet> array = new ArrayList<>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
            for (int i = 0; i < alldata.length(); i++) {
                DataWallet data = new DataWallet();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setDate(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setDesc(ss);
                        //owner.add(ss);
                    }
                    if(j==3){
                        data.setAmount(ss);

                    }
                    if(j==4){data.setStatus(Integer.parseInt(ss));}

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataBids> drawMyBidList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataBids> array = new ArrayList<DataBids>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);

            for (int i = 0; i < alldata.length(); i++) {
                DataBids data = new DataBids();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setPrice(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }
                    if(j==4){
                        data.setContact(ss);

                    }
                    if(j==5){
                        data.setIslandSpeed(ss);
                    }
                    if(j==6){
                        data.setMarker(Integer.parseInt(ss));
                    }
                    if(j==7){
                        data.setDeviceID(ss);
                    }
                    if(j==8){
                        data.setBidStatus(Integer.parseInt(ss));
                    }
                    if(j==9){
                        data.setBidStatusText(ss);
                    }
                    if(j==10){
                        data.setIsExpired(Integer.parseInt(ss));
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataMyHire> drawMyBoatHireList(JSONObject jObj, Context context){
        JSONArray alldata;
        List<DataMyHire> array = new ArrayList<DataMyHire>();
        try {
            alldata = jObj.getJSONArray(AppConstants.TAG_ALL_DATA);
//			System.out.println(alldata);
            //SplashScreen.isRegistered = jObj.getString("reg");
            List<String> id, name, owner,imageName;
            imageName= new ArrayList<String>();
            id = new ArrayList<String>();
            name = new ArrayList<String>();
            owner = new ArrayList<String>();

            for (int i = 0; i < alldata.length(); i++) {

                DataMyHire data = new DataMyHire();
                JSONArray jsonArray = alldata.getJSONArray(i);
                for (int j = 0; j < jsonArray.length(); j++) {
                    String ss = jsonArray.getString(j);
                    if (j==0) {
                        data.setId(ss);
                        //id.add(ss);
                    }
                    if (j==1) {
                        data.setName(ss);
                        //name.add(ss);
                    }
                    if (j==2) {
                        data.setDesc1(ss);
                        //owner.add(ss);
                    }
                    if (j==3) {
                        //data.setImage(ss);
                        String  imageUrl=AppConstants.IMAGE_SMALL_URL+(ss);
                        data.setImage(imageUrl);
                        //imageName.add(ss);
                    }
                    if(j==4){
                        data.setBids(ss);
                    }
                    if(j==5){
                        data.setStatus(Integer.parseInt(ss));
                    }
                    if(j==6){
                        data.setStatusText(ss);
                    }
                    if(j==7){
                        data.setHireDate(ss);
                    }
                    if(j==8){
                        data.setDesc2(ss);
                    }
                    if(j==9){
                        data.setTimeAgo(ss);
                    }
                    if(j==10){
                        data.setMyBidCount(Integer.parseInt(ss));
                    }
                    if(j==11){
//                        data.setMyWinCount(Integer.parseInt(ss));
                        data.setMyWinCount(Integer.parseInt(ss));
                    }
                    if(j==12){
                        data.setMyAcceptCount(Integer.parseInt(ss));
                    }

                }
                array.add(data);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static List<DataAirports> drawAirportList(JSONArray jArray, Context context){
        List<DataAirports> array = new ArrayList<DataAirports>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                DataAirports data = new DataAirports();
                JSONObject jObj = jArray.getJSONObject(i);

                data.setAirportId(jObj.getString("airportId"));
                data.setNameAirport(jObj.getString("nameAirport"));
                data.setCodeIataAirport(jObj.getString("codeIataAirport"));
                data.setLatitude(jObj.getString("latitudeAirport"));
                data.setLongitude(jObj.getString("longitudeAirport"));
                data.setTimezone(jObj.getString("timezone"));
                data.setCodeIataCity(jObj.getString("codeIataCity"));
                array.add(data);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
/*    public static List<DataFlightSchedule> drawFlightSchedule(JSONArray jArray, Context context){
        List<DataFlightSchedule> array = new ArrayList<DataFlightSchedule>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                DataFlightSchedule data = new DataFlightSchedule();
                JSONObject jObj = jArray.getJSONObject(i);
                JSONObject depObject = jObj.getJSONObject("departure");
                JSONObject airlineObject = jObj.getJSONObject("airline");
                JSONObject flightObject = jObj.getJSONObject("flight");

                data.setType(jObj.getString("type"));
                data.setStatus(jObj.getString("status"));
                data.setIataCode(depObject.getString("iataCode"));
                data.setScheduledTime(depObject.getString("scheduledTime"));
             //   data.setEstimatedRunway(depObject.getString("estimatedRunway"));
             //   data.setActualRunway(depObject.getString("actualRunway"));
                data.setAirlineName(airlineObject.getString("name"));
                data.setIataCode(airlineObject.getString("iataCode"));
                data.setFlightIataNumber(flightObject.getString("iataNumber"));


                array.add(data);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    */
    public static List<DataFlightSchedule> drawFlightArrival(JSONArray jArray, Context context){
        List<DataFlightSchedule> array = new ArrayList<DataFlightSchedule>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                DataFlightSchedule data = new DataFlightSchedule();
                JSONObject jObj = jArray.getJSONObject(i);
                //JSONObject depObject = jObj.getJSONObject("departure");
                JSONObject arrObject = jObj.getJSONObject("arrival");
                JSONObject airlineObject = jObj.getJSONObject("airline");
                JSONObject flightObject = jObj.getJSONObject("flight");

                data.setType(jObj.getString("type"));
                data.setStatus(jObj.getString("status"));
                data.setIataCode(arrObject.getString("iataCode"));
                //String scheduled_time = arrObject.getString("scheduledTime");
               // Date scheduled_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(scheduled_time);
                //String output = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").format(scheduled_date);
                //data.setScheduledTime(output);
                data.setScheduledTime(getDate(arrObject.getString("scheduledTime")));
                //   data.setEstimatedRunway(depObject.getString("estimatedRunway"));
                //   data.setActualRunway(depObject.getString("actualRunway"));
                data.setAirlineName(airlineObject.getString("name"));
                data.setIataCode(airlineObject.getString("iataCode"));
                data.setFlightIataNumber(flightObject.getString("iataNumber"));


                array.add(data);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }


    private static String getDate(String ourDate)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        }
        catch (Exception e){
            ourDate = "00-00-0000 00:00";
            e.printStackTrace();
        }
        return ourDate;
    }
    public static List<DataFlightSchedule> drawFlightDeparture(JSONArray jArray, Context context){
        List<DataFlightSchedule> array = new ArrayList<DataFlightSchedule>();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                DataFlightSchedule data = new DataFlightSchedule();
                JSONObject jObj = jArray.getJSONObject(i);
                JSONObject depObject = jObj.getJSONObject("departure");
                //JSONObject arrObject = jObj.getJSONObject("arrival");
                JSONObject airlineObject = jObj.getJSONObject("airline");
                JSONObject flightObject = jObj.getJSONObject("flight");

                data.setType(jObj.getString("type"));
                data.setStatus(jObj.getString("status"));
                data.setIataCode(depObject.getString("iataCode"));
//                data.setScheduledTime(depObject.getString("scheduledTime"));
                data.setScheduledTime(getDate(depObject.getString("scheduledTime")));
                //   data.setEstimatedRunway(depObject.getString("estimatedRunway"));
                //   data.setActualRunway(depObject.getString("actualRunway"));
                data.setAirlineName(airlineObject.getString("name"));
                data.setIataCode(airlineObject.getString("iataCode"));
                data.setFlightIataNumber(flightObject.getString("iataNumber"));


                array.add(data);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;

    }
    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        return email.contains("@");
    }
    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }



    public static void createNotificationChannel(Context contx) {

        // create high channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(AppConstants.CHANNEL_HIGH, AppConstants.CHANNEL_NAME_HIGH, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager1(contx).createNotificationChannel(channel);

            // create low channel
            NotificationChannel channel2 = new NotificationChannel(AppConstants.CHANNEL_LOW, AppConstants.CHANNEL_NAME_LOW, NotificationManager.IMPORTANCE_LOW);
            channel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager2(contx).createNotificationChannel(channel2);
        }
    }

    public static NotificationManager getManager1(Context contx) {
        if (mManager1 == null) {
            mManager1 = (NotificationManager) contx.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager1;
    }
    public static NotificationManager getManager2(Context contx) {
        if (mManager2 == null) {
            mManager2 = (NotificationManager) contx.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager2;
    }




}
