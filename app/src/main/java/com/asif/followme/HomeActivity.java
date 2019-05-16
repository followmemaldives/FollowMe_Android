package com.asif.followme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.BoatHire.Public.PublicHireActivity;
import com.asif.followme.MyAccount.MyActivity;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import com.google.firebase.FirebaseApp;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;


public class HomeActivity extends Activity implements
        View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {
    Button btnMy,btnPublic,btnBoatHire;
    TextView homeFooter;
    Context context;
    TelephonyManager        Tel;
    public static String imei,country,operator;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String versionName;
    int versionCode;
    String uname, pwd;
//    ArrayList<String> permissions=new ArrayList<>();
    int PERMISSION_ALL = 1;
    private static AsyncTask<Void, Void, String> PreferenceAsyncTask;
    private PublicPrefTask mPrefTask = null;
//    private callAJAXTask mAJAXTask = null;
    private setHomeTask mHomeTask = null;
    private LinearLayout appUpdateBox;
    public static CookieManager cookieManager;
    TextView contactNumber;
    LinearLayout contactBox;
 //   public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        context = this;

        Tel = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        FirebaseApp.initializeApp(context);
//        token = FirebaseInstanceId.getInstance().getToken();
        country = Tel.getNetworkCountryIso();
//        System.out.println("Country 1................................................ "+country);
        operator = Tel.getNetworkOperator();
        SettingsPreferences.setOperator(context, operator);
        btnMy = (Button) findViewById(R.id.btn_my);
        btnPublic = (Button) findViewById(R.id.btn_public);
        btnBoatHire = (Button) findViewById(R.id.btn_boat_hire);
        homeFooter = (TextView) findViewById(R.id.home_footer);
        appUpdateBox = (LinearLayout) findViewById(R.id.app_update_box);
        contactNumber = (TextView) findViewById(R.id.contact_number);
        contactBox = (LinearLayout) findViewById(R.id.contact_box);
        btnMy.setOnClickListener(this);
        btnPublic.setOnClickListener(this);
        btnBoatHire.setOnClickListener(this);
        appUpdateBox.setOnClickListener(this);
        //TODO remove this after next version of Boathire launch. Keep the btnBoatHire always visible
        if(SettingsPreferences.getBoatHireService(context)== true){
            btnBoatHire.setVisibility(View.VISIBLE);
        } else {
            btnBoatHire.setVisibility(View.GONE);
        }

        Long tStamp = System.currentTimeMillis();
//        System.out.println("Serial Number: "+tStamp);


 //       if(!hasPermissions(this, PERMISSIONS)){
 //           ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
 //       }

/*        permissionUtils=new PermissionUtils(getApplicationContext());
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
*/
     //   isPhoneStatePermissionGranted();
        initCookie();

        uname = SettingsPreferences.getUserName(context);
        pwd = SettingsPreferences.getPassword(context);
//        if(!uname.equalsIgnoreCase("") && !pwd.equalsIgnoreCase("")) {
            mHomeTask = new setHomeTask();
            mHomeTask.execute((Void) null);
//        }
/*        if(tkn != null) {
            if (tkn.length() > 10 && SettingsPreferences.getTokenRef(context).equalsIgnoreCase("0")) {
                mAJAXTask = new callAJAXTask("get_token_ref", tkn);
                mAJAXTask.execute((Void) null);
            }
        }
*/
//        System.out.println("Firebse Token:"+token);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
            homeFooter.setText("info@followme.mv, Version: "+versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println("This Version is:"+versionCode+", OLD Version:"+SettingsPreferences.getVersionCode(context)+",Country:"+country+" .................................................");
        if(SettingsPreferences.getVersionCode(context) < versionCode){    //lets initialize variables on first run (after update or install)
            if(!country.equalsIgnoreCase("")){
                SettingsPreferences.setCountry(context, country);
            } else {
                country = SettingsPreferences.getCountry(context);
            }
            if(country.equals("mv")){
                contactNumber.setText("7778839");
            } else {
                contactNumber.setText("+960 7901617");
            }
            SettingsPreferences.setAlertMove(context,true);
            SettingsPreferences.setAlertMajor(context,true);
            SettingsPreferences.setAlertMinor(context,true);
            SettingsPreferences.setAlertHire(context,true);
            SettingsPreferences.setSoundMove(context,true);
            SettingsPreferences.setSoundMajor(context,false);   //not set by default
            SettingsPreferences.setSoundMinor(context,true);
            SettingsPreferences.setSoundHire(context,true);
            mPrefTask = new PublicPrefTask(country);
            mPrefTask.execute((Void) null);
        }
        AppUtils.createNotificationChannel(context);
        contactBox.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {
                    String phone = contactNumber.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        );

        //if (isInternetPresent){
        //}
   // SettingsPreferences.setStoragePermission(HomeActivity.this,isStoragePermissionGranted());
    }

    public static void initCookie(){
        cookieManager = new CookieManager();  // to maintain php session
        CookieHandler.setDefault(cookieManager);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_my:
                uname = SettingsPreferences.getUserName(context);
                pwd = SettingsPreferences.getPassword(context);
                if(uname.equals("") && pwd.equals(""))
                {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.putExtra("sender","my_account");
                    startActivity(intent);
                    //	finish();
                } else {
                    Intent intent = new Intent(HomeActivity.this, MyActivity.class);
                    intent.putExtra("reload",true);
                    intent.putExtra("sender", "home");
                 //   intent.putExtra("signin",true);
                    startActivity(intent);
                }
                break;
            case R.id.btn_public:
             //   SettingsPreferences.setListPublicTime(context, 0);	//force reload public list from server
                Intent intent = new Intent(context, PublicActivity.class);
                startActivity(intent);
                //	finish();
                break;
            case R.id.btn_boat_hire:
                //Intent intent2 = new Intent(context, PublicHireActivity.class);
                //startActivity(intent2);
                uname = SettingsPreferences.getUserName(context);
                pwd = SettingsPreferences.getPassword(context);
                Intent intent2;
                if(uname.equals("") && pwd.equals(""))
                {
                    intent2 = new Intent(context, LoginActivity.class);
                    intent2.putExtra("sender","boat_hire");
                    startActivity(intent2);
                    //	finish();
                } else {
                    intent2 = new Intent(context, PublicHireActivity.class);
                    startActivity(intent2);
                }
                break;
            case R.id.app_update_box:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //imei = Tel.getDeviceId();
                    //SettingsPreferences.setIMEI(context, imei);

                    System.out.println("Permission Granted Successfully. Write working code here. ************************************************************************************");
                } else {
                    System.out.println("You did not accept the request can not use the functionality.");
                }
                break;
        }
    }






/*
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public  boolean isPhoneStatePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                imei = Tel.getDeviceId();
                SettingsPreferences.setIMEI(context, imei);
                country = Tel.getNetworkCountryIso();
                System.out.println("Country 1................................................ "+country);
                operator = Tel.getNetworkOperator();
                SettingsPreferences.setOperator(context, operator);
                if(SettingsPreferences.getCountry(context)==""){
                    SettingsPreferences.setCountry(context, country);

                }

                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            imei = Tel.getDeviceId();
            SettingsPreferences.setIMEI(context, imei);
            country = Tel.getNetworkCountryIso();
                System.out.println("Country 2................................................ "+country);
            operator = Tel.getNetworkOperator();
            SettingsPreferences.setOperator(context, operator);
            if(SettingsPreferences.getCountry(context)==""){
                SettingsPreferences.setCountry(context, country);

            }

            return true;
        }
    }
    */




    public class PublicPrefTask extends AsyncTask<Void, Void, String> {
        private final String co;

        PublicPrefTask(String country) {
            co = country;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(getApplicationContext(), "Registering...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.SavePublicPreferences(context,co);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mPrefTask = null;
            //progressBar.setVisibility(View.GONE);

            //    showProgress(false);

            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                           // String lat = jObj.getString("lat");
                           // String lon = jObj.getString("lon");
                           // String zoom = jObj.getString("zoom");
                            SettingsPreferences.setCountryLat(context, jObj.getString("lat"));
                            SettingsPreferences.setCountryLon(context, jObj.getString("lon"));
                            SettingsPreferences.setCountryZoom(context,jObj.getInt("zoom"));
                            SettingsPreferences.setVersionCode(context,versionCode);

                        } else {
//                            SettingsPreferences.setVersionCode(context,versionCode);
                            //String errmsg = jObj.getString("error");
                            //Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }
        }

        @Override
        protected void onCancelled() {
            mPrefTask = null;
            //showProgress(false);
        }
    }


    public class setHomeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.InitializeHome(context);
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mHomeTask = null;
            //progressBar.setVisibility(View.GONE);

            //    showProgress(false);

            System.out.println("My Location REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
//                            SettingsPreferences.setSessionID(context, jObj.getString("sid"));
                            SettingsPreferences.setAd(context, jObj.getString("adv"));
                            SettingsPreferences.setTrackIP(context,jObj.getString("track_ip"));
                            SettingsPreferences.setTrackPort(context,jObj.getInt("track_port"));
                            if(jObj.getString("hire").equalsIgnoreCase("true")){
                                SettingsPreferences.setBoatHireService(context,true);
                                btnBoatHire.setVisibility(View.VISIBLE);
                            } else {
                                SettingsPreferences.setBoatHireService(context,false);
                                btnBoatHire.setVisibility(View.GONE);
                            }
                           if(jObj.getInt("version")>versionCode){
                                SettingsPreferences.setVersionCode(context,versionCode);
                                appUpdateBox.setVisibility(View.VISIBLE);
                           }
                           if(jObj.getString("serial").length()>6){
                               SettingsPreferences.setIMEI(context,jObj.getString("serial"));
                           }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }
        }

        @Override
        protected void onCancelled() {
            mHomeTask = null;
            //showProgress(false);
        }
    }

/*    public class callAJAXTask extends AsyncTask<Void, Void, String> {
        String id;
        String action;
        callAJAXTask(String action, String id) {
            this.action = action;
            this.id = id;
        }

        @Override
        protected String doInBackground(Void... arg0) {

            try {
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.getAJAX(context,action, id);
            } catch (Exception ex) {
                //finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            mAJAXTask = null;
            //showProgress(false);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            if(action.equalsIgnoreCase("get_token_ref")){
                                if(jObj.has("token_ref")){
                                    SettingsPreferences.setTokenRef(context,jObj.getString("token_ref"));
                                    SettingsPreferences.setIMEI(context,jObj.getString("token_ref"));
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Please check your internet connection 2", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAJAXTask = null;
            //showProgress(false);
        }
    }
*/
}
