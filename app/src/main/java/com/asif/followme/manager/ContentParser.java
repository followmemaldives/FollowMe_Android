package com.asif.followme.manager;

import android.content.Context;
import android.graphics.Bitmap;
import com.asif.followme.BoatHire.Operator.HireMyFragment;
import com.asif.followme.BoatHire.Public.PublicBidFragment;
import com.asif.followme.PhotoActivity;
import com.asif.followme.util.AppConstants;
import com.asif.followme.util.MultipartUtility;
import com.asif.followme.util.SettingsPreferences;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Created by user on 12/18/2017.
 */

public class ContentParser {

    private Context mContext;

    public ContentParser(Context context) {
        mContext = context;

    }
    public String GetPublicVessels(Context context, int atoll,String search) {
		System.out.println("Getting Vessels from Server...");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
//        String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String fav_id = SettingsPreferences.getIMEI(context);
        int acc = SettingsPreferences.getSelectedPublicAccount(context);
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();

        System.out.println("Country in GetPublicVessels >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+country);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("acc", acc);
            postDataParams.put("atoll",atoll);
            postDataParams.put("co", country);
            postDataParams.put("fav_id", fav_id);
            postDataParams.put("tz", timezone);
            postDataParams.put("s",search);
            System.out.println("Acc:"+acc+",Atoll:"+atoll+",Country:"+country+",FAV_ID:"+fav_id+",Search:"+search);
            URL url = new URL(AppConstants.PUBLIC_VESSELS_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetPublicGroups(Context context) {
        System.out.println("Getting Public Groups from Server...");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String country = SettingsPreferences.getCountry(context);
        TimeZone tz = TimeZone.getDefault();

        try {
            postDataParams.put("co", country);
            URL url = new URL(AppConstants.PUBLIC_GROUPS_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(responseFromServer);
        return responseFromServer;
    }

    public String GetAirports(Context context, String country) {
        System.out.println("Getting Airport List from Server for Country: "+country);

        String responseFromServer = "";
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();


        try {
            URL url = new URL(AppConstants.AIRPORT_LIST_URL+country);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //responseFromServer = bufferedReader.readLine();
            String line="";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                responseFromServer+=line;
                //length += line.length();
            }




            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("KKK:"+responseFromServer);
        return responseFromServer;
    }
    public String GetFlightSchedule(Context context, String airport, String type) {
        System.out.println("Getting Flight Schedule List from Server for airport: "+airport+",Type:"+type);

        String responseFromServer = "";
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();


        try {
            String url_string;
            if(type.equals("")) {
                url_string = AppConstants.FLIGHT_SCHEDULE_URL + airport;
            } else {
                url_string = AppConstants.FLIGHT_SCHEDULE_URL + airport+"&type="+type;

            }
            URL url = new URL(url_string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //responseFromServer = bufferedReader.readLine();
            String line="";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                responseFromServer+=line;
                //length += line.length();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("KKK:"+responseFromServer);
        return responseFromServer;
    }

    public String getFlightLocation(Context context, String flight_no) {
        System.out.println("Getting Flight Location from Server for flight: "+flight_no);

        String responseFromServer = "";
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();


        try {
            String url_string;
                url_string = AppConstants.FLIGHT_LOCATION_URL + flight_no;
            URL url = new URL(url_string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //responseFromServer = bufferedReader.readLine();
            String line="";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                responseFromServer+=line;
                //length += line.length();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("KKK:"+responseFromServer);
        return responseFromServer;
    }

    public String getData(URL url,JSONObject postDataParams){
        String responseFromServer = null;
//        String sid = SettingsPreferences.getSessionID(mContext);
        int retry = 0;
        while(retry < 1) {  //retry 2 times maxium
            System.out.println("Try Attempt:"+retry+".................................................");
            try {
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                try {
                    conn.setReadTimeout(30000 /* milliseconds */);
                    conn.setConnectTimeout(30000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    // conn.setDoOutput(true);
                    //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    //conn.addRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=sfsdfsdf");
                    //conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
 //                   writer.write("");

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            retry=5;
                            //break;
                        }

                        in.close();
                        responseFromServer = sb.toString();
                    } else {
                        System.out.println("Response Code: "+responseCode);

                        //return "not ok";
                        responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Server** Please check your internet connection\"}");
                    }
                    conn.disconnect();
                } catch (SocketTimeoutException e) {
                    //e.printStackTrace();
                    retry=5;    //avoid retry if timeout occurs
                    responseFromServer = new String("{\"status\":\"error\",\"error\":\"Connection Timeout. Please check your internet connection\"}");
                } catch (IOException e) {
                    e.printStackTrace();
                    responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Server.. Please check your internet connection\"}");
                } finally {
                    conn.disconnect();
                }
            } catch (Exception e) {
                responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Server... Please check your internet connection\"}");
                e.printStackTrace();
            }
            retry++;
        }
        System.out.println("__x___________________________________________________________________________");
        System.out.println("ContentParser Response:" + responseFromServer);
        try {
            JSONObject jObj = new JSONObject(responseFromServer);
            if (jObj.has("sid")) {
                SettingsPreferences.setSessionID(mContext, jObj.getString("sid"));
            }
        } catch (JSONException e) {

        }
        return responseFromServer;
    }

    /*    public String GetMyVessels(Context context,int acc,String sender) { //to be removed
        System.out.println("Getting Vessels from Server by: "+sender);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
        //System.out.println("IMEI:"+imei+",SID:"+sid+",ACC:"+acc);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("acc", acc);
        //    postDataParams.put("sid", sid);
            postDataParams.put("imei",imei);

            URL url = new URL(AppConstants.MY_VESSELS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
*/
    public String GetMyDevices(Context context) {
        System.out.println("GetMyDevices()............");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
        //System.out.println("IMEI:"+imei+",SID:"+sid+",ACC:"+acc);

        try {
            //String id=String.valueOf(id2);
            //postDataParams.put("acc", acc);
            //    postDataParams.put("sid", sid);
            postDataParams.put("imei",imei);

            URL url = new URL(AppConstants.MY_DEVICES_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetMyGroups(Context context) {
        System.out.println("GetMyGroups()............");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        String imei= SettingsPreferences.getIMEI(context);

        try {
            postDataParams.put("imei",imei);

            URL url = new URL(AppConstants.MY_GROUPS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetPublicBoatHireList1(Context context,int state) {
        System.out.println("GetPublicBoatHireList() , Hire State:"+state);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;

        try {
            postDataParams.put("state", state);

            URL url = new URL(AppConstants.HIRE_LIST_PUBLIC_URL_1);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetPublicBoatHireList2(Context context,int state) {
        System.out.println("GetPublicBoatHireList() , Hire State:"+state);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;

        try {
            postDataParams.put("state", state);

            URL url = new URL(AppConstants.HIRE_LIST_PUBLIC_URL_2);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String getMyBidList(Context context,int hire_id) {
        System.out.println("getMyBidList(), Hire ID:"+hire_id);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String sid = SettingsPreferences.getSessionID(context);
        //String country = SettingsPreferences.getCountry(context);
        //String imei= SettingsPreferences.getIMEI(context);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.BID_LIST_MY_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String getPublicBidList(Context context,int hire_id) {
        System.out.println("getPublicBidList(), Hire ID:"+hire_id);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
        System.out.println("Hire ID:"+hire_id);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.BID_LIST_PUBLIC_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetTripPlans(Context context,int filter_index, String date_from, String date_to, int device_id) {
        System.out.println("GetTripPlans() , Index:"+filter_index+",DateFrom:"+date_from+", DateTo:"+date_to);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;

        try {
            postDataParams.put("filter_index", filter_index);
            postDataParams.put("date_from", date_from);
            postDataParams.put("date_to", date_to);
            postDataParams.put("device_id", device_id);

            URL url = new URL(AppConstants.TRIP_PLANS_MY_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String getWallet(Context context) {
        System.out.println("getWallet()");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
//        System.out.println("Hire ID:"+hire_id);

        try {
            //String id=String.valueOf(id2);
            //postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.MY_WALLET_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String getWalletInfo(Context context, int wid) {
        System.out.println("getWallet()");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("wid", wid);

            URL url = new URL(AppConstants.MY_WALLET_INFO_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String getPublicLocation(Context context, String device_id) {
        System.out.println("Getting Vessels Location from Server for device id: "+device_id);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        String acc=Integer.toString(SettingsPreferences.getSelectedPublicAccount(context));
    //    String sid=SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String operator = SettingsPreferences.getOperator(context);
       // String tz = SettingsPreferences.getTimeZone(context);


        try {
            //String id=String.valueOf(id2);
            postDataParams.put("acc", acc);
            postDataParams.put("co", country);
            postDataParams.put("id", device_id);
            postDataParams.put("op", operator);
    //        postDataParams.put("tz", tz);
    //        postDataParams.put("sid", sid);

            URL url = new URL(AppConstants.LIVE_PUBLIC_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }


    public String SavePublicPreferences(Context context,String country) {
        System.out.println("Getting Vessels from Server...");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        // String sid = SettingsPreferences.getSessionID(context);
       // String country = SettingsPreferences.getCountry(context);
        System.out.println("Country in SavePublicPreferences >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+country);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("action", "get_pref");
            postDataParams.put("co", country);

            URL url = new URL(AppConstants.PUBLIC_PREFERENCE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }


    public String GetVesselInfo(Context context, String device_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        try {
            //String id=String.valueOf(id2);
            postDataParams.put("device_id", device_id);

            URL url = new URL(AppConstants.GET_PHOTO_INFO_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }

    public String AutoLogin(Context context){
        String uname = SettingsPreferences.getUserName(context);
        String pwd = SettingsPreferences.getPassword(context);
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();
        String imei = SettingsPreferences.getIMEI(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
        String token = FirebaseInstanceId.getInstance().getToken();
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        try {
            //String id=String.valueOf(id2);
            postDataParams.put("user_name", uname);
            postDataParams.put("pass", pwd);
            postDataParams.put("token", token);
            postDataParams.put("tz", timezone);
            postDataParams.put("imei", imei);
            postDataParams.put("acc",acc);

            URL url = new URL(AppConstants.LOGIN_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }
    public String AuthenticateFM(Context context, String uname, String pwd) {
        System.out.println("AuthenticateFM("+uname+","+pwd+")");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String token = FirebaseInstanceId.getInstance().getToken();
       // String imei=SettingsPreferences.getIMEI(context);
        String imei = SettingsPreferences.getIMEI(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
        System.out.println("AuthenticateFM("+uname+","+pwd+")+IMEI:"+imei);
        System.out.println("Token:"+token);

        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("user_name", uname);
            postDataParams.put("pass", pwd);
            postDataParams.put("token", token);
            postDataParams.put("tz", timezone);
            postDataParams.put("imei", imei);
            postDataParams.put("acc",acc);

            URL url = new URL(AppConstants.LOGIN_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }




    public String InitializeHome(Context context) {
        System.out.println("InitializeHome()");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String token = FirebaseInstanceId.getInstance().getToken();
        String imei = SettingsPreferences.getIMEI(context);
        String uname = SettingsPreferences.getUserName(context);
        String pwd = SettingsPreferences.getPassword(context);
        String country = SettingsPreferences.getCountry(context);
        String operator = SettingsPreferences.getOperator(context);
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();
        System.out.println("Token:"+token);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("user_name", uname);
            postDataParams.put("pass", pwd);
            postDataParams.put("tz", timezone);
            postDataParams.put("imei", imei);
            postDataParams.put("co", country);
            postDataParams.put("op", operator);
            postDataParams.put("token", token);

            URL url = new URL(AppConstants.INIT_HOME_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }
    public String updateFavourite(Context context, String device_id, int favourite) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String fav = String.valueOf(favourite);
        String fav_id=SettingsPreferences.getIMEI(context);
        try {
            //String id=String.valueOf(id2);
            postDataParams.put("id", device_id);
            postDataParams.put("fav_id", fav_id);
            postDataParams.put("fav", fav);
//            System.out.println("Device id:"+device_id+", FAV_ID:"+fav_id+",Fav:"+fav);

            URL url = new URL(AppConstants.FAVOURITE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }

    public String setGeoAlert(Context context, String device_id, String fence_id) {
        String responseFromServer = null;
        JSONObject postDataParams = new JSONObject();
        String regId = FirebaseInstanceId.getInstance().getToken();
        System.out.println("Device iD:"+device_id+",Fence ID:"+fence_id);

        try {
            postDataParams.put("did", device_id);
            postDataParams.put("fid", fence_id);
            postDataParams.put("rid",regId);
            postDataParams.put("action","1");

            URL url = new URL(AppConstants.PUBLIC_ALERT_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("response:"+responseFromServer);
        return responseFromServer;
    }


    public String getMyLocation(Context context, String device_id, String neighbour) {
        System.out.println("Getting Vessels Location from Server for device id: "+device_id);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        String acc = "0";
        //String imei= AppUtils.getImeiNumber(context);
        String nav=SettingsPreferences.getMyNavigationName(context);
        if(nav.equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
      //  String sid=SettingsPreferences.getSessionID(context);
     //   String country = SettingsPreferences.getCountry(context);
        String operator = SettingsPreferences.getOperator(context);
        // String tz = SettingsPreferences.getTimeZone(context);


        try {
            //String id=String.valueOf(id2);
            postDataParams.put("acc", acc);
        //    postDataParams.put("co", country);
            postDataParams.put("id", device_id);
            postDataParams.put("op", operator);
            postDataParams.put("nb", neighbour);
        //    postDataParams.put("sid", sid);

            URL url = new URL(AppConstants.LIVE_MY_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String getDeviceEditInfo(Context context, String device_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        //String acc=Integer.toString(SettingsPreferences.getSelectedMyAccount(context));
//        String sid=SettingsPreferences.getSessionID(context);
        //   String country = SettingsPreferences.getCountry(context);
        //String operator = SettingsPreferences.getOperator(context);
        // String tz = SettingsPreferences.getTimeZone(context);
        System.out.println("Device ID:"+device_id);

        try {
            //String id=String.valueOf(id2);
            //postDataParams.put("acc", acc);
            //    postDataParams.put("co", country);
            postDataParams.put("device_id", device_id);
            //postDataParams.put("op", operator);
            //        postDataParams.put("tz", tz);
            //postDataParams.put("sid", sid);

            URL url = new URL(AppConstants.GET_DEVICE_INFO_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String userProfileInfo(Context context, String action, String name, String email, String phone, String pass, String co) {
        System.out.println("userProfileInfo(), Action:"+action);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        //String acc=Integer.toString(SettingsPreferences.getSelectedMyAccount(context));
//        String sid=SettingsPreferences.getSessionID(context);
        //   String country = SettingsPreferences.getCountry(context);
        //String operator = SettingsPreferences.getOperator(context);
        // String tz = SettingsPreferences.getTimeZone(context);

        try {
            //String id=String.valueOf(id2);
            //postDataParams.put("acc", acc);
            //    postDataParams.put("co", country);
//            postDataParams.put("device_id", device_id);
            //postDataParams.put("op", operator);
            //        postDataParams.put("tz", tz);
            postDataParams.put("action", action);
            postDataParams.put("name", name);
            postDataParams.put("email", email);
            postDataParams.put("phone", phone);
            postDataParams.put("pass", pass);
            postDataParams.put("co",co);    //country

            URL url = new URL(AppConstants.SAVE_USER_PROFILE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String saveDeviceInfo(Context context, String device_id, int v_type, String v_imei,String v_name,String v_public,String contact,String fuel, String charter) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("saveDeviceInfo(), Charter:"+charter);

        //String acc=Integer.toString(SettingsPreferences.getSelectedMyAccount(context));
        //String sid=SettingsPreferences.getSessionID(context);
        //   String country = SettingsPreferences.getCountry(context);
        //String operator = SettingsPreferences.getOperator(context);
        // String tz = SettingsPreferences.getTimeZone(context);


        try {
            postDataParams.put("device_id", device_id);
            postDataParams.put("imie", v_imei);
            postDataParams.put("name", v_name);
            postDataParams.put("public", v_public);
            postDataParams.put("contact", contact);
            postDataParams.put("fuel", fuel);
            postDataParams.put("v_type", v_type);
            postDataParams.put("charter", charter);


            URL url = new URL(AppConstants.SAVE_DEVICE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getDeviceTypes(int type, Context context) {   //0-all,1-vehicle,2-boat
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("type", type);

            URL url = new URL(AppConstants.GET_DEVICE_TYPE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String getNameValuePairs(Context context, String pair) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("pair", pair);

            URL url = new URL(AppConstants.NAME_VALUE_PAIRS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String getSpinnerOptions(Context context, String pair) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("type", pair);

            URL url = new URL(AppConstants.SPINNER_OPTIONS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String getSharedUsers(Context context, int device_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("device_id", device_id);

            URL url = new URL(AppConstants.GET_SHARED_LIST_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getPaxList(Context context, int trip_id, int bfrom, int bto, int bstatus) {
        System.out.println("getPaxList(), Trip ID:"+trip_id+",Bfrom:"+bfrom+",Bto:"+bto+",BStatus:"+bstatus);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("trip_id", trip_id);
            postDataParams.put("bfrom", bfrom);
            postDataParams.put("bto", bto);
            postDataParams.put("bstatus", bstatus);

            URL url = new URL(AppConstants.GET_PAX_LIST_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getTemplateList(Context context, int client_id) {
        System.out.println("getTemplateList(), Client ID:"+client_id);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;

        try {
            postDataParams.put("client_id", client_id);

            URL url = new URL(AppConstants.GET_TEMPLATE_LIST_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String verifyUser(Context context, String action, String name, String contact) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("name", name);
            postDataParams.put("contact", contact);

            URL url = new URL(AppConstants.VERIFY_USER_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getTrace(Context context, String device_id, String mdate) {
        System.out.println("getTrace():Device id:"+device_id+",Date:"+mdate);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
//        int acc = SettingsPreferences.getMyNavigationIndex(context);
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("id", device_id);
            postDataParams.put("date", mdate);
            postDataParams.put("acc", acc);

            URL url = new URL(AppConstants.TRACE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String RemoveSharedUsers(Context context, String device_id,String shared_user) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("device_id", device_id);
            postDataParams.put("shared_user", shared_user);
            postDataParams.put("action", "remove");

            URL url = new URL(AppConstants.POST_SHARED_USERS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }
    public String readSharedUser(Context context, String device_id, String shared_user) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("device_id", device_id);
            postDataParams.put("shared_user", shared_user);
            postDataParams.put("action", "read");

            URL url = new URL(AppConstants.POST_SHARED_USERS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }
    public String postSharedUser(Context context, String email, String device_id, int access, int trip_access, String shared_user) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("email", email);
            postDataParams.put("device_id", device_id);
            postDataParams.put("shared_user", shared_user);
            postDataParams.put("action", "add");
            postDataParams.put("access", access);
            postDataParams.put("trip", trip_access);

            URL url = new URL(AppConstants.POST_SHARED_USERS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String addPassengers(Context context, String action, int pax_id, int trip_id, String pax, int b_from, int b_to,String contact, int b_status) {
        System.out.println("addPassengers(), Action:"+action+",Pax ID:"+pax_id+",Pax:"+pax+",Trip:"+trip_id+",From:"+b_from+",To:"+b_to+",Contct:"+contact+",BStatus:"+b_status);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("pax", pax);
            postDataParams.put("pax_id", pax_id);
            postDataParams.put("trip_id", trip_id);
            postDataParams.put("contact", contact);
            postDataParams.put("bfrom", b_from);
            postDataParams.put("bto", b_to);
            postDataParams.put("bstatus",b_status);
         //   postDataParams.put("action", "add");
         //   postDataParams.put("access", access);

            URL url = new URL(AppConstants.POST_PAX_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getPaxInfo(Context context, int trip_id, int pax_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("trip_id", trip_id);
            postDataParams.put("pax_id", pax_id);
            //   postDataParams.put("action", "add");
            //   postDataParams.put("access", access);

            URL url = new URL(AppConstants.GET_PAX_INFO_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getPaxDestinations(Context context, int trip_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("trip_id", trip_id);


            URL url = new URL(AppConstants.GET_PAX_DEST_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String SaveNewDevice(Context context, String device_id,String action,String v_brand,String v_imei,String v_name,String v_public,String contact,String v_sim,String fuel,String lat,String lon, String charter) {

        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("Action:"+action+", Device ID:"+device_id);
        String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("device_id", device_id);
            postDataParams.put("action", action);
            postDataParams.put("brand", v_brand);
            postDataParams.put("imei", v_imei);
            postDataParams.put("name", v_name);
            postDataParams.put("public", v_public);
            postDataParams.put("contact", contact);
            postDataParams.put("sim", v_sim);
            postDataParams.put("fuel", fuel);
            postDataParams.put("lat", lat);
            postDataParams.put("lon", lon);
            postDataParams.put("co", country);
            postDataParams.put("charter", charter);

            URL url = new URL(AppConstants.SAVE_DEVICE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getDeviceInfo(Context context,String device_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);

        try {
            postDataParams.put("id", device_id);

            URL url = new URL(AppConstants.DEVICE_INFO_URL);
            responseFromServer = postData(url,postDataParams);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String getTripLogs(Context context, int filter_index,String date_from,String date_to,int sort_index) {
        System.out.println("getTripLogs()");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String id = SettingsPreferences.getSelectedItemID(context);
        //String device_id = SettingsPreferences.getSelectedDevice(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
//        int acc = SettingsPreferences.getMyNavigationIndex(context);
//        System.out.println("ContentParser getTripLogs Date:"+mDate+",Device Id:"+device_id+",ACC:"+acc);

        try {
            postDataParams.put("id", id);   //device id or group id
            postDataParams.put("filter_index",filter_index);
            postDataParams.put("date_from",date_from);
            postDataParams.put("date_to",date_to);
            postDataParams.put("sort_index",sort_index);
//            postDataParams.put("date", mDate);
            postDataParams.put("acc", acc);

            URL url = new URL(AppConstants.TRIP_LOG_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }
    public String getGuardLogs(Context context, String date) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String id = SettingsPreferences.getSelectedItemID(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
        System.out.println("getTripLogs(), ACC:"+acc+",Date:"+date);
//        int acc = SettingsPreferences.getMyNavigationIndex(context);
//        System.out.println("ContentParser getTripLogs Date:"+mDate+",Device Id:"+device_id+",ACC:"+acc);

        try {
            postDataParams.put("id", id);   //device id or group id
            postDataParams.put("date",date);
            postDataParams.put("acc", acc);

            URL url = new URL(AppConstants.GUARD_LOG_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }
    public String getSmartGuardLogs(Context context, String date) {
        System.out.println("getTripLogs()");
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String id = SettingsPreferences.getSelectedItemID(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }
//        int acc = SettingsPreferences.getMyNavigationIndex(context);
//        System.out.println("ContentParser getTripLogs Date:"+mDate+",Device Id:"+device_id+",ACC:"+acc);

        try {
            postDataParams.put("id", id);   //device id or group id
            postDataParams.put("date",date);
            postDataParams.put("acc", acc);

            URL url = new URL(AppConstants.GUARD_SMART_LOG_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }
    public String getAlarmLogs(Context context, int filter_index,String date_from,String date_to,int sort_index) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String id = SettingsPreferences.getSelectedItemID(context);
        String acc = "0";
        if(SettingsPreferences.getMyNavigationName(context).equalsIgnoreCase("nav_my_groups")){
            acc = "1";
        }

        try {
            postDataParams.put("id", id);   //device id or group id
            postDataParams.put("filter_index",filter_index);
            postDataParams.put("date_from",date_from);
            postDataParams.put("date_to",date_to);
            postDataParams.put("sort_index",sort_index);
            postDataParams.put("acc", acc);

            URL url = new URL(AppConstants.ALARM_LOG_URL);
            responseFromServer = postData(url,postDataParams);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;

    }

    public String getGroupDevices(Context context, int group_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("getGroupDevices Fleet id:"+group_id);

        try {
            postDataParams.put("fleet_id", group_id);

            URL url = new URL(AppConstants.MY_FLEET_DEVICES_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String changeFleet(Context context, String action, int fleet_id, int device_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("Fleet id:"+fleet_id+", Device ID:"+device_id+",Action:"+action);

        try {
            postDataParams.put("fleet_id", fleet_id);
            postDataParams.put("device_id", device_id);
            postDataParams.put("action", action);

            URL url = new URL(AppConstants.MY_FLEET_DEVICES_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String changeGroup(Context context, String action, String group_name, int fleet_id) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
//        int acc = SettingsPreferences.getSelectedMyAccount(context);
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("Fleet id:"+fleet_id+", Group Name:"+group_name+",Action:"+action+"...................................");

        try {
            postDataParams.put("fleet_id", fleet_id);
            postDataParams.put("name", group_name);
            postDataParams.put("action", action);


            URL url = new URL(AppConstants.POST_NEW_FLEET_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String saveFireID(Context context, String token) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        String serial = SettingsPreferences.getIMEI(context);
        System.out.println("saveFireID On Server Token: "+token);
        System.out.println("******************************************************************************************************");

        try {
            postDataParams.put("imei", serial);
            postDataParams.put("token", token);

            URL url = new URL(AppConstants.GCM_REGISTER_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String SavePreferences(Context context,int move,int minor,int major, int hire ) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        String token = FirebaseInstanceId.getInstance().getToken();
        String serial = SettingsPreferences.getIMEI(context);
        String country = SettingsPreferences.getCountry(context);
        System.out.println("saveFireID Token: "+token);
//        System.out.println("COUNTY:"+country);
        System.out.println("SavePreference() , Move:"+move+",Minor:"+minor+",Major:"+major+",Hire:"+hire);

        try {
//            postDataParams.put("imei", serial);
            postDataParams.put("token",token);
            postDataParams.put("move", move);
            postDataParams.put("minor", minor);
            postDataParams.put("major", major);
            postDataParams.put("hire", hire);
            postDataParams.put("co",country);

            URL url = new URL(AppConstants.SETTING_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getNotice(Context context, String device_id,String action, String notice) {
        System.out.println("getNotice() Device id: "+device_id+", Action:"+action+",Notice:"+notice);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        try {
            postDataParams.put("id", device_id);
            postDataParams.put("action", action);
            postDataParams.put("notice", notice);

            URL url = new URL(AppConstants.NOTICE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getETA(Context context, String action, String device_id,int fence_id) {
        System.out.println("getETA() Action: "+action+", Device id: "+device_id+", Fence ID:"+fence_id);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        try {
            postDataParams.put("action", action);
            postDataParams.put("id", device_id);
            postDataParams.put("fence_id", fence_id);

            URL url = new URL(AppConstants.ETA_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getETAPublic(Context context, String action, String device_id,int fence_id) {
        System.out.println("getETAPublic() Action: "+action+", Device id: "+device_id+", Fence ID:"+fence_id);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        try {
            postDataParams.put("action", action);
            postDataParams.put("id", device_id);
            postDataParams.put("fence_id", fence_id);

            URL url = new URL(AppConstants.ETA_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String registerUser(Context context, String name, String contact, String email, String pass, String country) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String imei = SettingsPreferences.getIMEI(context);
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();
        System.out.println("registerUser(), imei:"+imei+",TZ:"+timezone);
        try {
            postDataParams.put("name", name);
            postDataParams.put("contact", contact);
            postDataParams.put("email", email);
            postDataParams.put("pass", pass);
            postDataParams.put("country", country);
            postDataParams.put("imei", imei);
            postDataParams.put("tz", timezone);

            URL url = new URL(AppConstants.USER_REGISTER_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String forgotPassword(Context context, String email, String code, String action, String pass) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String imei = SettingsPreferences.getIMEI(context);
        System.out.println("Action:"+action+", Email:"+email+", Code:"+code);
        try {
            postDataParams.put("email", email);
            postDataParams.put("code", code);
            postDataParams.put("action", action);
            postDataParams.put("pass", pass);

            URL url = new URL(AppConstants.FORGOT_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String sendFeedback(Context context, String name, String contact, String email, String feedback) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String country = SettingsPreferences.getCountry(context);
//        String sid = SettingsPreferences.getSessionID(context);
        try {
            postDataParams.put("name", name);
            postDataParams.put("contact", contact);
            postDataParams.put("email", email);
            postDataParams.put("country", country);
//            postDataParams.put("sid", sid);
            postDataParams.put("feedback", feedback);


            URL url = new URL(AppConstants.FEEDBACK_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String GetMyTrips(Context context,int acc) {
        System.out.println("GetMyTrips() at ContentParser");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
//        String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
//        System.out.println("IMEI:"+imei+",SID:"+sid+",ACC:"+acc);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("acc", acc);
//            postDataParams.put("sid", sid);
//            postDataParams.put("imei",imei);

            URL url = new URL(AppConstants.MY_TRIP_PLANS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String GetMyBoatHires(Context context,int state) {
        System.out.println("GeMyBoatHires() at ContentParser, State:"+state);
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
//        String sid = SettingsPreferences.getSessionID(context);
        String country = SettingsPreferences.getCountry(context);
        String imei= SettingsPreferences.getIMEI(context);
//        System.out.println("IMEI:"+imei+",SID:"+sid+",ACC:"+acc);

        try {
            //String id=String.valueOf(id2);
            postDataParams.put("state", state);
//            postDataParams.put("sid", sid);
//            postDataParams.put("imei",imei);

            URL url = new URL(AppConstants.HIRE_LIST_MY_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String saveTripPlans(Context context, String action, int trip_id, String date,int boat_id,String destinations,String remarks,int status) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("saveTripPlans(), Action:"+action+", Trip ID:"+trip_id+", Date:"+date+",Boat:"+boat_id+",Destination:"+destinations+",Remarks:"+remarks+",Status:"+status);
        String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("boat_id", boat_id);
            postDataParams.put("trip_id", trip_id);
            postDataParams.put("date", date);
            postDataParams.put("dest", destinations);
            postDataParams.put("remarks", remarks);
            postDataParams.put("status", status);

            postDataParams.put("co", country);

            URL url = new URL(AppConstants.POST_TRIP_PLANS_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }
    public String saveRoutePlans(Context context, String action, int route_id, int client_id, String destinations, int status) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("saveTripPlans(), Action:"+action+", Route ID:"+route_id+",Status:"+status+",Client:"+client_id+",Dest:"+destinations);
        String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("route_id", route_id);
            postDataParams.put("dest", destinations);
            postDataParams.put("status", status);
            postDataParams.put("client_id", client_id); //the client id which the route to be saved

            URL url = new URL(AppConstants.POST_ROUTE_PLANS_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }

    public String getTripInfo(Context context, int trip_id, String action) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("getTripInfo(), Action:"+action+", TRIP ID:"+trip_id);
        //String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("trip_id", trip_id);

            URL url = new URL(AppConstants.GET_TRIP_INFO_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getTripFilter(Context context) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;

        try {
//            postDataParams.put("action", action);
            URL url = new URL(AppConstants.GET_TRIP_FILTER_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String saveBoatHire(Context context, String action, int hire_id, String date,int boat_type, String destinations,String remarks,int status, JSONObject boatTypes, int hire_type, String hire_duration, int duration_index) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("saveBoatHire() , Action:"+action+", Hire ID:"+hire_id+", Date:"+date+",Boat Type:"+boat_type+", Status: "+status+",Destinations:"+destinations+", boatTypes:"+boatTypes+",Hire Type:"+hire_type+",Duration:"+hire_duration+",Duration Index:"+duration_index);
        TimeZone tz = TimeZone.getDefault();
        String timezone = tz.getID();

        String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("boat_type", boat_type);
            postDataParams.put("hire_id", hire_id);
            postDataParams.put("date", date);
            postDataParams.put("destinations", destinations);
            postDataParams.put("remarks", remarks);
            postDataParams.put("hire_status", status);
            postDataParams.put("timezome",timezone);
            postDataParams.put("boat_types", boatTypes.toString());
            postDataParams.put("hire_type",hire_type);
            postDataParams.put("duration_text",hire_duration);
            postDataParams.put("duration_index",duration_index);

            postDataParams.put("co", country);

            URL url = new URL(AppConstants.POST_BOAT_HIRE_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }

    public String getBoatHireInfo(Context context, int hire_id, String action) {
        System.out.println("getBoatHire(), Hire id:"+hire_id+", Action:"+action);
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("Action:"+action+", Hire ID:"+hire_id);
        //String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.POST_BOAT_HIRE_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }

    public String operatorBidActions(Context context, String action, int bid_id, double amount, int boat) {  //For operator bidding form
        String hire_id = HireMyFragment.selected_item_id;
        System.out.println("operatorBidActions() , Hire ID: "+hire_id+", Bid ID:"+bid_id+", Action:"+action+", Amount:"+amount+", Boat:"+boat);

        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("bid_id", bid_id);
            postDataParams.put("amount", amount);
            postDataParams.put("device_id", boat);
            postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.POST_BOAT_BID_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String rateBidActions(Context context, String action, int bid_id, int rate, String remarks) {  //For operator bidding form
        String hire_id = PublicBidFragment.selected_item_id;
        System.out.println("rateBidActions() , Hire ID: "+hire_id+", Bid ID:"+bid_id+", Action:"+action+", Rate:"+rate+", Remarks:"+remarks);

        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        //String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("bid_id", bid_id);
            postDataParams.put("rate", rate);
            postDataParams.put("remarks", remarks);
            postDataParams.put("hire_id", hire_id);

            URL url = new URL(AppConstants.POST_BOAT_BID_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String reloadActions(Context context, String action, int pkg, int device_id) {  //For operator bidding form
        String hire_id = HireMyFragment.selected_item_id;
        String key = SettingsPreferences.getSessionKey(context);

        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        System.out.println("operatorBidActions() , Action:"+action+", Package:"+pkg+", Boat:"+device_id+", Key:"+key);
        //String imei= AppUtils.getImeiNumber(context);
        //String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("pkg", pkg);
            postDataParams.put("device_id", device_id);
            postDataParams.put("key",key);

            URL url = new URL(AppConstants.RELOAD_ACTIONS_URL);
            responseFromServer = postData(url,postDataParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFromServer;
    }
    public String getAJAX(Context context, String action, String id, String id2, String param3) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        //String imei= AppUtils.getImeiNumber(context);
        System.out.println("getAJAX() , Action:"+action+", ID:"+id+", ID2:"+id2);

        String country = SettingsPreferences.getCountry(context);

        try {
            postDataParams.put("action", action);
            postDataParams.put("id", id);
            postDataParams.put("id2", id2);
            postDataParams.put("param3",param3);

            URL url = new URL(AppConstants.GET_AJAX_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }
    public String Logout(Context context) {
        System.out.println("Logout() Logging out from Server...");
        JSONObject postDataParams = new JSONObject();

        String responseFromServer = null;
        String regId = FirebaseInstanceId.getInstance().getToken();
//        String sid = SettingsPreferences.getSessionID(context);
//        String imei= SettingsPreferences.getIMEI(context);
        System.out.println("regid:"+regId);

        try {
            postDataParams.put("regid", regId);
            URL url = new URL(AppConstants.LOGOUT_URL);
            responseFromServer = postData(url,postDataParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(responseFromServer);
        return responseFromServer;
    }
    public String ProfileSave(Context context,Bitmap bitmap) {
        JSONObject postDataParams = new JSONObject();
        String responseFromServer = null;
        String device_id=SettingsPreferences.getSelectedItemID(context);
        HttpURLConnection conn;
        try {
            URL url = new URL(AppConstants.PROFILE_URL);
            MultipartUtility multipart = new MultipartUtility(AppConstants.PROFILE_URL, "UTF-8","********");
            multipart.addFormField("device_id", device_id);
            System.out.println("ProfileSave(), Device ID:"+device_id);
            if(bitmap!=null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                multipart.addFilePart("image", bs,device_id+".png");//ima
            } else {
                if(PhotoActivity.RemoveImage == true){
                    multipart.addFormField("action", "rem");
                } else {
                    System.out.println("No change to Image......................");
                }
            }

            conn = multipart.execute();
            responseFromServer = getResponse(conn);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return responseFromServer;
    }

    private String getResponse(HttpURLConnection conn){
        try {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte []        data = new byte[1024];
            int             len = dis.read(data, 0, 1024);

            dis.close();
            int responseCode = conn.getResponseCode();

            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        } catch(Exception e){
            System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            //Log.e(TAG, "GeoPictureUploader: biffed it getting HTTPResponse");
            return "";
        }
    }


/*
    public String getTrace(Context context, String device_id,String mDate) {
        String responseFromServer = null;
        //System.out.println("DATE___________________________________________:"+mDate);
        try {

            //HttpClient httpclient = SettingsPreferences.getHttpclient();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", device_id));
            nameValuePairs.add(new BasicNameValuePair("date", mDate));
            HttpPost httppost = new HttpPost(AppConstants.TRACE_URL);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpclient.execute(httppost);
            HttpResponse response = SettingsPreferences.getHttpclient().execute(httppost);	//tried to solve session issue

//			responseFromServer = EntityUtils.toString(response.getEntity());
            HttpEntity entity = response.getEntity();
            responseFromServer = EntityUtils.toString(entity);
            entity.consumeContent();

        } catch (ConnectTimeoutException e){
            responseFromServer="{\"status\":\"error\",\"error\":\"It is taking too long. Please try again\"}";
//			AppUtils.showAlertDialog(context, "Timeout", "It took longer than expected. Please try again!");
//			return "error";
//			System.out.println("Connection Timed out....");
        } catch (SocketTimeoutException e){
            responseFromServer="{\"status\":\"error\",\"error\":\"It is taking too long. Please try again\"}";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFromServer;
    }

*/
public String postImageData(URL url, JSONObject postDataParams){
    String responseFromServer = null;
    int retry = 0;
    while(retry < 2) {
        System.out.println("Try Attempt:"+retry+".................................................");
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=sfsdfsdf");
                //conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        retry=5;
                        //break;
                    }

                    in.close();
                    responseFromServer = sb.toString();
                } else {
                    //return "not ok";
                    responseFromServer = new String("false : " + responseCode);
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        retry++;
    }
    System.out.println("_____________________________________________________________________________");
    System.out.println("ContentParser Response:" + responseFromServer);
    return responseFromServer;
}

    public String postData(URL url, JSONObject postDataParams){
        String responseFromServer = null;
        String sid = SettingsPreferences.getSessionID(mContext);
        int retry = 0;
        while(retry < 2) {  //retry 2 times maxium
            System.out.println("Try Attempt:"+retry+", SID:"+sid+".................................................");
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                postDataParams.put("sid",sid);

                try {
                    conn.setReadTimeout(30000 /* milliseconds */);
                    conn.setConnectTimeout(30000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    //conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=sfsdfsdf");
                    //conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream()));
                        StringBuffer sb = new StringBuffer("");
                        String line = "";

                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                            retry=5;
                            //break;
                        }

                        in.close();
                        responseFromServer = sb.toString();
                    } else {
                        //return "not ok";
                        responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Serverrr. Please check your internet connection\"}");
                    }
                    conn.disconnect();
                } catch (SocketTimeoutException e) {
                    //e.printStackTrace();
                    retry=5;    //avoid retry if timeout occurs
                    responseFromServer = new String("{\"status\":\"error\",\"error\":\"Connection Timeout. Please check your internet connection\"}");
                } catch (IOException e) {
                    //e.printStackTrace();
                    responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Server.. Please check your internet connection\"}");
                } finally {
                    conn.disconnect();
                }
            } catch (Exception e) {
                responseFromServer = new String("{\"status\":\"error\",\"error\":\"Unable to reach Server.. Please check your internet connection\"}");
                e.printStackTrace();
            }
            retry++;
        }
        System.out.println("_____________________________________________________________________________");
        System.out.println("ContentParser Response:" + responseFromServer);
        try {
            JSONObject jObj = new JSONObject(responseFromServer);
            if (jObj.has("sid")) {
                SettingsPreferences.setSessionID(mContext, jObj.getString("sid"));
            }
        } catch (JSONException e) {

        }
        return responseFromServer;
    }





    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


}
