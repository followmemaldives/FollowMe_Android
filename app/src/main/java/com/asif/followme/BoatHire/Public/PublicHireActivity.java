package com.asif.followme.BoatHire.Public;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.asif.followme.*;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PublicHireActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener{

    public int MyAccountType;
    private Context context;
    static SwipeRefreshLayout swipeLayout;
//    private GetMyWallet mWalletTask = null;
    public static List<DataPublicHire> data = new ArrayList<DataPublicHire>();

    static ListView lv;
//    public static String shared_vessel;
//    public static String shared_vessel_id;
//    public static String selected_fleet_id,selected_fleet_name;
    public static AlertDialog myAlert,noticeAlert,fleetAlert,newFleetAlert;
    Intent intent;
    AlertDialog.Builder fleetbuilder;
    Dialog dialog;
    static Activity activity;
    public static String selected_item_name, selected_item_id;
    final int REQUEST_MENU_DIALOG = 5;
    final int REQUEST_GROUP_DIALOG = 6;
    int HireState = 0;
    static int HireNav = 0;
    LinearLayout layoutList, layoutWelcome;
    private Button btnVerify, btnTopUp;

    MenuItem item_hire, item_filter_all,item_filter_draft,item_filter_published, item_hired;
    private static Parcelable state;
    public Intent ServiceIntent;
    public TextView MyNameView, MyEmailView;
//    private UserReLoginTask mReAuthTask = null;
    private LogoutTask mLogoutTask = null;
    private int NavigationIndex =0;
    private String NavigationName = "nav_hire_all";
    final int NAV_HIRE_ALL_INDEX = 0;
    final int NAV_HIRE_HISTORY_INDEX = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire_public_activity);  //public_activity_hire
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //Class fragmentClass = FirstFragment.class;
        setTitle("My Boat Hire/All");

        Fragment fragment = null;
        try {
            fragment = (Fragment) PublicHireFragment.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        navigationView.getMenu().getItem(0).setChecked(true);
        MyNameView = (TextView) header.findViewById(R.id.my_full_name);
        MyEmailView = (TextView) header.findViewById(R.id.my_email);
        MyNameView.setText(SettingsPreferences.getFullName(context));
        MyEmailView.setText(SettingsPreferences.getUserName(context));
        MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!AppUtils.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    //    mHireTask = new GetPublicBoatHires(HireState, HireNav);
    //    mHireTask.execute((Void) null);

    }

    public void onPause(){
        //state = lv.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        System.out.println("OnStart...............................................");
//        SettingsPreferences.setAccount(context, "my");
 //           MyAccountType = SettingsPreferences.getSelectedMyAccount(context);
            Boolean doRefresh;
            String sender = "";
            try {
                Bundle extras = getIntent().getExtras();
                doRefresh = extras.getBoolean("reload");
                sender = extras.getString("sender");
            } catch (Exception e) {
                doRefresh = false; //default no refresh
                sender="xx";
            }
            if(!sender.equalsIgnoreCase("home")) {  //if not request from home page
                if (doRefresh) {
                       // mHireTask = new GetPublicBoatHires(HireState, HireNav);
                       // mHireTask.execute((Void) null);
                } else {
                    if (MyAccountType == 5) {
                        //populateMyTrips(context);
                    } else {
                        //populateMyVessels(context);
                    }
                }
            }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
         Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();

        if(id == R.id.nav_hire_all) {
            NavigationIndex = NAV_HIRE_ALL_INDEX;
            fragmentClass = PublicHireFragment.class;
        } else if(id == R.id.nav_hire_history) {
            NavigationIndex = NAV_HIRE_HISTORY_INDEX;
            fragmentClass = PublicHistoryFragment.class;
        } else if(id == R.id.nav_my_wallet){
            setTitle("My Wallet");
            return true;
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(context, MyPreferences.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_help){
            Intent intent = new Intent(context, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.asif.followme");
            startActivity(Intent.createChooser(intent, "Share"));
            return true;
        } else if (id == R.id.nav_signout) {
            mLogoutTask = new LogoutTask();
            mLogoutTask.execute((Void) null);
            return true;
        }



        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         //   case R.id.btn_verify:
         //   Intent intent = new Intent(activity, VerifyActivity.class);
         //   startActivity(intent);
         //   break;

        }
    }

    public class LogoutTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
//          System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
//            swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            //			fetchDataFromJSON();
            //	uname= loginPreferences.getString("username", "");
            //	pwd= loginPreferences.getString("password", "");
            //	System.out.println("DoBackground....");
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.Logout(context);

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
 //           swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //swipeLayout.setRefreshing(false);
            //System.out.println(result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            SettingsPreferences.setUserName(context,"");
                            SettingsPreferences.setPassword(context,"");
                            Database db = new Database(context);
                            db.deleteMy();
                            intent	=	new Intent(context, LoginActivity.class);
                            intent.putExtra("sender","boat_hire");
                            startActivity(intent);
                            finish();

                        } else {
                            AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                        //	System.out.println("Result"+result);

                    }
                } catch (JSONException e) {
                    //loading.setText("Unable to reach Server...");
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                //Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

            }
        }
    }


/*   public class UserReLoginTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... arg0) {
            // TODO: attempt authentication against a network service.

            try{
                String uname = SettingsPreferences.getUserName(context);
                String pwd = SettingsPreferences.getPassword(context);
                ContentParser parser = new ContentParser(getBaseContext());
                return parser.AuthenticateFM(context,uname,pwd);
                //GetText();
            }
            catch(Exception ex)
            {
                //finish();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final String result) {
            mReAuthTask = null;
            //showProgress(false);

//            System.out.println("Login REsult: "+result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
//					System.out.println("SID:"+jObj.getString("sid"));
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //   Time m = new Time();
                            //   m.setToNow();
                            //   long m1 = m.toMillis(true);
                            //   SettingsPreferences.setListMyTime(context, m1);
//                            SettingsPreferences.setSessionID(context, jObj.getString("sid"));
                            //    SettingsPreferences.setAccess(context, jObj.getString("access"));
                            //    SettingsPreferences.setAd(context,jObj.getString("ad"));
                            if(jObj.getString("reg").equalsIgnoreCase("true")){
                                SettingsPreferences.setIsTracker(context,true);
                            } else {
                                SettingsPreferences.setIsTracker(context,false);
                            }
                            AppUtils.saveMyVessel(jObj, context);

                        } else {
                            String errmsg = jObj.getString("error");
                         //   Toast.makeText(getApplicationContext(), "Error: 122-2,Uname:"+SettingsPreferences.getUserName(context)+",PWD:"+SettingsPreferences.getPassword(context)+","+jObj.getString("error"), Toast.LENGTH_LONG).show();
                            AppUtils.showAlertDialog(context, "Error", errmsg);
//							Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Unable to reach server. Please check your internet connection", Toast.LENGTH_LONG).show();
                //AppUtils.showAlertDialog(context, "Timeout", "Unable to reach Server. Please try again!");
            }





        }

        @Override
        protected void onCancelled() {
            mReAuthTask = null;
           // showProgress(false);
        }
    }
*/


}
