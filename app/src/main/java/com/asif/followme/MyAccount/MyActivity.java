package com.asif.followme.MyAccount;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.asif.followme.BoatHire.Operator.HireMyFragment;
import com.asif.followme.*;
import com.asif.followme.TripPlan.TripPlanFragment;
import com.asif.followme.manager.ContentParser;
import com.asif.followme.model.DataMy;
import com.asif.followme.util.AppUtils;
import com.asif.followme.util.SettingsPreferences;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Context context;
    static SwipeRefreshLayout swipeLayout;
//    public static List<DataMy> result = new ArrayList<DataMy>();
    public static List<DataMy> data = new ArrayList<DataMy>();
//    public static List<DataMyHire> dataHire = new ArrayList<DataMyHire>();

//    private GetMyTrips mTripTask = null;
//    public static List<DataMyTrip> db_result2 = new ArrayList<DataMyTrip>();
//    public static List<DataMyHire> data = new ArrayList<DataMyHire>();

//    static ListView lv;
//    public static String shared_vessel;
//    public static String shared_vessel_id;
//    public static String selected_fleet_id,selected_fleet_name;
    public static AlertDialog myAlert,noticeAlert,fleetAlert,newFleetAlert;
    Intent intent;
//    AlertDialog.Builder fleetbuilder;
//    Dialog dialog;
    static Activity activity;
    public static String selected_item_name, selected_item_id;
/*    final int REQUEST_MENU_DIALOG = 5;
    final int REQUEST_GROUP_DIALOG = 6;
    MenuItem item_device, item_group, item_trip;
    MenuItem item_bids_all, item_bids_my;
    private ToggleButton switchLive;
    */
//    private static MenuItem switchItem;
//    private static Parcelable state;
    public static Intent ServiceIntent;
    public TextView MyNameView, MyEmailView;
//    private UserReLoginTask mReAuthTask = null;
    private LogoutTask mLogoutTask = null;
//    private String bidList = "all";
//    private NavigationView navigationView;
    private TextView NavHireCounter,DrawerCounter;
//    private int NavigationIndex =0;
//    private NavigationMenu NavigationItem;
    private String NavigationName = "nav_my_vessels";
    private int NavigationId = 0;
/*    final int NAV_MY_BOAT_INDEX = 0;
    final int NAV_MY_GROUP_INDEX = 1;
    final int NAV_BOOKING_INDEX = 2;
    final int NAV_TRIP_PLAN_INDEX = 2;
    final int NAV_BOAT_HIRE_INDEX = 3;
    final int NAV_WALLET_INDEX = 4;
*/
    private int hire_alert_count =0;
//    private NavigationMenu NavigationHireGroup;
    private LinearLayout profileEditBox;

    public static Parcelable state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//        lv=(ListView) findViewById(R.id.list);
     //   MyNameView = (TextView) findViewById(R.id.my_full_name);
        ServiceIntent = new Intent(getApplicationContext(), LocationService.class);
    //    MyNameView.setText(SettingsPreferences.getFullName(context));




 /*       if(MyAccountType == 5){
            System.out.println("Account Type 5:"+MyAccountType);
           populateMyTrips(context);
        } else {
            System.out.println("Account Type Other:"+MyAccountType);
            //populateMyVessels(context);
        }
  */
 //    populateMyVessels(context);
 /*       MyAccountType = SettingsPreferences.getSelectedMyAccount(context);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(MyAccountType ==  5) {
                            mTripTask = new GetMyTrips(MyAccountType);
                            mTripTask.execute((Void) null);
                        } if(MyAccountType == 6){
                            mHireTask = new GetMyBoatHires(bidList);
                            mHireTask.execute((Void) null);

                        } else {
                            mVesselTask = new GetMyVessels(MyAccountType,"swipe");
                            mVesselTask.execute((Void) null);
                        }
                    }
                }
        );

*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        NavHireCounter = (TextView) navigationView.getMenu().findItem(R.id.nav_boat_hire).getActionView();
        DrawerCounter = (TextView) findViewById(R.id.drawer_badge);
//        NavHireCounter.setText("222");
        //NavHireCounter = (TextView) navigationView.getMenu().findItem(R.id.nav_boat_hire).getActionView().findViewById(R.id.nav_hire_counter);

        MyNameView = (TextView) header.findViewById(R.id.my_full_name);
        MyEmailView = (TextView) header.findViewById(R.id.my_email);
        MyNameView.setText(SettingsPreferences.getFullName(context));
        MyEmailView.setText(SettingsPreferences.getUserName(context));
        this.activity = this;

        profileEditBox = (LinearLayout) header.findViewById(R.id.profile_edit_box);
        profileEditBox.setOnClickListener(this);
/*        String[] PERMISSIONS = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.WAKE_LOCK, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!AppUtils.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
*/
        NavigationName = SettingsPreferences.getMyNavigationName(context);
        NavigationId = SettingsPreferences.getMyNavigationId(context);
        //navigationView.getMenu().getItem(NavigationIndex).setChecked(true);
        if(navigationView.getMenu().findItem(NavigationId)!=null) {
            navigationView.setCheckedItem(NavigationId);
        } else {
            navigationView.getMenu().getItem(0).setChecked(true);
        }
//        System.out.println("Navigatioin Name:"+NavigationName+" +++++++++++++++++++++++++++++++++++++++++");
        Fragment newFragment = null;
        switch (NavigationName){
            case "nav_my_groups":
                newFragment = new MyGroupsFragment();
                break;
            case "nav_trip_plan":
                newFragment = new TripPlanFragment();
                break;
            case "nav_boat_hire":
                clearBadgeCounters();
                newFragment = new HireMyFragment();
                break;
            default:
                newFragment = new MyBoatsFragment();
                navigationView.getMenu().getItem(0).setChecked(true);
                SettingsPreferences.setMyNavigationName(context,"nav_my_vessels");

                break;
        }
        //newFragment = new MyBoatsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,newFragment);
        //   transaction.addToBackStack(null);
        transaction.commit();


        if(SettingsPreferences.getBoatHireService(context)== true){
            navigationView.getMenu().findItem(R.id.nav_boat_hire).setVisible(true);
        } else {
            navigationView.getMenu().findItem(R.id.nav_boat_hire).setVisible(false);
        }

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        setBadgeCounters();
//        Boolean reload = false;
/*        MyAccountType = SettingsPreferences.getSelectedMyAccount(context);
        try{
            Bundle extras = getIntent().getExtras();
            reload = extras.getBoolean("reload");
            if(reload) {
                switch(MyAccountType){
                    case 5:
                        navigationView.getMenu().getItem(3).setChecked(true);
                        mTripTask = new GetMyTrips(MyAccountType);
                        mTripTask.execute((Void) null);
                        break;
                    case 6:
                        navigationView.getMenu().getItem(4).setChecked(true);
                        mHireTask = new GetMyBoatHires(bidList);
                        mHireTask.execute((Void) null);
                        break;
                    case 1:
                        navigationView.getMenu().getItem(1).setChecked(true);
                        mVesselTask = new GetMyVessels(MyAccountType,"onstart");
                        mVesselTask.execute((Void) null);
                        break;
                    default:
                        navigationView.getMenu().getItem(0).setChecked(true);
                        System.out.println("OnStart GetMyVessels when Extras=reload .............................................");
                        mVesselTask = new GetMyVessels(MyAccountType,"onstart");
                        mVesselTask.execute((Void) null);

                }
//                mVesselTask = new GetMyVessels(MyAccountType);
//                mVesselTask.execute((Void) null);
            } else {
                switch(MyAccountType){
                    case 5:
                        populateMyTrips(context);
                    case 6:
                        mHireTask = new GetMyBoatHires(bidList);
                        mHireTask.execute((Void) null);
                        break;
                    default:
                        populateMyVessels(context);
                }
            }

        } catch (Exception e){
        }
*/
    }

    public void setBadgeCounters(){
        hire_alert_count = SettingsPreferences.getHireAlertCount(context);
        System.out.println("Hire Alert Count:"+hire_alert_count);
        if(hire_alert_count > 0) {
            NavHireCounter.setVisibility(View.VISIBLE);
            DrawerCounter.setVisibility(View.VISIBLE);
            if (hire_alert_count < 100) {
                String c = Integer.toString(hire_alert_count);
                NavHireCounter.setText(c);
                DrawerCounter.setText(c);
            } else {
                NavHireCounter.setText("+99");
                DrawerCounter.setText("+99");
            }
        }

    }
    public void clearBadgeCounters(){
        SettingsPreferences.clearHireAlertCount(context);
        NavHireCounter.setVisibility(View.GONE);
        DrawerCounter.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onPause(){
       // state = lv.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        System.out.println("OnStart...............................................");
//        SettingsPreferences.setAccount(context, "my");
/*            MyAccountType = SettingsPreferences.getSelectedMyAccount(context);
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
                    switch(MyAccountType){
                        case 5:
                            mTripTask = new GetMyTrips(MyAccountType);
                            mTripTask.execute((Void) null);
                            break;
                        case 6:
                            mHireTask = new GetMyBoatHires(bidList);
                            mHireTask.execute((Void) null);
                            break;
                        default:
                            mVesselTask = new GetMyVessels(MyAccountType,"onResume");
                            mVesselTask.execute((Void) null);

                    }
                } else {
                    switch(MyAccountType){
                        case 5:
                            populateMyTrips(context);
                        case 6:
                            mHireTask = new GetMyBoatHires(bidList);
                            mHireTask.execute((Void) null);
                            break;
                        default:
                            populateMyVessels(context);
                    }
                }
            }
*/
    }

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switchItem = menu.findItem(R.id.my_switch_item);
        if(SettingsPreferences.getIsTracker(context)) {
            switchItem.setVisible(true);
        } else {
            switchItem.setVisible(false);
        }
        return true;
    }

*/
/*
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        item_device = menu.findItem(R.id.action_new_device);
        item_group = menu.findItem(R.id.action_new_group);
        item_trip = menu.findItem(R.id.action_new_trip);
        item_bids_all = menu.findItem(R.id.action_bids_all);
        item_bids_my = menu.findItem(R.id.action_bids_my);
        switchItem = menu.findItem(R.id.my_switch_item);
        //switchLive = (ToggleButton) menu.findItem(R.id.switch_live);
        switchLive = (ToggleButton) switchItem.getActionView().findViewById(R.id.switch_live);
        //ToggleButton tb  = (ToggleButton) menu.findItem(R.id.my_switch_item).getActionView().findViewById(R.id.switch_live);
        //switchLive = (ToggleButton) findViewById(R.id.switch_live);
            MyAccountType = SettingsPreferences.getSelectedMyAccount(context);

        if(MyAccountType==0){   //my vessels
            item_device.setVisible(true);
            item_group.setVisible(false);
            item_trip.setVisible(false);
            item_bids_all.setVisible(false);
            item_bids_my.setVisible(false);
            setTitle("My Account");
        } else if(MyAccountType == 5) {  //trip plan
            item_device.setVisible(false);
            item_group.setVisible(false);
            item_trip.setVisible(true);
            item_bids_all.setVisible(false);
            item_bids_my.setVisible(false);
            setTitle("Trip Plans");
        } else if(MyAccountType == 6){
            item_device.setVisible(false);
            item_group.setVisible(false);
            item_trip.setVisible(false);
            item_bids_all.setVisible(true);
            item_bids_my.setVisible(true);
            setTitle("Boat Hire Requests");
        } else {
            item_device.setVisible(false);
            item_group.setVisible(true);
            item_trip.setVisible(false);
            item_bids_all.setVisible(false);
            item_bids_my.setVisible(false);
            setTitle("My Groups");
        }
        if(SettingsPreferences.getTrackMe(context).equalsIgnoreCase("on")){
            if(isMyServiceRunning(LocationService.class)){
                switchLive.setChecked(true);
            } else {
                switchLive.setChecked(false);
            }
        } else {
            switchLive.setChecked(false);
        }

            switchLive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean track) {
                    if(track){
//                        System.out.println("Track ON");
                        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WAKE_LOCK};
                        if(!AppUtils.hasPermissions(context, PERMISSIONS)){
                            ActivityCompat.requestPermissions(MyActivity.this, PERMISSIONS, 2);
                        } else {
                            SettingsPreferences.setTrackMe(context, "on");
                            startService(ServiceIntent);
                        }

                    } else {
                        SettingsPreferences.setTrackMe(context,"off");
//                        System.out.println("Track OFF");
                        stopService(ServiceIntent);
                        //Your code when unchecked
                    }
                }
            });
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.my, menu);
       // SubMenu subMenu1 = menu.addSubMenu("");

        return true;
    }
*/
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_new_device){
            intent		=	new Intent(context, NewDevice.class);
            activity.startActivityForResult(intent,4);
            //startActivity(intent);
            return true;
        }
        if(id == R.id.action_new_group){
            intent		=	new Intent(context, NewGroupForm.class);
            intent.putExtra("action","new");
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_new_trip){
            intent		=	new Intent(context, NewTripForm.class);
            activity.startActivityForResult(intent,9);
            return true;
        }
        if(id == R.id.action_bids_all){
            bidList = "all";
            setTitle("Boat Hire Requests");
            mHireTask = new GetMyBoatHires(bidList);
            mHireTask.execute((Void) null);
            return true;
        }
        if(id == R.id.action_bids_my){
            bidList = "my";
            setTitle("My Bids");
            mHireTask = new GetMyBoatHires(bidList);
            mHireTask.execute((Void) null);
            return true;

        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, MyPreferences.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();
        NavigationId = id;
        SettingsPreferences.setMyNavigationId(context,id);
        String nav_name = getResources().getResourceEntryName(id);
        SettingsPreferences.setMyNavigationName(context,nav_name);
        if(id == R.id.nav_my_vessels) {
//            NavigationIndex = NAV_MY_BOAT_INDEX;
//            SettingsPreferences.setMyNavigationName(context,nav_name);
            fragmentClass = MyBoatsFragment.class;
        } else if(id == R.id.nav_my_groups) {
//            NavigationIndex = NAV_MY_GROUP_INDEX;
//            SettingsPreferences.setMyNavigationName(context,nav_name);
            fragmentClass = MyGroupsFragment.class;
        } else if(id==R.id.nav_trip_plan){
//            NavigationIndex = NAV_TRIP_PLAN_INDEX;
//            SettingsPreferences.setMyNavigationName(context,nav_name);
            fragmentClass = TripPlanFragment.class;
        } else if(id == R.id.nav_boat_hire){
            clearBadgeCounters();
//            NavigationIndex = NAV_BOAT_HIRE_INDEX;
//            SettingsPreferences.setMyNavigationName(context,nav_name);
            fragmentClass = HireMyFragment.class;
        } else if(id == R.id.nav_my_wallet){
            fragmentClass = WalletFragment.class;
        } else if(id == R.id.nav_my_wallet){
            return true;
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(context, MyPreferences.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_help){
            Intent intent = new Intent(context, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_buy){
            Intent intent = new Intent(context, BuyActivity.class);
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
        //SettingsPreferences.setSelectedMyAccount(context,NavigationIndex);
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




/*

        if (id == R.id.nav_my_vessels) {
            MyAccountType = 0;
//            item_device.setVisible(true);
//            item_group.setVisible(false);
//            item_trip.setVisible(false);
//            item_bids_all.setVisible(false);
//            item_bids_my.setVisible(false);
//            setTitle("My Account");
            SettingsPreferences.setSelectedMyAccount(context,MyAccountType);
            mVesselTask = new GetMyVessels(MyAccountType,"navigation");
            mVesselTask.execute((Void) null);
        } else if (id == R.id.nav_my_groups) {
            MyAccountType = 1;
            item_device.setVisible(false);
            item_group.setVisible(true);
            item_trip.setVisible(false);
            item_bids_all.setVisible(false);
            item_bids_my.setVisible(false);
            setTitle("My Groups");
            SettingsPreferences.setSelectedMyAccount(context,MyAccountType);
            mVesselTask = new GetMyVessels(MyAccountType,"navigation");
            mVesselTask.execute((Void) null);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(context, MyPreferences.class);
            startActivity(intent);
        } else if (id == R.id.nav_help){
            Intent intent = new Intent(context, HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.asif.followme");
            startActivity(Intent.createChooser(intent, "Share"));
        } else if (id == R.id.nav_signout) {
            mLogoutTask = new LogoutTask();
            mLogoutTask.execute((Void) null);
//            logout();
//            intent	=	new Intent(context, LoginActivity.class);
//            startActivity(intent);
//            finish();

        } else if(id==R.id.nav_trip_plan){
            MyAccountType = 5;
            item_device.setVisible(false);
            item_group.setVisible(false);
            item_trip.setVisible(true);
            item_bids_all.setVisible(false);
            item_bids_my.setVisible(false);
            setTitle("Trip Plans");
            SettingsPreferences.setSelectedMyAccount(context,MyAccountType);
            mTripTask = new GetMyTrips(MyAccountType);
            mTripTask.execute((Void) null);

        } else if(id==R.id.nav_boat_hire){
            MyAccountType = 6;
            bidList ="all";
            item_device.setVisible(false);
            item_group.setVisible(false);
            item_trip.setVisible(false);
            item_bids_all.setVisible(true);
            item_bids_my.setVisible(true);
            setTitle("Boat Hire Requests");
            SettingsPreferences.setSelectedMyAccount(context,MyAccountType);
            mHireTask = new GetMyBoatHires(bidList);    //all open,win,all bid,
            mHireTask.execute((Void) null);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

        */
    }

/*    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("onItemClick at MyActivity...................");
        if(MyAccountType == 5) {
            DataMyTrip object = (DataMyTrip) adapterView.getItemAtPosition(i);
            SettingsPreferences.setSelectedDevice(context, object.getId());
            selected_item_name = object.getName();
            selected_item_id = object.getId();
        } else if(MyAccountType == 6){  //boat hire operator list click
            DataMyHire object = (DataMyHire) adapterView.getItemAtPosition(i);
            selected_item_name = object.getName();
            selected_item_id = object.getId();
            Intent intent = new Intent(MyActivity.this, MyBidActivity.class);
            intent.putExtra("title",selected_item_name);
//            intent.putExtra("selected_id", selected_item_id);
//			Intent intent = new Intent(Get_list.this, DrawPointActivity.class);
            startActivity(intent);
            finish();

        } else {
            DataMy object = (DataMy) adapterView.getItemAtPosition(i);
            SettingsPreferences.setSelectedDevice(context, object.getId());
            selected_item_name = object.getName();
            selected_item_id = object.getId();
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

            if (resultCode == ConnectionResult.SUCCESS)
            {
                Intent intent = new Intent(MyActivity.this, MyMapsActivity.class);
                intent.putExtra("title",selected_item_name);
//            intent.putExtra("selected_id", selected_item_id);
//			Intent intent = new Intent(Get_list.this, DrawPointActivity.class);
                startActivity(intent);
                finish();
                //		Toast.makeText(getApplicationContext(),"isGooglePlayServicesAvailable SUCCESS",Toast.LENGTH_LONG).show();
            }
            else
            {
                //GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, requestCode);
                dialog.show();
            }
        }

    }
*/
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("Request Code:"+requestCode);
 //       System.out.println("Result Code:"+resultCode);
//        System.out.println("Data:"+data);
        switch(requestCode) {
            case (4):{  //return from New Device form
                if (resultCode == Activity.RESULT_OK) {
                    String action = data.getStringExtra("action");
                    if (action.equalsIgnoreCase("self_track")) {  // mobile phone added as a tracker...
                        if (SettingsPreferences.getIsTracker(context)) {
                            switchItem.setVisible(true);
                            AppUtils.showAlertDialog(context, "Self Tracking", "You have just added mobile phone as a tracking device. To start/stop tracking, click the TRACK button at the top");
                        } else {
                            switchItem.setVisible(false);
                        }
                    }
                }
                break;
            }
            case (REQUEST_MENU_DIALOG):{ //5
//                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    final String action = data.getStringExtra("action");
                    final String device_id =MyActivity.selected_item_id;
                    String title = selected_item_name;
                    String message ="";
                    if(action.equalsIgnoreCase("rem")){
                        message = "Do you want to Remove this Device from My Account?";
                    }
                    if(action.equalsIgnoreCase("del")){
                       message = "This will Delete the device & its history permanently from all shared users. Do you want to Delete this Device Now?";
                    }
                    if(action.equalsIgnoreCase("trip_delete")){
                        message = "Do you want to Delete the Trip Plan Now?";
                    }
//                    System.out.println("Action:"+action);
                    AppUtils.showConfirmDialog(context,title,message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteDevice mDeleteTask = new DeleteDevice(context,device_id,action);
                                mDeleteTask.execute((Void) null);
                            }
                        });
                }
                break;
            }
            case (REQUEST_GROUP_DIALOG): { //6
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    final String action = data.getStringExtra("action");
                    final String group_id =MyActivity.selected_item_id;
                    String title = selected_item_name;
                    if(action.equalsIgnoreCase("group_delete")) {
                        AppUtils.showConfirmDialog(context, title, "Do you want to Delete this Group Now?",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteGroup mGroupTask = new DeleteGroup(context, group_id, "del");
                                        mGroupTask.execute((Void) null);
                                    }
                                });
                    }
                }
                break;
            }

        }
    }
*/
    public static void selectMenuItem(int acc) {
        Intent intent;
        switch(acc){
            case 0:
                intent = new Intent(activity, MyMenuActivity.class);
                intent.putExtra("vname",selected_item_name);
                activity.startActivityForResult(intent,5);
                break;
            case 5:
                intent = new Intent(activity, MyTripMenuActivity.class);
                intent.putExtra("vname",selected_item_name);
                activity.startActivityForResult(intent,5);
                break;
            default:
                intent = new Intent(activity, MyGroupMenuActivity.class);
                intent.putExtra("vname",selected_item_name);
                activity.startActivityForResult(intent,6);


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.profile_edit_box:
                Intent intent = new Intent(context, ProfileActivity.class);
                startActivity(intent);
                break;

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
                            intent.putExtra("sender","my_account");
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


/*    public class GetMyVessels extends AsyncTask<Void, Void, String> {
        private final int id2;
        private final String sender;

        GetMyVessels(int id,String sender) {
            id2 = id;
            this.sender=sender;
        }
            @Override
            protected void onPreExecute() {
//              System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
                swipeLayout.setRefreshing(true);

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
                    String result = parser.GetMyVessels(context,id2,sender);

                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject jObj = new JSONObject(result);
                            if (jObj.has("status")) {
                                String statuss = jObj.getString("status");
                                if (statuss.equalsIgnoreCase("ok")) {
                                    AppUtils.saveMyVessel(jObj, context);
                               }
                               return result;
                             }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     }

                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onCancelled() {
                swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                swipeLayout.setRefreshing(false);
                //System.out.println(result);
                SettingsPreferences.setSelectedMyAccount(context, id2);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            String statuss = jObj.getString("status");
                            if (statuss.equalsIgnoreCase("ok")) {
                            //    Time m = new Time();
                            //    m.setToNow();
                             //   long m1 = m.toMillis(true);
                             //   SettingsPreferences.setListMyTime(context, m1);
                                //	errmsg = jObj.getString(TAG_ERROR_MSG);
//                                AppUtils.saveMyVessel(jObj, context);
                                //populateMyVessels(context);
                                //	System.out.println(jObj.toString());

                                //	loading.setText("Login Successful...");
                                //	Intent intent = new Intent(context, Get_list.class);
                                //	startActivity(intent);
                                //	finish();
                            } else {
                                if(jObj.getString("status").equalsIgnoreCase("login")){
                                    mReAuthTask = new UserReLoginTask();
                                    mReAuthTask.execute((Void) null);

                                //    Intent intent = new Intent(MyActivity.this, LoginActivity.class);
                                 //   startActivity(intent);
                                 //   finish();
                                } else {
                                //    Toast.makeText(getApplicationContext(), "Error: 122-1"+jObj.getString("error"), Toast.LENGTH_LONG).show();
                                    AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                                }
                                //	loading.setText("Authentication failed");
                                //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                                //	startActivity(intent);
                                //	finish();
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
*/
/*    public static void populateMyVessels(Context context){
        Database db = new Database(context);
        result = db.fetchallMydata();
        lv.setAdapter(new MyListAdapter(context, result));
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        if(result.size()==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
        }
        if(state != null) {
            lv.onRestoreInstanceState(state);
        }
    }
*/

/*    public class UserReLoginTask extends AsyncTask<Void, Void, String> {


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
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    /*    switch (requestCode) {
            case 1:
                    if(MyAccountType ==  5){
                        mTripTask = new GetMyTrips(MyAccountType);
                        mTripTask.execute((Void) null);

                    } else {
                        mVesselTask = new GetMyVessels(MyAccountType,"onstart");
                        mVesselTask.execute((Void) null);
                    }

                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SettingsPreferences.setTrackMe(context, "on");
                    startService(ServiceIntent);
 //                  System.out.println("Permission Granted Successfully. Write working code here. ************************************************************************************");
                } else {
                    switchLive.setChecked(false);
                    SettingsPreferences.setTrackMe(context,"off");
//                    System.out.println("You did not accept the request can not use the functionality.");
                }
                break;

        }
        */
 //   }

/*    public class GetMyTrips extends AsyncTask<Void, Void, String> {
        private final int id2;

        GetMyTrips(int id) {
            id2 = id;
        }
        @Override
        protected void onPreExecute() {
//                System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
            swipeLayout.setRefreshing(true);

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
                String result = parser.GetMyTrips(context,id2);

                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject jObj = new JSONObject(result);
                        if (jObj.has("status")) {
                            String statuss = jObj.getString("status");
                            if (statuss.equalsIgnoreCase("ok")) {
                                AppUtils.saveMyTrips(jObj, context);
                            }
                            return result;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeLayout.setRefreshing(false);
            //System.out.println(result);
            SettingsPreferences.setSelectedMyAccount(context, id2);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //    Time m = new Time();
                            //    m.setToNow();
                            //   long m1 = m.toMillis(true);
                            //   SettingsPreferences.setListMyTime(context, m1);
                            //	errmsg = jObj.getString(TAG_ERROR_MSG);
//                                AppUtils.saveMyVessel(jObj, context);
                            populateMyTrips(context);
                            //	System.out.println(jObj.toString());

                            //	loading.setText("Login Successful...");
                            //	Intent intent = new Intent(context, Get_list.class);
                            //	startActivity(intent);
                            //	finish();
                        } else {
                            if(jObj.getString("status").equalsIgnoreCase("login")){
                                mReAuthTask = new MyActivity.UserReLoginTask();
                                mReAuthTask.execute((Void) null);

                                //    Intent intent = new Intent(MyActivity.this, LoginActivity.class);
                                //   startActivity(intent);
                                //   finish();
                            } else {
                                AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                            }
                            //	loading.setText("Authentication failed");
                            //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                            //	startActivity(intent);
                            //	finish();
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
*/
/*    public static void populateMyTrips(Context context){
        Database db = new Database(context);
        db_result2 = db.fetchallMyTripdata();
        lv.setAdapter(new MyTripAdapter(context, db_result2));
        lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        if(db_result2.size()==0){
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show();
        }
        if(state != null) {
            lv.onRestoreInstanceState(state);
        }
    }
*/
/*    public class GetMyBoatHires extends AsyncTask<Void, Void, String> {
        String nav;
        GetMyBoatHires(String nav) {
            this.nav = nav;
        }
        @Override
        protected void onPreExecute() {
//              System.out.println("Calling GetMyVessels with id:"+id2+"++++++++++++++++++++++++++++++++++++");
            swipeLayout.setRefreshing(true);

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try{
                ContentParser parser = new ContentParser(getBaseContext());
                String result = parser.GetMyBoatHires(context, nav);
                return result;
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            swipeLayout.setRefreshing(false);
//			System.out.println("Cancelled.....................");
//		    onMyAsyncTaskCompletedListener(null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeLayout.setRefreshing(false);
            //System.out.println(result);
        //    SettingsPreferences.setSelectedMyAccount(context, MyAccountType);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.has("status")) {
                        String statuss = jObj.getString("status");
                        if (statuss.equalsIgnoreCase("ok")) {
                            //result = AppUtils.drawMyBoatHireList(jObj, context);
                            dataHire = AppUtils.drawMyBoatHireList(jObj,context);
                            //lv=(ListView)findViewById(R.id.list);
                            //lv.setAdapter(new MyListAdapter(context, data));
                            lv.setAdapter(new MyHireAdapter(context,dataHire));
                            lv.setOnItemClickListener((AdapterView.OnItemClickListener) context);

                        } else {
                            AppUtils.showAlertDialog(context, "Error", jObj.getString("error"));
                        }
                        //	loading.setText("Authentication failed");
                        //	Intent intent = new Intent(SplashScreen.this, Login_Activity.class);
                        //	startActivity(intent);
                        //	finish();

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
*/
}
